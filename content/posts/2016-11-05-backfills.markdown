---
layout: post
title:  "Event stream processing: backfills"
date-published:   2016-11-05
tags:   [streams, stream processing, kinesis, events]
author: Johannes Staffans
uuid:   74f22045-a456-43fd-b638-734c3dd30d6d
---

During the last months I have been involved in developing an event processing pipeline based on Amazon Kinesis. This post is about different strategies for replaying or **backfilling** events, to make up for lost events or a buggy stream worker implementation.

### What are backfills?

The architecture of a Kinesis-based event pipeline is pretty simple. Events are sent to Kinesis from various clients and later processed by several worker components performing various tasks — in my case, backups, machine learning and persisting events to Redshift. A nice thing about Kinesis is that events are available for 24 hours after being sent, which means that when a worker crashes, it can be re-started and no events will be lost, as long as the downtime wasn't longer than the Kinesis 24 hour horizon. The worker will simply pick up the stream where it left off — a nice feature of the Amazon Kinesis Client Library!

But of course, due to various reasons, it might happen that a worker is offline for more than 24 hours. Or you may notice a bug in the implementation of a worker which means that the results it has painstakingly refined from the event stream are wrong and need to be re-calculated. 

If we take the case of a buggy worker implementation, after the problem has been fixed, the worker now needs to be let loose on all old events and perform a re-calculation. The source of the old events are your event backups — Amazon provides a nice way to dump events from Kinesis to S3 via Kinesis Firehose, which is what we are using for backing up all Kinesis events, but there are other backup possibilities as well.

### Batch or online?

Conceptually, there are two basic strategies of providing input to the worker that will perform the re-calculation: either as one big batch of events (maybe reading straight from the S3 backups),  or by backfilling the events through the event pipeline itself, which I'll call **online** backfilling.

I think the batch update approach has a lot going for it. It is simpler and involves less components. One drawback is however that the worker component needs to be set up with two distinct code paths: one on  which events are read from Kinesis and one where events are read from a file dump, e.g. in S3. I tend to be in favor of having less code paths in my code. If you instead set up your worker to be prepared for receiving backfilled events online, you can use the same infrastructure and code paths for the backfills as during normal operation.

### Distinguishing backfilled events

Going with the online approach, we need a way of isolating the backfilled events, so that they aren't mistakenly re-read by other components in our event processing architecture. We only want to target a single worker component, namely the one that had a bug, for the backfill. 

We could imagine tagging indivdual events with some kind of metadata to let other components know that they should ignore it, because it is a backfill event that should only be processed by one particular worker. But it seems redundant to implement this filtering in each and every component. Why should every component need to care about a backfill being performed? 

I think the simplest approach is to set up a completely separate Kinesis stream just for the purpose of the backfill, which amounts to a couple of clicks in the AWS console. Ideally, the name of the Kinesis stream that the worker component reads events from is configured as an environment variable or similar — if that's the case, the worker can simply be re-started with an updated piece of configuration that causes it to read events from the backfill stream instead. When the backfill is done, just switch back over to the main stream. No events are lost, as long as you didn't spend more than 24 hours backfilling and not listening to the main stream!

### Other considerations

In the online backfilling scenario, there needs to be something that writes the events to the backfill Kinesis stream. I ended up writing around 20 lines of Python that gets the event backup archives from S3 for a given date range and sends them off to Kinesis in batches of a few hundred events each. Care needs to be taken not to exceed Kinesis write limits!  

### Conclusion 

It's important to have a strategy in mind for backfilling events in a stream processing pipeline. The day will inevitabely come when a bug or extended downtime makes it necessary to replay events. Making it easy to switch components over from the main stream to a separate backfill stream enables replays with the least amount of extra code. 
