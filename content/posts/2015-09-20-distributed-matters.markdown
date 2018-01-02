---
title:          "Distributed Matters 2015"
date-published: 2015-09-20
tags:           [devops, distributed, conference, aws, redis, disque]
author:         Johannes Staffans
uuid:           4ab445b8-7191-44f5-a67a-7233e51a3560
---

Yesterday, I had the pleasure of attending the Distributed Matters 2015 conference here in Berlin.
The conference had re-branded itself for this year, having previously gone under the name of NoSQL 
Matters. I think broadening the scope to distributed systems was a good move — NoSQL was still 
very much present in the agenda, but my favorite topics were definitely those dealing with 
distributed systems and communication on a broader scale. All in all, the conference was 
very library-, technique- and tool-oriented, there were not many talks of the more philosophical kind.

The venue was the always awesome Kulturbrauerei in the north of Berlin, which works very well
for mid-sized conferences like Distributed Matters. I appreciated the fact that there were
lots of standing tables around that were perfect spots for meeting people and networking.
I always try to be ultra-social during conferences and talk to as many people as possible.

## The talks

I took copious [notes][1] during the talks that I attended, which were:

### Keynote: Jepsen V (Kyle Kingsbury, [@aphyr](https://twitter.com/aphyr))

Kyle Kingsbury of [Jepsen][2] fame is certainly an exciting choice of keynote speaker 
for a conference dealing with distributed systems. His talk was greatly anticipated
by the attendees I got to talk to. While I appreciated the hand-drawn slides a lot,
I didn't really get anything new out of the keynote that Kyle hasn't already written
about on his blog, so it was a bit disappointing for me. There was some interesting
stuff towards the end on techniques for testing your architecture (e.g. simulate
a network partition with `iptables`), but unfortunately those parts were quickly skimmed
over due to time constraints.

### Lisp in the Machine (Joe Nash, [@jna_sh](https://twitter.com/jna_sh))

Talking very fast, Joe Nash of Braintree told a story about how his company, 
whose domain is payment processing, stores transaction data. Events initially
land in Postgres, the "live" database. Amazon Redshift is used for reporting
and archiving so the events need to be transferred there as well. Previously,
this was a big batch job; now, business users enjoy near real-time reporting
thanks to a Kafka-based pipeline — win!

Braintree uses Clojure to handle the data pipeline. This makes a lot of sense since
they are using lots of JVM technology already. Another improvement has been
to incorporate Elasticsearch for transaction searches. Different kinds of data
munging needs to happen for Redshift and Elasticsearch and Clojure's data structures
are very helpful here.

### A tale of queues — from ActiveMQ over Hazelcast to Disque (Philipp Krenn, [@xeraa](https://twitter.com/xeraa))

The domain of the company Philipp works at, [ecosio][3], is business process management.
Customers basically pass messages through ecosio's systems, where a typical message
would be placing an order for some product. There are lots of legacy systems that need connecting, for which
Apache Camel is used. Internally, messages first land in MySQL and are then forwarded 
using a distributed queue. It might be a slightly over-engineered solution, given the 
relatively low number of messages, but pretty interesting in its execution.

ecosio has evaluated lots of queuing solutions (see [notes][4]), finally landing on 
[Disque][5]. Probably they are the first company to use this alpha software in 
production, which makes them either brave or stupid! More details on Disque later.

### Microservices at SoundCloud (Phil Calçado, [@pcalcado](https://twitter.com/pcalcado))

Phil has recently written about [how SoundCloud ended up with microservices][6] 
and his talk at Distributed Matters went into further detail about what the 
service architecture SoundCloud has ended up with looks like. He noted that 
following the [Twelve-Factor App pattern][7] from the beginning was a big boon. 
Also, having standardised reporting and administration interfaces for all
services helps a lot with maintainability. This was the first time I heard 
about [twitter-server][8], which seems like a pretty good anti-bike-shedding
weapon when it comes to discussing how to best set up services.

SoundCloud do 20 % time projects, one of which has been [Prometheus][9], 
a monitoring and alerting toolkit that seems pretty full-featured
and is definitely something I will be trying out!

### SimCity BuildIt – Building Highly Scalable and Cost Efficient Server Architecture (Matti Palosuo)

Matti leads the team building the Java/Spring Boot-based backend for the hugely popular mobile game, 
SimCity BuildIt. Some 8 million transactions are handled on any given day. The data model is relatively 
simple — a player ID and the player's associated information. Redis is used as the main database, 
holding data on the "live" set of players (anyone who has logged in to the game during the last few months). 
The team has built its own Java tool called Anteater which handles distributed configuration, sharding 
and transactions in Redis. Anteater doesn't seem to be open-source — bummer!

