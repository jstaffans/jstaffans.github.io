---
layout: post
title:  "freq-words: learn to read with ClojureScript"
date:   2015-05-25
tags:   [clojurescript, reagent, boot]
---

My father, a retired elementary school teacher, runs a [website](http://www.kjellstaffans.fi) 
offering material for young children who are just starting to learn how to read and 
write in Swedish. A big portion of the material is geared towards children who experience 
difficulties learning the basics of reading and writing and one of the teaching aids
is an [old Flash application](http://www.kjellstaffans.fi/wp-content/uploads/frekord.html)
I wrote almost a decade ago; children can use the application to practice reading some of 
the most common words in the Swedish language. 

I think it's time to give the application a make-over by way of modernising the UI and
providing some frequently-asked-for features such as a timer and the possibility of 
saving high scores. I'll take the opportunity to learn some more about web application
development using ClojureScript. This article is the first in a series describing 
my successes and failures developing the application.

I use React.js a lot a work, so I'm interested in trying out one of its ClojureScript
wrappers. I decided to pick Reagent because its state management facilities seem simpler
than Om's. While I'm at it I'll try out [Boot](http://boot-clj.com) as well. Martin Klepsch,
a friend from the Berlin Clojure meetup group, handily provides a [batteries-included
template for ClojureScript development with Boot](https://github.com/martinklepsch/tenzing). 
Thanks Martin! 

I'll use Cursive Clojure as my development environment and base my styles on 
Twitter Bootstrap. Sass is a must-have in my book so I'll try to integrate that
as well. 

With the basic dependencies sorted out it's time to get started!

### Setting up the project

Setting up a new Boot+ClojureScript project is easy with Tenzing:

```bash
$> lein new tenzing freq-words +reagent
$> cd freq-words
$> boot dev
```

Pointing a browser to `localhost:3000` now gives us a view of the basic default application. 

As I said earlier, I use Cursive (the Clojure/ClojureScript plugin for IntelliJ IDEA) as my
development environment, which unfortunately does not integrate directly with Boot for 
the goodies like auto-completion and inline documentation that I'm used to. In order to
get those working, I add a dummy `project.clj` file that Cursive can pick up, containing
the application dependencies from `build.boot` file (I won't add any Boot-specific 
dependencies since I won't be changing the `build.boot` file that much):

```clojure
(defproject freq-words "0.1.0-SNAPSHOT"
  :description "freq-words - project file for Cursive Clojure"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[reagent "0.5.0"]])

```

Cursive can now pick up the project's dependencies. I'll just have to remember to 
update both files if I add any third-party libraries.

#### My first Boot task: compiling Sass files

I will base my layout on a free theme from Bootswatch, but will also most likely
add some styles of my own. I think CSS gets tedious really fast so I prefer to 
use a higher-level language like Sass. Having downloaded the base theme as 
`src/scss/_bootstrap.scss`, I will add an `src/scss/main.scss` file as well
where I include the base theme and can add any custom styles:

```scss

@import 'bootstrap';

// a little custom style
.my-form {
  background: blue;
}

```

The challenge now is to add Sass compilation to the Boot build chain. 
A custom task does the trick (based on a conversation on the [Hoplon 
mailing list](http://hoplon.discoursehosting.net/t/trying-to-create-a-boot-task-to-compile-sass-files/386)): 

```clojure

(deftask sass
  []
  (let [tmp (core/tmp-dir!)]
    (core/with-pre-wrap fs 
      (let [in-files (core/input-files fs)
            in-main (first (core/by-re [#"^(?!_).*\.scss"] in-files))
            out-dir (io/file tmp "stylesheets")
            out (io/file out-dir "main.css")]
        (.mkdirs out-dir)
        (util/dosh "sassc"
          "--style" "compressed"
          (.getPath (core/tmp-file in-main))
          (.getPath out))
        (-> fs
          (core/add-resource tmp)
          (core/commit!))))))
```

This task only considers one file (the first `.scss` files that does not start with an underscore). When using Sass, files whose name start with an underscore are files that are included in another stylesheet (`src/scss/main.scss` handles all the includes in my case), so we do not need to consider them separately.

To make the task find the Sass sources, we need to add the relevant directory to
the `:source-paths` key in the Boot environment:

```clojure
; This is one of the first lines in the build.boot file
(set-env! :source-paths #{"src/cljs" "src/scss"} ...
```

And to integrate our task into the Boot build chain, we'll add it to the pre-defined
`build` task that is part of the Tenzing template:

```clojure
(deftask build []
  (comp (speak)
        (sass)
        (cljs)))
```

Boot being awesome, we get things like file change watching for the Sass files automatically. 

We can verify that everything works, both for development and production use:

```bash
§> boot dev  
# Development site running on http://localhost:3000
§> boot build
$> cd target && python -m SimpleHTTPServer
# Production site running on http://localhost:8000
```

### Wrap-up

We now have a pretty nice setup to start working on the application itself. 
Sources are [available on Github](https://github.com/jstaffans/freq-words). Stay tuned for the next installment in the series!
