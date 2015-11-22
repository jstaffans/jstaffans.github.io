---
layout: post
title:  "Fun with Specter"
date:   2015-11-19
tags:   [clojure, data structures, specter]
---

[Specter] [1] is a new library by Nathan Marz that makes it easier to deal with
nested data structures, such as lists of maps of maps. This post explores some
less-documented parts of the library.

For this post, we'll be dealing with a simple movie dataset:

```clojure
[{:name "Lethal Weapon", :director "Paul Verhoeven", :rating 7.6}
 {:name "RoboCop", :director "George P. Cosmatos", :rating 7.5}
 {:name "Lethal Weapon 3", :director "Ted Kotcheff", :rating 6.6}
 {:name "Rambo III", :director "John McTiernan", :rating 5.4}
 {:name "The Terminator", :director "Peter MacDonald", :rating 8.1}
 ... ]
```

Transforming the sequence is simple enough:

```clojure
(require '[com.rpl.specter :as s])
=> nil
(s/transform [s/ALL :name] clojure.string/upper-case movies)
=> [{:name "LETHAL WEAPON", :director "Paul Verhoeven", :rating 7.6}, ... ]
``` 

In general, transformation is Specter's forte and is covered very well
by the [documentation] [1]. I was however interested in exploiting 
Specter for doing analysis and aggregation of data stored in a sequence
of nested maps. It is possible to do so with standard Clojure functions,
but I like the declarativeness of Specter and wanted to give it a shot.

Let's for example find all movies by James Cameron with a rating higher than 8.0:

```clojure
(s/select 
  [s/ALL 
   (s/cond-path 
     [:director #(= "James Cameron" %)] 
     [:rating #(> % 8.0)])] 
  movies)
=> [8.4 8.5 8.1 8.3 8.5 8.6]

```

**(Note: Nathan Marz let me know via Twitter that there's a  better
to do this, more on that at the end of the post!)**


So we get the ratings and they are all greater than 8.0 - but we have 
lost the original maps. How do we get those? It turns out that you can reference 
the `VAL` symbol basically anywhere within the selector path. This resolves to
whatever value is selected by Specter at this level of nesting. If we example put 
`VAL` at the end, we duplicate the rating:

```clojure
(s/select 
  [s/ALL 
   (s/cond-path 
     [:director #(= "James Cameron" %)] 
     [:rating #(> % 8.0) s/VAL])] 
  movies)
=> [[8.4 8.4] [8.5 8.5] [8.1 8.1] [8.3 8.3] [8.5 8.5] [8.6 8.6]]
```

When working with sequences of maps, it's usually the case that we
want to get the whole map back, so we should put `VAL` at the beginning
of the selector path:

```clojure
(s/select 
  [s/ALL 
   s/VAL
   (s/cond-path 
     [:director #(= "James Cameron" %)] 
     [:rating #(> % 8.0)])] 
  movies)
=> => [[{:name "Braveheart", :director "James Cameron", :rating 8.4} 8.4] ... ]
``` 

Now we get the full map back, but it's wrapped in a collection. We can
introduce a helper function for this use case:

```clojure
(defn select-maps 
  [selector structure]
    (->> (s/select selector structure)
         ; the map we're after is always the first argument
         (mapv (fn [[m & _]] m))))    
```

Now working with sequences of maps is more comfortable:

```clojure
(select-maps
  [s/ALL 
   s/VAL
   (s/cond-path 
     [:director #(= "James Cameron" %)] 
     [:rating #(> % 8.0)])] 
  movies)
=> [{:name "Braveheart", :director "James Cameron", :rating 8.4} ... ]
``` 

Specter's path definition functions can be exploited to get e.g. 
movies of James Cameron that have an either very bad or very good rating:

```clojure
(select-maps
  [s/ALL 
   s/VAL
   (s/cond-path [:director #(= "James Cameron" %)]
     (s/multi-path [:rating #(> % 8.5)] [:rating #(< % 6.0)]))] 
  movies)

=>
[{:name "Terminator 2: Judgment Day",
  :director "James Cameron",
  :rating 8.6}
 {:name "Rambo III", :director "James Cameron", :rating 5.4}]

```

So this works, but it's clunky and requires a helper function. Is there an
easier way?

#### The right way

As pointed out to me by the library author, there's a much better way of 
accomplishing the above:

```clojure
(s/select 
  [s/ALL
   (s/selected? :director #(= "James Cameron" %))
   (s/selected? :rating #(> % 8.0))]
  movies)

```

The `selected?` function filters the current value based on whether the selector
that follows matches anything. It doesn't mess up the return value like the other
examples above. It can of course be combined with conditional paths or multipaths
as in the previous example:

```clojure
(s/select 
  [s/ALL
   (s/selected? :director #(= "James Cameron" %))
   (s/selected? (s/multi-path [:rating #(> % 8.0)] [:rating #(< % 6.0)]))]
  movies)

```

#### Conclusion

I think Specter is one of the best things to come out of the Clojure ecosystem
recently. I find it a lot easier to grasp how to use Specter than for example
zippers, which is another popular way of working with nested data structures.
It's still a bit dense to get into, though - Specter would benefit greatly
from something like the [Learn Datalog Today] [2] website!


[1]: https://github.com/nathanmarz/specter
[2]: http://www.learndatalogtoday.org/
