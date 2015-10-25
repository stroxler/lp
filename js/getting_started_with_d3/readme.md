# Getting Started with D3

This code is adapted from the Mike Dewar book 'Getting Started with D3'.

There are a few oddities in my coverage:
 - I skipped the last chapter on layouts because it seemed inadequate
 - There are two versions of the multinine example because of some CSS
   style stuff I ran into.

## Setup

This code is self-contained. But if you ever needed an updated
copy of the d3 source code (which I have in `lib` here), you would get it
by running
```
wget https://cdnjs.cloudflare.com/ajax/libs/d3/3.5.6/d3.min.js
```
or alternatively you could add the script as a link in your html instead
of keeping it local

To actually start the webserver, run (from this directory)
```
python -m SimpleHTTPServer 5555
```

## Alternatives

D3 is pretty cool. This book is much too basic to really help with appreciating
its power.

That said, for a data analyst (as opposed to a front-end visualization
specialist) there are easier and possibly better alternatives, for example
the `bokeh` library in python (which can be used standalone or as a web-based
front-end for matplotlib) and a few R libraries.
