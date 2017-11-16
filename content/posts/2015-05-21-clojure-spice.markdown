---
title:  "Spicing up Java projects with Clojure"
date:   2015-05-21
redirect_from: /clojure/java/2015/05/21/clojure-spice.html
tags:   [clojure, java]
author: Johannes Staffans
uuid:   738cd775-b94a-4931-b111-e434a4059cc1
---

Many engineers with an interest in Clojure are probably working on Java code bases for most of the day. 
This doesn't have to be the end of the world if you're tickling to write some Clojure code, as there are many ways of 
bringing Clojure to existing Java-based projects. 

You can opt to build parts of the system separately using Clojure, providing a JAR file that can be used 
in the main project. It's even relatively straight-forward to write Clojure namespaces alongside the 
Java packages and do Java-Clojure interop. But there are also some subtler ways of using Clojure with
legacy Java code bases. One of those is adding a REPL to your Java application and using it to do
REPL-based testing and exploration with Clojure. 

### JVM Break Glass: driving Spring with Clojure

An excellent place to start is the [JVM Break Glass library](https://github.com/matlux/jvm-breakglass). The purpose of this library is to provide
REPL facilities for traditional enterprise Java applications. It works especially well for Spring-based
applications. Once you have a REPL running, you can use methods provided by the library to fetch
Spring beans and interact with them as you would with any other Java object. 

If your Java application is anything like the ones I've seen, it probably provides a REST API of some
sort and is most likely organized into different layers — Controllers, Services and so on. In addition
to unit tests, I have often seen people using cURL or even a browser as the main tool for test-driving 
the system, which of course means going through the Controller layer even if the thing we are interested 
in is actually a piece of business logic in some deeper application layer. 

Being able to play around with beans in the Service layer directly in a running system is a 
way of focusing exactly on the thing you're interested in and iterating rapidly. This is second
nature to Clojurists who do REPL-driven programming all the time, but almost magical to most
Java programmers.

#### Getting started

First add the JVM Break Glass library as a dependency to your project:

```xml
<dependency>
    <groupId>net.matlux</groupId>
    <artifactId>jvm-breakglass</artifactId>
    <version>0.0.8</version>
</dependency>
```

Spring then needs to be configured to launch the REPL when the application starts.
In this example, we are only interested in having a REPL during local development and not in production,
which we can achieve using Spring profiles:

```java
@Bean
@Profile("dev")
public NreplServerSpring repl() {
    return new NreplServerSpring(1112);
}
```

This will launch an nREPL listening to port 1112 when the application is launched with the JVM option `-Dspring.profiles.active=dev`. 

With the application running, you can then simply connect to the nREPL server with Leiningen or using whatever 
Clojure development environment you otherwise use. 

So far, so good. When you have a REPL open, the following will get you started:

```clojure
(use 'cl-java-introspector.spring)
(use 'cl-java-introspector.core)
=> nil
```

After that, start looking for your beans and play around!

```clojure
(filter #(.contains % "Service") (get-beans))
=> ("paymentService" "reportingService")
(def reporting-service (get-bean "reportingService"))
=> #'user/reporting-service
(methods-info reporting-service)
...
(.getPayments reporting-service)
=> []  ; no payments yet!
```

This is already quite powerful for exploring APIs, driving Service layer beans and so on. The [JVM Break Glass project page](https://github.com/matlux/jvm-breakglass) has some other examples of what is possible using the REPL. But besides pure exploration, there are lots more things you can do once you have an nREPL hook into your project.

#### Data visualisations with Incanter

One way of leveraging Clojure in Java projects is taking advantage of its impressive data processing and analysis capabilities. 
I recommend the [Clojure Data Analysis Cookbook](https://www.packtpub.com/big-data-and-business-intelligence/clojure-data-analysis-cookbook) for anyone interested in learning more about patterns for data crunching
in Clojure. Here, we'll take a look at how to visualise data using the [Incanter](http://incanter.org/) library. 

I think visualising data flowing through a live application is one of the best ways of gaining deeper understanding
into how the application works. Of course, everything we do here can also be accomplished by logging data and using 
an external data analysis application, but being able to do it directly in the application brings a whole different
level of interactivity to the table.

First, we'll add one more dependency to our project:

```xml
<dependency>
    <groupId>im.chit</groupId>
    <artifactId>vinyasa</artifactId>
    <version>0.3.4</version>
</dependency>
```

The [vinyasa library](https://github.com/zcaudate/vinyasa) provides a kind of toolbelt for REPL-driven development in Clojure. One of the tools included is a
utility called [pull](https://github.com/zcaudate/vinyasa#pull). Using this function, Maven dependencies can be pulled in directly from the REPL,
without the need for adding dependencies to your project. More importantly, there is no need to restart anything, which makes it perfect
for REPL exploration.

```clojure
; This will take a long time the first time you do it because 
; incanter has lots of dependencies. After that, the dependencies 
; will be cached in your local Maven repository. 
(pull 'incanter)
=> ; lots of dependencies ...
```

As an example, we'll do some exploration of the [toy payment system](http://jstaffans.github.io/blog/2015/03/16/spring-boot.html) I wrote about in a previous blog post.
Given a credit card number, the service will perform a charge of 1,000 €. There is however a subtle bug — the sum always seems to be close to 1,000 €, but never exactly 1,000 €.
We decide to explore the problem a bit by performing a series of payments using a test credit card number and plotting a histogram of the charged sums.

As this is more involved than a few lines of code at the REPL, we'll save our work as a .clj file in a special folder: `src/exploration/clojure/visualise_payments.clj`. 

```clojure
(ns visualise-payments
 (:require
  [cl-java-introspector.spring :refer :all]
  [cl-java-introspector.core :refer :all]
  [vinyasa.pull :refer [pull]]))

; Get the Spring service bean we want to test drive
(def paymentService (get-bean "paymentService"))

; Helpers for working with RxJava observables and Java beans
(defn- result-from-observable
  [observable]
  (.first (.toBlocking observable)))

(defn- sum-from-result
  [result]
  (.getSum (.getPayment result)))

; create a sequence of test payments 
(defn- do-test-payments
  [n]
  (letfn [(pay [cc-number]
            (result-from-observable (.doPayment paymentService cc-number)))]
    (map pay (repeat n "TEST_NUMBER"))))

(def payments (do-test-payments 500))

; Pull Incanter dependency for data analysis and graphs
(pull 'incanter)

(require
 '[incanter.core :as i]
 '[incanter.charts :as c])

; pull statuses and sums into an Incanter dataset
(def payments-dataset (i/dataset [:status :sum] (map (juxt :status sum-from-result) payments)))

; plot a histogram with the sums
(i/view (c/histogram (i/sel payments-dataset :cols :sum) :nbins 20))
```

Loading this into the REPL will produce an Incanter chart (note that it often opens behind the currently open window, at least on OS X):

![incanter chart](/images/incanter.png)

The sum seems to follow a Gaussian distribution around 1,000 EUR. We now know something more about the thing we are investigating 
and can continue from here. This was of course a pretty trivial example — at [komoot](https://www.komoot.de), I have used a similar 
approach to investigate e.g. how many active users we have within different radiuses of city centers, only leveraging the application itself 
and not trawling through database dumps.

**Note: make sure that the application is started with `-Djava.awt.headless=false`, otherwise opening graphics windows with Incanter won't work! 
Headless mode seems to be the default at least using IntelliJ IDEA.**

#### Sample project

Have a look at a [sample project on Github](https://github.com/jstaffans/pay-me-spring-boot).

### Other uses of Clojure in Java applications

We have barely scratched the surface of the potential Clojure has for Java applications. Another area in which I have had 
success is for example property testing using [test.check](https://github.com/clojure/test.check). Testing in general
is a good avenue for bringing Clojure into Java projects. 

We didn't really talk about doing REPL-driven development in this article, only about exploring an existing Java
application using Clojure. You can definitely do REPL-driven development too, if you use a tool like [JRebel](http://zeroturnaround.com/software/jrebel/)
that is capable of advanced code hot-swapping. It's not as nice as namespace reloading in Clojure, but pretty close!


