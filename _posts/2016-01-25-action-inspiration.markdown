---
layout: post
title:  "Action leads to inspiration"
date:   2016-01-25
tags:   [software development, getting started, advice, clojurebridge, clojure]
---

Last weekend, a [ClojureBridge] [1] event [took place in Berlin] [2]. I was one of the coaches and had
a wonderful time exploring Clojure with a group of new-comers to the language. In addition to coding,
some lightning talks touching upon different aspects of software development in general and Clojure 
in particular were held. One of the presentations was by me, in which I talked about some of the hurdles that novice 
programmers might face and gave some tips on how to overcome them. This is that talk in blog form.

### What does programming boil down to?

I'll take the premise that programming isn't something that is very hard to learn. It's not rocket 
science (unless your code is not running a website but an actual rocket!) or neurosurgery. Programmers
regularily deal with simple math, basic logic and the syntax of programming languages - how you call functions,
how data structures like vectors or hash maps are represented and so on. Once you start developing
more complex applications, there are of course more things to learn, such as how the Internet works, 
the mechanics of different libraries and how to organize code in larger projects. But you actually don't 
need that much to get started solving smaller problems.

### So what's the problem?

Despite the relatively simple nature of programming, it seems there is a general perception of software development
as being something that's hard to pick up. Many people who start to learn to code seem to become discouraged at an 
early stage and don't continue learning. Why is that? 

I think the issues beginners face when coding can generally be split into two categories: 

* Not knowing how to begin.
* Getting stuck along the way and becoming discouraged.

### Not knowing where to start

The step from thinking "I'm going to code a program that does X" to actually having running code can be 
intimidating if you are not familiar with programming. What editor should I be using? What framework can
help me solve my problem? How on earth should I code so that the solution is elegant, I heard that writing
spaghetti code is really bad?

There's a quote by [Arthur Ashe] [3], winner of three Grand Slams in tennis and recipient of the Presidential
Medal of Freedom, that goes:

> Start where you are.
> Use what you have.
> Do what you can.

My take on this is that you should begin your solution using what knowledge you already have. If you have only
ever coded a Java program with a "Hello World!" string printed from a `main()` method, a Java program with
a `main()` method is probably a good place to start. I'd interpret "do what you can" as trying to first solve
the parts of the problem that you understand, such as printing out text or getting user input from the command
prompt. 

Your favourite search engine is also your friend when taking the first steps to implement a new program.
You can start by typing in code from examples of similar applications, from tutorials or from screencasts. 

Iterating in small steps is key - a common beginner mistake is to try to solve the whole problem at
once. Always try to find the smallest thing you can solve first. If your problem involves working with
a collection of values, first implement a solution that handles a single value. Work your way from the
small to the large. There are some good books out there that can help you to learn this divide-and-conquer
approach, such as ["Programming Interviews Exposed"] [4]. 

Beware of [bikeshedding] [6] (a term that gets thrown around a lot among software developers 
and basically means wasting time on irrelevant details). At the end of the day, what your program *does* is what matters, 
not which framework you use, which editor or which programming language. 

Another favourite quote of mine, which I unfortunately don't know who to attribute to, is:

> Action leads to inspiration.

A common misconception is that a programmer should know exactly what they're doing before starting to code.
The solution should just appear before the mental eye in a puff of magical smoke and the actual coding is just the mechanical task
of writing the program in an editor. In reality, I think it is often the other way around - we start coding first,
without having much of an idea what we're doing. As we proceed, we see patterns, have new ideas on what 
we could go back and change in order to make it better and thoughts on what we should concentrate on next. 
In short, taking the action of starting to code leads to inspiration about how to solve the problem at hand -
not the other way around. 

A programmer will often find that the inspiration that comes from coding will take the solution in wholly new 
directions that could never have been anticipated when starting out. Often this can mean throwing out heaps
of existing code and starting afresh, which I see as a good thing - the first attempts at a solution are
usually just about gaining a deeper understanding of the problem. Don't become too attached to your code,
a lot of times throwing it out is just the right thing to do.

### Getting stuck

Getting stuck and not knowing how to proceed with a particular programming problem is maybe the biggest source of discouragment 
for new coders. It is unfortunately something one has to become used to, as this is something that happens very often, even to experienced
programmers.

The scary thing about getting stuck coding is that it can feel very personal and intimidating. In other occupations, one might
get stuck because a tool breaks, or a shipment of goods is late, or something else that is out of our control.
In software development, it's easy to think, "I should really know how to solve this problem!", or something along
those lines. Programming is after all primarily a mental excercise. 

Once down the rabbit hole of feeling inadequate, it's difficult to get out. The primitive and irrational part of the mind,
the limbic system that produces emotions like fear and anger and triggers fight-or-flight responses,
is in full control and does not give the slow, logical prefrontal cortex a chance to solve the problem. 
Daniel Kahneman's excellent book ["Thinking, Fast and Slow"] [5] explains this mental process, modeled as 
two systems that drive the way we think, in fascinating detail. 

To get un-stuck with a difficult problem, you need to give your brain time to process the information. 
You can for example go out for a walk, watch a movie or go dancing - anything! For me, solutions to 
problems I've faced during the day often come to me just before I go to sleep. Having a notebook by
the bed may be useful if that's the case for you, too.

Novice programmers can often feel intimidated by more experienced developers, as these seem to blaze
out code without much effort, taking every problem in stride. You should know, however, that a programmer like that
has probably seen similar problems hundreds of times already and has developed a repertoire of patterns around them.
Recognizing patterns and having a gut feeling about how to approach them, what data structures to use
and which algorithms are a good fit is something that comes with experience. Once you start building
your own repertoire, you'll start seeing patterns too and know instinctively what solutions to apply.

### Conclusion

Software development is fun and rewarding, but can also be frustrating, particularily for 
inexperienced programmers who don't know how to start approaching a problem. Getting stuck
can be intimidating but is something that I think new programmers need to become friendly with.
I hope this post can give some ideas on what actions to take to get that inspiration flowing!

Thanks again to the organizers of ClojureBridge Berlin!

[1]: http://www.clojurebridge.org/
[2]: http://clojurebridge-berlin.github.io/
[3]: https://en.wikipedia.org/wiki/Arthur_Ashe
[4]: http://www.goodreads.com/book/show/154154.Programming_Interviews_Exposed
[5]: http://www.goodreads.com/book/show/11468377-thinking-fast-and-slow
[6]: https://en.wikipedia.org/wiki/Law_of_triviality
