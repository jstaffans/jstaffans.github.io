---
layout: post
title:  "Clojure in Java projects"
date:   2014-12-14
categories: Clojure Java
---

Clojure can be a real boon for large, existing Java projects. 
Here are some tips for making the integration as smooth as possible.

The first step is usually convincing your colleagues that it is a good idea.
Some of the benefits you can bring up:

* The language is much more expressive than Java.
* The interactive, REPL-driven coding cycle speeds up development.
* Any old Java libraries can still be used with help from Clojure's excellent interop facilities.
* The tooling works. In particular, the Clojure plugins for the standard Java IDE's (IntelliJ and Eclipse) have come a long way.

It can be a good idea to do some kind of live-coding session where you
demonstate iterating over a piece of Clojure code and how reloading it instantly
has an effect on the Java system. For this to work, you need REPL integration -
more on that below.

Several different approaches can be taken when introducing Clojure. From less to more integrated solutions:

1. Develop stand-alone microservices in Clojure.
2. Build separate Clojure projects whose JAR artifacts are used in the main Java project.
3. Add Clojure code directly to the main Java project.

### Clojure microservices

This might not even fall under the heading of "integrating Clojure into Java projects", but I nevertheless
wanted to bring it up because Clojure lends itself so well to the development of microservices. If you are OK with the 
added challenges that a distributed backend brings, developing stand-alone services may be a good option. 
You may not even need to start with a distributed system but instead take an approach as suggested in Chris Stucchio's blog post 
called ["Microservices for the Grumpy Neckbeard"][microservices-neckbeard]. If you do go distributed, have a look at
[how they do Clojure microservices at SoundCloud][microservices-soundcloud].


### The JAR way

### Baking Clojure into the main project

{% highlight clojure %}
(defn foo
  [x]
  (+ 1 x))
{% endhighlight %}

[microservices-neckbeard]: https://www.chrisstucchio.com/blog/2014/microservices_for_the_grumpy_neckbeard.html
[microservices-soundcloud]: http://blog.josephwilk.net/clojure/building-clojure-services-at-scale.html
