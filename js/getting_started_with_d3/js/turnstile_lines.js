function draw(data) {
  "use strict";
  var margin = 40, width = 700 - margin, height = 300 - margin;

  /**
   * Notes:
   *   most of this is pretty self-explanatory.
   *   dont' remove the path 'stroke=black' command: the lines on the plot
   *     are paths, but so are the lines inside the g.axis elements!
   */
  // unlike the previous chart, which was directly created in an "svg"
  // element and therefore seemed to be free-floating, this one will be inside
  // a "g" element.
  d3.select("body")
    .append("svg")
      .attr("width", width + margin)
      .attr("height", height + margin)
    .append("g")
      .attr("class", "chart");

  // create times square datapoints by attaching the json array in 
  // data["times_square"] to "circle.times_square" elements
  d3.select("svg")
    .selectAll("circle.times_square")
    .data(data.times_square)
    .enter()
    .append("circle")
      .attr("class", "times_square")

  // do the same for "grand_central"
  d3.select("svg")
    .selectAll("circle.grand_central")
    .data(data.grand_central)
    .enter()
    .append("circle")
      .attr("class", "grand_central")

  // set up the scale for count
  var count_extent = d3.extent(
    data.times_square.concat(data.grand_central),
    function(d) { return d.count; }
  );
  var count_scale = d3.scale.linear()
    .domain(count_extent).range([height, margin])

  // set up the scale for time:
  //   Note that we use d3.time.scale(), not d3.scale.linear()... this is what
  //   tells d3 to use date labels
  var time_extent = d3.extent(
    data.times_square.concat(data.grand_central),
    function(d) { return d.time; }
  );
  var time_scale = d3.time.scale()
    .domain(time_extent).range([width, margin])

  // create the datapoints. Note that we don't have to do anything about
  // the source of the data points here, since we can use css for that.
  d3.selectAll("circle")
    .attr("cx", function(d) { return time_scale(d.time); })
    .attr("cy", function(d) { return count_scale(d.count); })
    .attr("r", 3);

  // add the axes
  var time_axis = d3.svg.axis().scale(time_scale);
  d3.select("svg")
    .append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0, " + height + ")")
    .call(time_axis);
  var count_axis = d3.svg.axis().scale(count_scale).orient("left");
  d3.select("svg")
    .append("g")
      .attr("class", "y axis")
      .attr("transform", "translate(" + margin + ", 0)")
    .call(count_axis);

  // add the paths to get a line plot
  //   note how d becomes an attribute, which gets used to determine .x and .y
  var line = d3.svg.line()
    .x(function(d) { return time_scale(d.time); })
    .y(function(d) { return count_scale(d.count); });
  d3.select("svg")
    .append("path")
      .attr("d", line(data.times_square))
      .attr("class", "times_square");
  d3.select("svg")
    .append("path")
      .attr("d", line(data.grand_central))
      .attr("class", "grand_central");

  // add the titles. This is probably the most annoying part
  //    note that this rotation makes waaay more sense than the one before
  //    in terms of short developer time (rotate clockwise around a point at
  //    the top, no translation needed!), but the text points the opposite
  //    direction.
  //  ... I'm pretty sure doing all this crap by hand isn't the right way to
  //      do it anyway...
  d3.select(".y.axis")
    .append("text")
    .text("mean number of turnstile revolutions")
    .attr("transform", "rotate(90, " + (-margin) + ", 0)")
    .attr("x", margin / 2)
    .attr("y", 0);
  d3.select(".x.axis")
    .append("text")
    .text("time")
    .attr("x", (width / 1.6) - margin)
    .attr("y", margin / 1.5);

}
