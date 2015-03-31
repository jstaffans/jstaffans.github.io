---
layout: post
title:  "A vertical microservice with Spring Boot"
date:   2015-03-16
categories: Blog
---

At [komoot](https://www.komoot.de), we have lately been busy moving to a more microservice-oriented architecture. Many of our new features, such as the card feed
that shows up whenever our mobile apps are opened, have been implemented in this fashion. We have also refactored and re-implemented some existing core components as 
dedicated services, one example being our routing engine.  

The microservices we have are so far strictly backend-related - our web client is for example still a monolithic thing, albeit a much more modern one these days. 
Whenever we are implementing web-facing features we need to write code in two places - for the service containing the backend logic (probably in Java, Python or Clojure)
and as part of the web UI, which is written in JavaScript using the [React.js library](http://facebook.github.io/react/).

The fact that you have to juggle two wildly different environments to do full-stack development can be a pain point. For features that require only simple UI elements
React.js is also perhaps overkill - a few server-rendered pages or an iframe with a bit of jQuery can often be enough. I think having the ability to package the UI and 
the backend logic together as a truly stand-alone service is an attractive concept. This works especially well if the web-facing feature we want
to implement is already conceptually separate from the rest of the web UI - think an import dialog, a feedback form, a payment processor and so on.

The cost of doing such "vertical microservices" is of course that you do not have as much flexibility, neither in the front- nor in the backend. 
You may e.g. have to bend Maven into doing things it was never really meant to do, such as optimizing JavaScript files. You also need to find a way  
of sharing stylesheets, so that the user gets a unified visual experience no matter which service is serving up the HTML (without having to copy and paste anything, of course). 

### Spring Boot

In Java land, I have long been a fan of containerless web services à la Dropwizard. After playing around with Spring Boot for a while, I would say that it does
an at least equally fine job. For a development team that's already used to Spring, there's also less of a learning curve and more familiar libraries that you
can just pick up and start using, Spring Security being a good example.

### The application

A while ago I [developed a toy payment service while checking out Duct, a recent addition to the Clojure web application development ecosystem](https://jstaffans.github.io/blog/2015/02/08/duct.html). 
That application also morphed into a kind of "vertical microservice" as described above. I really enjoyed the seamless integration of Clojure and ClojureScript 
when developing that application, so I decided to see what it would be like to develop roughly the same application using Java and Spring Boot. 

#### Backend 

When developing the Clojure-based payment application, I made good use of core.async for orchestration between different parts of the backend, such as 
the reporting subsystem and the payment processing component - the separation of behavior and data and event-based approach that core.async brings 
is a big win in my opinion. When looking for something similar for use in a Java project, I finally landed by [RxJava](https://github.com/ReactiveX/RxJava). 
The concept is not quite the same as core.async, but it does support a more functional, event-driven style of programming. It's also pretty
easy to port some core.async goodies, such as implementing timeouts by waiting for the first of several Observables to emit a value (the core.async equivalent 
would be to wait for the first of several channels to close, where one channel is a timeout channel). 

Some people may say that RxJava is best (or only) suited for UI programming, but I do think it has its place in the backend too. 
See [this post on the Netflix blog](http://techblog.netflix.com/2013/02/rxjava-netflix-api.html) for a good example. The library does however come with 
a lot more concepts than a simple little thing like core.async, and I'm still only learning the basics. 

#### Frontend

The application relies on server-side rendering using Thymeleaf for its HTML, but there are still some frontend assets to take care of, namely stylesheets
and Javascript files. When developing the frontend parts of a stand-alone microservice, there are basically two choices: 

1. Implement a separate, node.js-based build chain with for example Grunt for things like SASS compilation, JS minification and so forth. 
Then just serve the content as static files in your application and have Maven run an external task during the production build. 
This will be the favorite for most people with JS experience, I think.

2. Bend your existing Java build system to manage the frontend parts as well. Great for Java devs who don't want to learn about node.js! 
But good luck trying to get a web designer to install Maven if you need help with those stylesheets ...

Since I am developing this project on my own and use Maven for Java development, I decided to go with the latter option. In order to not complicate things 
too much, I opted for a pretty basic but decently scalable setup:

* Dependencies to vendor libraries such as jQuery handled by [Webjars](http://www.webjars.org).
* RequireJS for modularisation, with a [Maven plugin](https://github.com/bringking/requirejs-maven-plugin) taking care of minification and concatenation of JavaScript files.
* SASS stylesheets with a [filter](https://github.com/darrinholst/sass-java) for development and, again, a [Maven plugin](https://github.com/darrinholst/sass-java/blob/master/sass-java-maven/README.md) for building production CSS files.

I would still like to implement cache busting for the frontend assets. 

The hardest part of the entire project was easily getting everything frontend-related to behave as expected, 
both when running from the IDE and using a production JAR! I would not recommend going with the Maven-based approach 
for anything really complex. Still, I am pretty satisfied with the result and I hope the project can serve as an 
example of how to wire things up for a simple application. 

### Conclusion

There are many avenues I didn't explore when creating this application - for the frontend parts, I could for example have 
relied on an asset pipeline like [wro4j](https://github.com/wro4j/wro4j) or used a node.js build chain. I wanted 
to see how far you could get with simple building blocks such as small, focused Maven plugins, and it turns out it
is definitely doable. 

Java 8, Spring Boot and RxJava show that there is still some fun to be had in Java land - at least as far as the backend goes. 
But if you decide to go down the path of vertical microservices on the JVM, I think Clojure and ClojureScript is an unbeatable 
combination at the moment. 

[Check out the application on Github](https://github.com/jstaffans/pay-me-spring-boot)!

