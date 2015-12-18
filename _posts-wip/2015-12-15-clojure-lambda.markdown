---
layout: post
title:  "A look at running Clojure on AWS Lambda"
date:   2015-12-15
tags:   [clojure, lambda, aws]
---

Lambda is the name of the serverless function execution service that's been a part
of the Amazon Web Services universe since late 2014. The evolution of the Lamba 
service has been quite rapid during the last year, with one of the recent additions
being support for execution of Java code. As we know, where there's Java, there can
be Clojure, so during a recent project I decided to implement the backend parts using
Lambda. 

One well-known limitation of running Clojure on AWS Lambda is the JVM startup time. 
This is a serious problem for an application that requires fast responses. In this case,
you can still use ClojureScript running on Node.js which is another execution
environment that Lambda supports. If, like me, you can tolerate function executions 
that are sometimes very slow, Clojure works just fine. Lambda will also generally run
subsequent invocations of your function using the same, warmed-up JVMs, so it's usually 
only the first few executions that are very slow. 

Another limitation that you might hit is the 50 Mb size limit of the deployment artifact.
50 Mb might seem like a lot, but you can quickly hit that limit if you're not careful
with avoiding pulling in heavy dependencies in your project. [Amazonica] [1] is an 
example of a library that has a lot of transient dependencies. Liberal use
of `:exclusions` in the `:dependencies` section of your Leiningen project file helps:

```clojure
:dependencies [[amazonica "0.3.39" 
                :exclusions [com.amazonaws/aws-java-sdk-datapipeline
                             com.amazonaws/aws-java-sdk-devicefarm
                             ...]
``` 

