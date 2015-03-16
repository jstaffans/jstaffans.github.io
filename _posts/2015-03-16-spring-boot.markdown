---
layout: post
title:  "A vertical microservice with Spring Boot"
date:   2015-03-16
categories: Blog
---

At [komoot](https://www.komoot.de), we have lately been busy moving to a more microservice-oriented architecture. Many of our new features, such as the card feed
that shows up whenever our mobile apps are opened, have been implemented this way. We have also refactored and re-implemented some existing core components as 
dedicated services, one example being our routing engine.  

The microservices we have are so far strictly backend-related -  our web client is for example still a monolithic thing, albeit a much more modern one these days. 
Whenever we are implementing web-facing features we need to write code in two places - for the service containing the backend logic (probably in Java, Python or Clojure)  
and as part of the web UI, which is written in JavaScript using the [React.js library](http://facebook.github.io/react/).

The fact that you have to juggle two wildly different environments to do full-stack development can be a pain point. For features that require only simple UI elements
React.js is also perhaps overkill - a few server-rendered pages with a bit of jQuery can often be enough. I think having the ability to package the UI and the backend logic
together as a truly stand-alone service is an attractive concept which simplifies development greatly. This works especially well if the web-facing feature we want
to implement is already conceptually separate from the rest of the web UI - think an import dialog, a feedback form, a payment processor and so on.

The cost of doing such "vertical microservices" is of course that you do not have as much flexibility, neither in the front- nor in the backend. 
You may e.g. have to bend Maven into doing things it was never really meant to do, such as optimizing JavaScript files. You also need to find a way  
of sharing stylesheets, so that the user gets a unified visual experience no matter which service is serving up the HTML (without having to copy and paste anything, of course). 

### Spring Boot

In Javaland, I have long been a fan of containerless web services à la Dropwizard. After playing around with Spring Boot for a while, I would say that it does
an at least equally fine job. For a development team that's already used to Spring, there's also less of a learning curve and more familiar libraries that you
can just pick up and start using.

* Decided to do pay-me with Spring Boot
* Minimal sane frontend asset handling: webjars (Java devs' answer to Bower) requirejs (w/ optimizer!), SASS
* Server-rendered pages with Thymeleaf

