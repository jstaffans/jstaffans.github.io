---
title:  "A whiff of data science in Clojure"
date:   2017-11-14
tags:   [data, science, clojure, gorilla, repl]
uuid:   21baeb20-e717-47ea-9309-9f7c15e42618
author: Johannes Staffans
---

As part of learning more about applied statistics, I decided to trawl around
the [Helsinki Region Infoshare](http://www.hri.fi/en/) open data portal for
interesting data sets to analyse. I also took the opportunity to familiarise
myself with [Gorilla REPL](gorilla-repl.org), a  Clojure take on notebook-style
programming made popular for data science by IPython notebooks.

The data set I picked is a customer satisfaction survey for a youth services bureau.
Feedback is collected via a terminal with four buttons, ranging from a sad smiley to 
a happy one. The question asked is "how happy are with the services of the youth services 
bureau today"? The question **I** ask is whether customer happiness depends on the 
time of the day. 

Gorilla REPL comes with a handy online viewer (although it'd be even handier
if Github would support Gorilla REPL notebooks natively, as it does IPython and
Jupyter notebooks). You can check out the rest of the story and the results 
of my analysis [here](http://viewer.gorilla-repl.org/view.html?source=github&user=jstaffans&repo=happy-or-not&path=src/happy_or_not/repl.clj).
