This is the final image-gallery code

This is Chapter 7 from "DOM Scripting" by Jeremy Keith,
which is the last version of his image gallery example.

The previous iterations, Chapters 4 and 6, are strictly
worse as examples of clojurescript bare-DOM scripting.
So I want to summarize all of the major things I have
learned / pain points I have encountered:
* how to use a simple sed-driving script to swap out
  some names on a bunch of files :)
   * I should maybe port some of this to python
     and think about releasing a tools package.
* how to set up clojurescript development in the
  browser (see the set-up-dev function)
* how to add window.onload handlers (see add-load-func);
  also, I learned that if the script tag is in the header
  rather than the body, then you *must* open the repl
  connection in a handler, not in the raw script. It will
  not work if the body has not started writing yet.
* a few clojure / clojurescript syntactical tricks:
   * the general doseq syntax
   * how to make a seq of native arrays (the array-seq func)
   * how to seq over a map (use vector destructuring)
   * how to convert a key that might be a keyword or a string
     to a string (use the name func)
   * how to use the -> macro, particularly in the context of
     host API calls (see show-pic for an example)
* how to get elements by id or tag name (the other common
  one you might want is getElementsByClassName)
* how to create new Elements and TextNodes, set attributes
  via the DOM api (see the create-elt func for my finest example)
* how to insert newly created Elements into the document
* how to properly prevent default actions (use e.preventDefault()),
  do not return false!)
* how to prevent the image from appearing to the right of the selection
  stuff, and a rough guess as to why the answer is so complicated
   * answer: use both "clear: both;" and "display: block;", you *must*
     use *both* of them
   * why both? Well, it seems to me that until you make the least have
     "float: left", the image always goes below. But once you set "float:left"
     on the list, it no longer automatically pushes the image down,
     because the image is not a so-called "block-level" element. That is
     why you need "display:block;". Moreover, even with raising it to be
     block-level, it still tends to float left because the "float:left" style
     propagates downward to siblings unless you explicitly clear it. This
     is why you need "clear: both;".
       ... take all this with a grain of salt, it is guesswork from a
       non-CSS-expert
