Starting "Getting Started with D3" in clojurescript

This code is the first half of Chapter 3, which is a
scatterplot which serves as a nice introduction to
svg, scales, and extents.

The trickiest thing actually seems to be not generating
the plot, but rather finding a clean way of thinking
about the axis transforms.

There's some stuff I'm not doing very well, I think:
  * the normal idiom, as I understand it, is to define
    inner and outer dimensions, and maye make an inner
    svg for the actual data. We're doing it all manually
    and it's ugly
  * I definitely missed a chance to try factoring out
    a chunk of my code (although given the previous bullet,
    maybe this is good)

What's shown here?
   * using scales (remember the range and domain inputs
     must be arrays!) and extents
   * I think using margins the way this author is is
     not the preferred way of embedding a plot inside
     an svg. Although it works, I think it's more common
     to put a new g element (like a div) inside the outer
     one.
   * if you get a TypeError: Cannot read property 'call'
     of undefined, it likely means that you are trying to
     use a function that isn't in your namespace (unfortunately
     this is a case where clojurescript's host language shines
     through, giving a not-very-useful-to-newbies error)
   * if you get that error in a chain of d3 calls, it may mean
     that the *preceding* call has some kind of error, which
     caused it to not return.
   * the transform attribute is really good to understand.
     There are a few key ideas I learned trying to debug here:
       * as you might imagine, the transforms are applied in
         order, so rotate => translate != translate => rotate
       * you need to remember that negative y is up, not down,
         and also that positive rotation is clockwise
       * any transform is relative to the current coordinate
         system. This means, e.g.
           * if you've rotated 90 degrees, then to move right
             you need to go in the negative y direction
           * if you rotate(90, 0, 0), it will swing down, with
             its hinge as whatever the top-left corner is
             *after* applying any previous transforms
