---
layout: post
title:  "Mob programming"
date:   2017-02-15
tags:   [team, engineering, agile]
---

Last week, my team and I had the need to re-focus our priorities and put our joint efforts
into pushing a new project forward. Traditionally, we have had a bit of work segregation going
on with individuals and pairs working on smaller sub-projects, but the business now needed
something different. Since we were all going to work on the same thing, which was new to most
of us, we decided that doing a few days of [mob programming] [] would be a good way to kick things off.

Mob programming is a concept that none of us had any prior experience with -- I had heard about
it in passing, but that was about it. We are however used to working in pairs and have a pretty
well-functioning team without any derailers, which made things a lot easier. 

# Motivation

The primary motivation for having all engineers work tightly together was to **develop a shared
understanding** of the problem domain and to enable us to later split up into pairs and work on 
smaller tasks while still having a good general overview. "The mob", as we rather quickly started
referring to ourselves as, could **reach consensus** on important technical architecture and project 
setup issues early on.

Just as important as getting ahead with the engineering effort itself was what it did for
team morale. I think that a hallmark of an effective team is that its members work in an
interdependent fashion - in such a setting, each individual is aware of the other team members' 
areas of expertise and skillset and rely upon the support of colleagues to effectively complete 
their tasks¹. Both mob and pair programming foster collaboration and mutual learning.

# The setup

We booked a meeting room and re-arranged the tables and chairs to face in one direction and 
set up a projector to mirror a laptop screen onto the wall. We had a few different keyboards 
to cater to people's preferred layouts. A nice touch was providing hand desinfectant, 
since we would all be sharing a keyboard and mouse. 

We used a timer to ping us to rotate the "driver" every twenty minutes. 

# Day one: requirements

The project was new, but still based on some previous work that had been going on within the 
company as well as on extensive research that had been done previously by an individual on 
the team. We started things off by **setting an agenda for the day**-- present the research and 
previous art, determine some initial high-level requirements, identify technical risks and so on. 
We quickly decided that we would **not** do any programming the first day, so we didn't do any 
driver rotation either. One team member just acted as a secretary and took down notes in a shared 
document. 

Even though we didn't do any coding, having the whole team together with a clear agenda worked 
really well towards building up understanding for the problem domain. 

# Day two

In the morning, we set up a goal for the day -- to get a basic project setup running and deployed
on AWS. We chose a Python/Django-based backend because of familiarity with that stack, but included
more modern, Webpack-based frontend asset tooling than what usually comes with Django (we anticipate 
the need to move to a single-page application soon-ish, and Django's own static asset handling isn't 
very modern or flexible). 

As we started programming, it didn't take long for suggestions for key combinations, shortcuts
and terminal tricks started flying through the room. The driver usually didn't contribute much 
to the discussion, instead just acting as the conduit for typing things out. We frequently paused 
in order for the driver to explain something he had just done, though - there were frequent comments 
along the line of "I had no idea you could do that with PyCharm!". 

By the end of the day, we had reached our goal -- a service running on AWS, with everything up and
ready for real implementation work to commence the following day. 

# Day three

The agenda for the day was to tackle one of the areas of the system that needed especially careful 
architecting and carried considerable technical risk with it. We spent some time in the morning
brainstorming ideas on a whiteboard, identifying pros and cons with each approach and finally
settling on one of them. 

With everyone on the same page with regards to what the solution we were striving for was, 
it felt like we later breezed through the actual programming bit. With so many bright people
in the same room, it was guaranteed that at least someone was on the ball, knowing exactly
what the next step should be in order to achieve the solution we had sketched out earlier. 

At one point in the day, we had a lengthy discussion with the person who was going to play the role 
of product owner. Normally, that discussion would probably have taken place between only one or two 
developers and the PO, but now we were all together in the same room. Since we had developed a shared 
understanding of the problem domain from a technical viewpoint, we could all speak confidently about 
what the team's standpoint were on the issues that we discussed. It was almost as if we could read
each others' minds!

We achieved our goal and did a short retrospective about the experience doing mob programming.
Everyone was very happy with the experience. One team member suggested that the mob programming 
format should be the *default* from now on -- we split up only if we really have to! 

# Conclusion

A highly effective team is one that collaborates well. Mob programming is maybe the most collaborative
way of working for a software development team and has positive repercussions long after the mob has 
disbanded (if it ever does). I'd especially recommend it during the start of a new project and 
with a new team, so that everyone gets to know each others' strengths and ways of working better.

One caveat is that the rest of the organisation may feel left in the dark if the mob fails to 
communicate its process and its goals to others. In the worst case, the mob may become seen as 
a dangerous clique that is derailing the company and needs to be dissolved immediately. 
The mob needs to communicate effectively to others within the organisation to prevent this,
for example by posting daily status updates. 

[mob programming]: http://mobprogramming.org/
