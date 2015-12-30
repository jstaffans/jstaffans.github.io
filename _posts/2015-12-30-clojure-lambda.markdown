---
layout: post
title:  "A look at running Clojure on AWS Lambda"
date:   2015-12-30
tags:   [clojure, lambda, aws]
---

Lambda is the name of the serverless function execution service that has been part
of the Amazon Web Services ecosystem since late 2014. The evolution of the Lamba 
service has been quite rapid during the last year, with one of the recent additions
being support for execution of Java code. And as we know, where there's Java, there can
be Clojure, so during a recent project I decided to implement some of the backend parts using
the AWS Lambda service.

### Advantages 

The obvious advantage of using Lambda is that you do not have to worry about 
server infrastructure, instead you are just deploying a function and letting
AWS worry about scaling and so on. I think that having the function be the
unit of deployment is a natural step to take when doing functional programming.
Clojure of course works splendidly in this setting.

### Limitations

One well-known limitation of running Clojure on AWS Lambda is the JVM startup time. 
This is a serious problem for an application that requires fast responses. In this case,
you can still use ClojureScript running on Node.js, which is another execution
environment that Lambda supports. If, like me, you can tolerate function executions 
that are sometimes very slow, Clojure works just fine. Lambda will also generally run
subsequent invocations of your function using the same, warmed-up JVMs, so it's usually 
only the first few executions that are very slow. 

Another limitation that one might hit is the 50 Mb size limit of the deployment artifact.
50 Mb might seem like a lot, but you can quickly reach that limit if you're not careful
about avoiding pulling in heavy dependencies in your project. [Amazonica] [1] is an 
example of a library that has a lot of transient dependencies. Liberal use
of `:exclusions` in the `:dependencies` section of your Leiningen project file helps:

```clojure
:dependencies [[amazonica "0.3.39" 
                :exclusions [com.amazonaws/aws-java-sdk-datapipeline
                             com.amazonaws/aws-java-sdk-devicefarm
                             ...]
``` 

One serious limitation that isn't immediately obvious is that Lambda functions
have no way to communicate with private RDS databases. This is because permissions 
are granted to a Lambda function based on 
its (IAM) execution role whereas RDS access is security group- and therefore
IP-address-based. The IP address of a Lambda executor is not known to the user,
so you would have to open up access to large chunks (the whole published AWS 
IP range, essentially) to grant access to a Lambda function. Addressing this limitation seems to at least
be [on the AWS Lambda roadmap] [2], but no dates have been given yet. 

### Practical issues

**Deployment** to AWS Lambda requires some command-line magic - I created a Jenkins job
that runs Leiningen, tests the code and deploys a new version to Lambda. There are
some things that need configuring, mainly how much memory to reserve for the function
(this correlates directly with how much you pay per function invocation as well). 
I found 384 Mb to be the lowest possible amount for my relatively simple data-crunching
function. Lower than that and the function would simply hang and time out.

Since Lambda is somewhat of a black box execution environment, **logging** is crucial.
Logs can be found in CloudWatch, but you have to remember to give the function
an execution IAM role that is authorized to create log streams - the [docs] [3] have 
more to say on that.

Your application will probably also need some **configuration**, maybe in the form
of an API key or threadpool settings (yes, threadpools are fine to use with Lambda). 
Lambda unfortunately lacks support for environment variables similar to what other AWS 
deployment tools like Elastic Beanstalk and OpsWorks have, so you have to get a bit 
more creative.

Usually, some setup that makes local development easy and while avoiding placing 
sensitive values in the source-code repository is a good way to go and I ended
up using the [immuconf] [4] library in combination with an S3 bucket. The blueprint
for the configuration is a checked-in `resources/config.edn` file, which can be 
overridden by both a file in an S3 bucket and a file that is available only locally:

``` 
(ns lambda-example.config
  (:require [clojure.java.io :as io]
            [amazonica.aws.s3 :refer [get-object]]
            [immuconf.config :as conf]
            [clojure.tools.logging :as log])
  (:refer-clojure :exclude [get]))

(defn- base-config
  []
  (io/resource "config.edn"))

(defn- s3-config
  []
  (try
    (:input-stream
      (get-object
        :bucket-name "my-config-bucket"
        :key "production/config.edn"))
    (catch Exception _
      (log/warn "S3 config not available!"))))

(defn- local-config
  []
  (when (.exists (io/as-file "local.edn")) "local.edn"))

(def config
  (apply 
    conf/load 
    (filter 
      (partial not= nil) 
      [(base-config) (s3-config) (local-config)])))

``` 

### Execution 

Lambda functions are versatile in that they can be triggered by almost any type of event 
in the AWS universe. I opted for subscribing to an SNS topic, but you can also trigger 
functions based on for example changes in an S3 bucket or updates to a DynamoDB table.

The [lambada] [5] library provides the necessary plumbing for actually executing your 
function in response to an AWS event. 

### Conclusion 

AWS Lambda is a fun, versatile and cheap way of taking the functional programming 
paradigm one step further by having the function be the unit of deployment. Although
there are some kinks such as the lack of environment variable support and restrictions
with regards to RDS access, most problems can be worked around. The JVM startup time
is a blocker for applications with real-time needs but not a problem for data-crunching
background tasks. 

[1]: https://github.com/mcohen01/amazonica
[2]: https://forums.aws.amazon.com/thread.jspa?threadID=166946&start=25&tstart=0
[3]: http://docs.aws.amazon.com/lambda/latest/dg/intro-permission-model.html#lambda-intro-execution-role
[4]: https://github.com/levand/immuconf
[5]: https://github.com/uswitch/lambada
