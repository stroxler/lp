Starting "Getting Started with D3" in clojurescript

This code is the second half of Chapter 2, which is a
div-based plain-html bar chart (still no svg so far)

What have I learned here?
  - I got to see how d3 works with numeric data, in
    a plain html setting.
  - I got to play a bit more with css, and get a little
    more feel for what the attributes do.

What bothers me about this code?
  - I used the enter and update phases in a way that
    works fine for static data, so I am happy enough I
    guess. But to make this dynamic-able you would need
    to do better than what I did, which was to use the
    update phase for appending and setting attributes
    on the labels and bars.. in fact, you would wind up with
    new divs if you updated the data!
