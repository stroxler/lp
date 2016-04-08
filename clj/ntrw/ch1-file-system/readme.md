This is my first real node project.

I started it based on the node quickstart; the contents
are me going through "Node.js the Right Way" and translating
to clojurescript.

Overall my experience using clojurescript with node has not
been good. In the browser I like it because
 1) you have no choice but javascript, so the negatives of
    javascript are less important (whereas you could use, e.g.,
    ruby or python on the server side instead)
 2) the emphasis on async in node - which is both less necessary
    than in the browser and more annoying to me - seems to make
    writing clean clojurscript pretty hard
 3) the fact that there are so many untyped errors (e.g. you have
    a typo in a var name and instead of it telling you, you get
    some undefined error that is a bit cryptic in javascript and
    more so when you are translating from clojurescript) turns
    out to be a huge pain.

So, after satisfying myself that I largely managed to figure out
the first chapter of "Node.js the Right Way", I am ready to
give up, and move on to more interesting clojurescript stuff in
the browser.

I should add that I still think planck might be worth a shot. It
is more geared toward clojurescript, plus it does not emphasize
async io and it compiles really fast. So clojurescript system
scripting may still be an option down the road.


I also want to jot down a few notes:
  - the clojurescript quickstart says that for node,
    there is little reason to use advanced optimization.
    This is false. It is not as important, since you will
    not be sending code over the internet except for distribution,
    and if you are always compiling locally it may be completely
    true that you do not need optimization. But if you want to
    distribute a node script as your end product, then (a)
    doing any optimization gives you a single-script output, which
    is wonderful for sharing, and (b) doing advanced optimization
    gives you a much smaller output assuming your script does not
    use too many dependencies
  - partial counterargument: for a simple script the overhead
    is big. Advanced optimization produces a file under 100K for
    a trivial hello world, whereas simple produces a file around
    700K. *But* the overhead probably drops a bit as your code
    gets more complex in relative terms, and perhaps more importantly
    a 2Mb executable, even if wasteful, probably is irrelevant for
    my purposes. It would matter if I needed to ship it to new machines
    often, or if I wanted to distribute it as a tool for other
    people to use.
