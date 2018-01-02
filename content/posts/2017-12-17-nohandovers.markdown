---
title:          "#NoHandovers"
date-published: 2017-12-17
tags:           [agile, team, flow, lean]
author:         Johannes Staffans
uuid:           81bfcabf-e523-4d67-9711-52bc8128208d

---

Time to take another look at the subject of a previous post, namely
[handovers within development teams][handover-post]. Making sure 
handovers go as smoothly as possible is all well and good, but what
if we could eliminate them completely?

One of the underlying principles of lean software development is the 
elimination of waste. Waste can be defined as anything we do that does 
not directly provide the customer with value. To me, it seems that the
activities needed for smooth handovers (like writing detailed pull
request descriptions) clearly constitute forms of waste. 

### Why are handovers necessary?

A handover becomes necessary when a developer, working alone, cannot 
finish the task he or she has undertaken in due time. 

There could be several reasons for why it's not possible to finish
the task: 

* unforeseen complexity, lack of design 
* too large work items (batch size)
* need to wait for e.g. code review

#### Batch size

The ability to split work into small, yet meaningful pieces is one of the 
best skills to have in software development. But going too granular during
planning sessions means these take a very long time — now you're not just 
wasting your own time, but everyone else's too! 

As with everything else in lean, we prefer to split work items at the last
responsible moment. So the first thing a software developer should
do before starting to implement a new work item is to think hard about 
possible ways of splitting the work up in smaller pieces. Ideally, you find 
ways to split the task up that make parallelisation possible. 

Maybe some of the work items even end up being thrown back into the backlog.
Work items getting bloated with unnecessary bells and whistles on their way
from the backlog to "In Progress" is not exactly unheard of.

In my experience, a reason for having large work items is the **reluctancy
to release half-baked features**. Working in very small batches inevitably 
means that the first iterations of a new feature will be very rough. 
Maybe validation is lacking or there is no error handling. 

The way to work around this is to get very friendly with the concept
of [feature toggles][]. This way, you can provide value to **some** of
your downstream customers, even if the end customer is oblivious to the
feature's existence. Your tester can have a go at breaking the feature,
for instance, or your UX designer can provide feedback.

Big bang deployments are the source of many software engineering horror
stories. Feature toggles help here as well since they are great for 
uncovering issues that might arise from deploying a feature to production. 
One example would be a feature whose performance relies on a cache being warm, 
something which can be hard to simulate accurately in a test environment. 
Once the feature goes live to end users, it has already been deployed behind 
a feature flag and monitored by the development team, which should have
made any performance issues visible early on.

#### #NoFeatureBranches?

The idea that work should be completed in small batches and deployed
frequently, using feature toggles, excludes us from using long-lived
feature branches. 

I can't say I'd miss them. Even with just a few developers, it's easy
to waste lots of time resolving merge conflicts, keeping track of 
which branch has which features on them and so on. Before long, you end
up basing feature branches on other feature branches that are themselves
still in progress, because you rely on some part of the functionality. 
You better hope that the code review of the base branch doesn't bring any 
big changes!

The longest-running branches I usually see are ones that include massive
refactorings of some key functionality. This seems to be something that
we would absolutely **not** want to keep in limbo any longer than necessary,
but building up to a big-bang merge takes time. An alternative for these
refactorings is to [branch by abstraction][] instead.

Code reviews are probably the only thing that I think feature branches
have going for them. Some possible solutions to consider:

* Do code review in small batches as well, *aka* pair programming.
* Review commits after they've been merged to master.
* Do reviews a committee fashion, focusing on the most critical bits of code.

### Conclusion

Lean software development is about eliminating waste. Making handovers go 
smoothly certainly makes work flow better, but as with anything, we should strive
to continue improving. A handover that never needs to happen is the most efficient
handover there is.

[handover-post]: http://jstaffans.github.io/posts/2017-09-08-the-handover.html
[feature toggles]: https://martinfowler.com/articles/feature-toggles.html
[branch by abstraction]: https://martinfowler.com/bliki/BranchByAbstraction.html
