---
layout: post
title:  "Every-day machine learning"
date-published:   2017-02-15
tags:   [machine learning, data]
---

The trend towards heavier use of machine learning seems to be as strong as ever.
We often see references to well-known, successful implementations of machine
learning, like AlphaGo or self-driving cars. But most of us work on projects
far more mundane than those. Can we still employ machine learning techniques?

The idea of this post is to brainstorm some potential ways of bringing
machine learning into a convential web application project. I'll try to 
consider a few different categories of use cases:

* Dealing with data
* Reacting to events
* Making recommendations

## Dealing with data

### Making up for missing labels

Imagine your application needs to work with user-generated data, perhaps in the
form of Optical Character Recognition (OCR) extracts. Say for example a list of 
words representing the names of authors along with more words with other information
about the author:

```
[
  {"text": "Dan", "format": "bold", "row": 12, "position": 0, ...},
  {"text": "Brown,", "format": "bold", "row": 12, "position": 5, ...},
  {"text": "born", "position": 12, ...},
  {"text": "August", "position": 17, ...},
  ...
  {"text": "Stephen", "format": "bold", "row": 29, "position": 0, ...},
  {"text": "King,", "row": 29, "position": 9, ...},
  {"text": "born", "row": 29, "position": 17, ...},
  {"text": "September", "row" 29, "position": 22, ...},
  ...
]

```

From our domain knowledge, we know that the author name appears in bold, so to get the 
author names that appear in the data, it should be enough to filter out the bold words - right?

It turns out that user-generated data is messy. In the piece of data above, the OCR process
has missed that one of the words was bolded, for example. 

We can instead look at the boldness of the word as only one thing that identifies the author name.
Others are, for example:

* Initial letter capitalized
* usually appears in beginning of row
* often occurs before the word "born"
* ...

We can then hand-pick a subset of data where we label the author names manually and train 
a _classification algorithm_ like Naive Bayes or Support Vector Machine which tells us with pretty 
good confidence which words represent the names of authors.

**Extrapolation:** any text classification problem that needs to work with messy data.


### Finding the best user-generated content



## Reacting to events

### Example 1

### Example 2

## Making recommendations

Making recommendations to the user is probably the most well-known use case
for machine learning algorithms. The canonical example is to suggest products
to the user given what similar users have purchased. I'll try to think of a few
other kinds of recommendations that we could use outside of the context of an
online shop.

### Example 1

* Recommending next level in game given user's results / proficiency / progress

### Example 2


## Final thoughts





