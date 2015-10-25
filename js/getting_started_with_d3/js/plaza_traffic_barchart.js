var saved_data = null;
function draw(data) {
  "use strict";
  console.log(data);

  /* NOTES
   * -----
   * While I am still not 100% clear what enter() does, it's instructive to
   * note that in this example, we add the data in 'line's, each of which
   * contains a 'label' and a 'bar', then we loop through and do the labels
   * and the bar...
   *   ... and we only do the data() and enter() calls the first time. So
   * together data() and enter() are what actually attach data to dom
   * elements.
   *
   * Note that the data points attached to dom elements seem to propogate
   * down in d3 calls: when we append the label and bar divs to the line
   * divs, we don't have to do any work in order for the .text and .style
   * calls to find the data point.
   */
  
  // attach the data and make an outer div.line for each point
  d3.select("body")
    .append("div").attr("class", "chart")
    .selectAll(".line")
    .data(data.cash).enter()
    .append("div").attr("class", "line");

  // for each line, attacvh an appropriate div.label and set its text
  d3.selectAll("div.line")
    .append("div").attr("class", "label")
    .text(function(d) { return d.name; });

  d3.selectAll("div.line")
    .append("div").attr("class", "bar")
    .style("width", function(d) { return d.count / 100 + "px"; })
    .text(function(d) { return Math.round(d.count); });

  /* CSS NOTES
   * ---------
   * - It's actually instructive to note what happens if you leave the css from
   *   the last exercise: the labels wind up above the bar
   * - A combination of proper sizing with a fixed width and the 
   *   float: left styling on the labels is what makes them work properly.
   * - If that's all you do, though, the bars wind up on top of the lines!
   *   The solution from the book is to add a margin-left: 22em to the bars
   * - This wasn't enough, so I had to add a few em to both the width of the
   *   labels and the margin-left of the bars.
   * - Note: I tried to make the bars float right instead of setting the
   *   margin-left by hand, but that didn't work. Everything wound up
   *   strung together, as if I had removed all of the "newlines" from the
   *   html.
   */
}
