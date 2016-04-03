The setup I am using here (bare clojurescript, no leiningen)
comes fromt he browser-focused portion of the
clojurescript quickstart, at
  https://github.com/clojure/clojurescript/wiki/Quick-Start

The content of my code is Chapter 3 of "DOM Scripting" by
Jeremy Keith.

Most of what is going on here should be self-explanatory from
skimming the makefile:
  - building once with build.clj
  - building with a watch using watch.clj
  - attaching a repl using repl.clj, which also necessitates
    adding a block of code to the core.cljs file. This code,
    I want to add, does *not* cause any problems if you continue
    to just open index.html.

To use the repl, you need to run `make repl`, which will build
the code once and start up the repl (using rlwrap to provide
a nicer interactive environment). Then, if you go to
localhost://9000 in a browser, you will see the page, plus
if you take actions in the repl (console printing, or writing
to js/document for example) the page in your browser is affected.

To get access to code from your .cljs file in the repl,
require it:
```
(require '[ch3.core :as hello])
```
or if you have ve modified the code
```
(require '[ch3.core :as hello] :reload)
```