MongoDB is used as the archive and backup database. If a player logs in after a long period of inactivity,
the player's data is first fetched from MongoDB into Redis. 

Everything is highly available, distributed over three AWS availability zones behind an ELB. 
Optimizations include patching Redis for better performance and cross-shard transactions
and using Protobuf for data transfers. 

It was interesting to see a pretty traditional stack wired up for high performance and 
availability — not much distributed stuff going on here, but the application is still
a highly successful one. The team also considered using Redis as the main database a big win.

### Netflix OSS and Spring Cloud (Arnaud Cogoluègnes, [@acogoluegnes](https://twitter.com/acogoluegnes))

In recent years, big JVM players like Netflix have pumped out a lot of cool 
libraries and tools for microservice development, such as Eureka (a service registry), 
Hystrix (circuit breakers) and Ribbon (client-side load balancer). [Spring Cloud][10] is an attempt 
to make these tools easily integratable in Spring-based applications. As someone who works
with Spring quite a lot, I found it fascinating that you can get e.g. Hystrix working with a 
single Spring method annotation. Even dashboards can be enabled using annotations.

Judging from the amount of Github stars, the Netflix OSS integrations seem to be the 
most popular parts of Spring Cloud, but there are a lot of other integrations as well.
I think you can get a pretty sweet microservice setup with Spring Boot and these
integrations, but the question you need to ask yourself is: do you want to be doing
Java/Spring at all?

### Microservices – stress-free and without increased heart-attack risk (Uwe Friedrichsen, [@ufried](https://twitter.com/ufried))

* [Slides](http://de.slideshare.net/ufried/microservices-stressfree-and-without-increased-heart-attack-risk)

By now I think everyone knows that "microservices are no free lunch", which I thought would 
be the gist of this talk as well. Thankfully the speaker went into a bit more detail. 
Among other things, I found the tie-in to Domain-Driven Design interesting — well-designed
microservices should approximate [Bounded Contexts][11]. Other things that the speaker
recommended us to consider on the path to microservice englightenment was forgetting
about layers and re-thinking DRY in favour of reduced deployment dependencies.

On the service interface side, [Postel's Law][12] was brought up as well as the need to validate
responses from other services, even though we should be able to trust them. Datastores are
another issue — a single, monolithic data store is probably a bad idea as are distributed
transactions (if you find yourself doing that, go back to the drawing board!). 

Production readiness is probably the biggest challenge. Besides deployment, configuration,
monitoring and so on, we also have to think differently about error handling — throwing
an exception no longer fits the bill, we need a separate error handling channel.

If nothing else, focus on production readiness and a functional design!

### Disque (Salvatore Sanfilippo, [@antirez](https://twitter.com/antirez))

The final talk of the day was by Salvatore Sanfilippo, the inventor of Redis, who 
apparently disagreed with Redis being used a distributed queue and decided to whip
up a solution for this particular use case. [Disque][13]
(pronounced *dis-queue*) is based on Redis and shares a lot with its parent project, 
among other things optional persistency, its API, its license and so on.

The main uses cases for Disque would be asynchronous job execution or as a
microservice event bus. Another interesting usage would be as a distributed
scheduler. Disque has configurable delivery semantics, by default *at-least-once* 
but trying hard to be *exactly-once*. Sweet stuff like delays and TTL is also supported.
The main design sacrifice is that only best-effort ordering is offered — for
strict ordering, look elsewhere.

Salvatore went into a lot of detail about the implementation, see the [notes][14]
for all the nitty-gritty. Suffice to say that people seemed pretty excited
by this simple yet powerful queuing solution. The first stable release should
come around in a few months, but in the meantime, some people are already
using Disque in production.

## Conclusion

The conference talks and the discussions I had with the speakers and other attendees 
gave me a lot of things to think about with regards to distributed systems as well
as lots of new vocabulary to talk about them. Thanks to the organizers for an 
excellent conference and see you next year!


[1]: https://gist.github.com/jstaffans/ed44a4099a79127280e5
[2]: https://aphyr.com/tags/jepsen
[3]: https://ecosio.com/en/
[4]: https://gist.github.com/jstaffans/ed44a4099a79127280e5#a-tale-of-queues--from-activemq-over-hazelcast-to-disque-philipp-krenn
[5]: https://github.com/antirez/disque
[6]: http://philcalcado.com/2015/09/08/how_we_ended_up_with_microservices.html
[7]: http://12factor.net/
[8]: https://github.com/twitter/twitter-server
[9]: http://prometheus.io/
[10]: http://projects.spring.io/spring-cloud/
[11]: http://martinfowler.com/bliki/BoundedContext.html
[12]: https://en.wikipedia.org/wiki/Robustness_principle
[13]: https://github.com/antirez/disque
[14]: https://gist.github.com/jstaffans/ed44a4099a79127280e5#disque
