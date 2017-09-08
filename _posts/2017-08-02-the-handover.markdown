---
layout: post
title:  "The handover"
date:   2017-09-08
tags:   [agile, teamwork, part-time teams]
---

The usual wisdom in software development is that a co-located team made up 
of full-time developers working together on a single project will perform best.
But how many teams are there like that? I'd like to instead consider a team
much more like the ones that I see around me - part-time team members, often
distributed, and working on multiple projects at once. What can a team like
that do to improve its performance?

To break things down a bit, let's look at the efforts of a software development
team from three, somewhat arbitrary angles:

* **Flow** - how steady is the progress of the team?
* **Knowledge** - does everyone on the team know what they need to know in order to do their jobs?
* **Process** - does the team strive to improve? Is its efforts supported by the rest of the organisation?

### Flow

Both for individual developers and for the team as a whole, getting into the "zone"
and achieving a steady flow is important. Unfortunately, there are all too many ways
for the flow to be disturbed. 

For the individual developer, not knowing what the next task is is for example a sure 
way of breaking the flow, as is being interrupted by unnecessary notifications or people 
stopping by with requests or questions.

On a project level, let's assume we're doing something like Scrum or Kanban and 
consider the flow of issues (let's call them stories) from the Product Backlog through 
whatever "In Progress" columns there may be to a final "Done" state. A steady flow here 
would mean that a story spends only a **short amount of time in every state**, rapidly moving 
from one state to the next as it is being worked on. 

What if a developer, who has Thursday and Friday off, picks up a story on Wednesday morning? 
Depending on the effort involved, there might be some hope of finishing it the same day.
Usually, though, squeezing design, implementation and code review into a single day rarely works. 
So probably it'll take until the following week, when the engineer is back at work,
before the story can be finished. During the days that the developer is away, 
the story remains in limbo. 

Recognising this as a problem, the developer would do best not to pick up any stories
on his or her own the day before leaving, but instead **pair up** with another engineer.
This is often the best way of ensuring smooth progress of a task.

If pairing is not an option, there needs to be a **handover**. On my team, the developer who 
started the work on any given story is expected to open a **work-in-progress pull request** and add
detailed information about the state of the implementation:

* High-level description
  * Free-form text is good
  * Bullet points are better
* References to further descriptions and acceptance criteria (e.g. a link to an issue in your issue tracker)
* Detailed todo list in the form of checkboxes, including items that have already been implemented
* How-to-test instructions, screenshots, animated GIFs displaying UI behavior
* Any technical debt incurred or items that need to be postponed

An example of a handover-friendly pull request would be:

```markdown
Add password reminder functionality
===================================

Widget where user can enter a registered e-mail address and press 'Reset password'.
The user will receive an e-mail. See [JIRA link], UI mockups [here].

## TODO

- [x] Backend (password reset API route, validation)
- [x] Frontend routes
- [x] Frontend component: enter e-mail
- [ ] Frontend component: landing page
- [ ] Error handling
- [ ] Mail template
- [ ] Mail sending
- [ ] Integration test

## How to test

Open browser to http://localhost:8000/reset-password, enter registered e-mail address. 

## Later

Metrics gathering (how often do users reset passwords?)

```

It's also important to keep the pull request focused on the story at hand - it is all too easy
to get caught up in unrelated, small fixes and refactorings when implementing a story. **If something
doesn't relate directly to the task at hand it should be in a separate pull request!** I'd suggest 
getting friendly with Git rebasing and cherry-picking, because often you'll realise you're working 
on something unrelated only when your halfway done with it.

### Knowledge

On a team where handovers are frequent, you cannot have knowledge silos, because that
will put a limit on who work can be handed over to. We've applied several 
strategies to avoid knowledge silos:

* For devops work, we rely heavily on the principle of **"infrastructure as code"**. All our cloud resources are for example documented in templates like CloudFormation or Terraform, and deployments are scripted.
* Cross-code-reviewing: request review from an engineer that is not too familiar with the area of the system your code touches and take the opportunity to explain it in more detail.
* Treat documentation as a first-class citizen.
* Lots of pairing and design discussions.

Besides knowledge about the system the team is building, team members also need to be on the same
page with regards to the development process and the interfaces to the rest of the organisation.
If for example a new staging system is set up, team members need to know its purpose and how to deploy it.
If an important customer demo is coming up, the team might need to know not to mess with a particular 
test system, and so forth. Our solution is to have a **dedicated `#announcements` Slack channel**,
which is only for important updates and where discussion on those topics is forbidden 
(we have other channels for that). A team member coming back from a few days off can
quickly scan this channel to get up to speed. 

### Process

A **working retrospective practice** is important for the team to be able to improve. In our case, 
the retrospective, which we do at two-week intervals, is one of the few times that all team members 
meet in the same room. We try very hard to keep the discussion focused on things that are actionable 
to us, and not surprisingly, we've come to see the handover as one of the most important things to 
"get right". If a handover goes badly for some reason, we can be sure that it will be discussed in 
the retrospective, and that some action will come out of it. 

One technique that we've found useful at the start of new projects is 
**[mob programming](http://jstaffans.github.io/2017/02/15/mob-programming.html)**. 
By building out the foundations of the project in a group setting, knowledge silos can be 
avoided from the get-go. It's also a great way of getting to know your team mates better - 
after all, you're going to rely on them heavily!

## Conclusion

Working in an interdependent fashion is part of what jells a team together. For a team 
consisting mostly of part-time engineers, interdependency often comes in the form of 
handovers. Instead of dumping a PR as a hot potato in your teammate's lap, spend a little
bit of time communicating what needs to done, what parts are already in place, what can
be done later and so on -- your teammates will thank you for it! Promote co-ownership
of the code, so that no part of it is known and understood only by a single team member.


