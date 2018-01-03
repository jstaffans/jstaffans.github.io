---
title:          "Why I'm excited about AWS Fargate"
date-published: 2018-01-02
tags:           [aws, devops, containers, docker, ecs]
uuid:           32df7bf4-e980-4224-9954-988f487134c2
author:         Johannes Staffans
---

Most of the systems I've worked on over the past several years have been deployed
as Docker containers in the AWS cloud. Regardless of which approach to deploying
containers on AWS you take, there's always been a clunky step involved, namely
managing the underlying infrastructure in terms of EC2 instances, auto-scaling
groups and so forth. AWS Fargate promises to change that.

### The story thus far

I've tried a lot of approaches to deploying Docker containers on AWS, and 
learned a few lessons along the way.

**OpsWorks** — my team and I were attracted by niceties such as user and key management, 
useful monitoring, flexible scaling and a sane way of defining environment 
variables. Still, we ended up writing a lot of glue code 
in the form of Chef recipes to fetch Docker images, launch containers and so forth —
not to mention having to manually implement blue-green and rolling deployments. 
The biggest pain point was however the OpsWorks agent, which was the cause of 
unacceptably long boot times. The agent was even so busy at times that it caused 
auto-scaling events all on its own!

**Docker Machine** — single-instance environments, e.g. for test purposes, can be
easily created and torn down using [Docker Machine][] and its AWS driver. Sharing 
those environments quickly becomes a hassle, though, since the configuration and
access keys are usually only available on whatever system was used to create the
environment. It's impossible to scale this solution to production use cases.

**Docker Swarm** — it seems elegant to be able to use the same Docker client that
is so familiar from local development to create and deploy multi-instance production 
environments as well. In practice, though, maintaining a Docker Swarm cluster on
your own is a lot of overhead for a small team. Probably the same thing can be said
of maintaining a Kubernetes cluster, which is something I haven't tried yet.

**Elastic Beanstalk** — multi-container deployments on Elastic Beanstalk is my
current go-to way of launching new systems that utilise Docker containers. A 
multi-container setup essentially turns your Elastic Beanstalk environment 
into a managed ECS cluster. Elastic Beanstalk provides a lot of useful features like 
monitoring, simple auto-scaling, and rolling deployments out of the box. There are a 
few drawbacks as well, for example that each instance needs to run the same container 
configuration, which can lead to sub-optimal resource usage. A long-standing
gripe of mine is also the fact that environment variables can only be stored in
plain text, which is obviosly a bad idea from a security perspective. Finally, debugging 
Elastic Beanstalk deployments is usually not a trivial exercise, since the whole platform 
seems to consist of a rickety patchwork of shell scripts. 

One noteable exception to the list above is ECS. The overhead of cluster maintenance never 
seemed to justify the added flexibility over the simpler approach of deploying Docker containers
to Elastic Beanstalk.

### AWS Fargate: the future?

[Fargate][] is the latest container orchestration offering from AWS. From what I've gathered
from the launch announcements, Fargate promises to relieve us developers from the burden of 
managing the underlying ECS cluster ourselves. If Fargate can give me fine-grained control over 
containers, deployments fas easy as multi-container Elastic Beanstalk deployments, and modern 
features around things like auto-scaling, configuration through environment variables and so forth, 
I'm hopeful it'll meet my container deployment needs for the foreseeable future.


[Docker Machine]: https://docs.docker.com/machine/
[Fargate]: https://aws.amazon.com/de/blogs/aws/aws-fargate/

