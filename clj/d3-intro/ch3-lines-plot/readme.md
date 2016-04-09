Starting "Getting Started with D3" in clojurescript

This code is the second half of Chapter 3, which is a
line plot which expands on the scatter plot by introducing
data-driven svg path elements and data-dependent
coloring.

New stuff in this code:
  - the handling of axis, etc is better than in my scatterplot
    example, since I used a translated g element for the data
    itself. However, it still seems messy and I don't think it's
    quite idiomatic yet.
  - We used class assignments and css to color different data
    differently
  - We used the time scale function to handle time (in milliseconds)
  - We used the line function to create paths
