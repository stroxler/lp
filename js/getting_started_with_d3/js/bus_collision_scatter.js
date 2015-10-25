var saved_data = null;
function draw(data) {
  "use strict";
  saved_data = data;

  // set up overall scales for our plot.
  //   If this plot were embedded in a page, we'd presumably need
  //   to infer all this from the context (e.g. from the IDE window manager)
  var margin = 50;
  var width = 700;
  var height = 300;


  d3.select("body")
    .append("svg")
      .attr("width", width)
      .attr("height", height)
    .selectAll("circle")
    .data(data)
    .enter()
    .append("circle")

  // set scales.
  //  - d3.extent is a builtin that finds the range of outputs of the callback.
  //  - the scales themselves are functions, as you can see in the
  //    block that starts with d3.selectAll("circle")
  //  - the axes are svg elements created via a d3 utility; they actually
  //    consist of an outer element that wraps the axis and ticks. You
  //    can use the inspector in a browser to see in more detail.
  var x_extent = d3.extent(data,
                           function(d) { return d.collision_with_injury; })
  var x_scale = d3.scale.linear()
    .range([margin, width - margin])
    .domain(x_extent)
  var x_axis = d3.svg.axis().scale(x_scale);

  var y_extent = d3.extent(data,
                           function(d) { return d.dist_between_fail; })

  var y_scale = d3.scale.linear()
    .range([margin, height - margin])
    .domain(y_extent)
  var y_axis = d3.svg.axis().scale(y_scale).orient("left");

  // create the axes. This is kind of technical svg stuff
  //  - we create a "g" svg dom object for each axis, with the proper
  //    class, and then transform it relative to the top-left of the screen.
  //  - (check: do you see why we need to put (height - margin) in parenths?)
  //  - the classes on the axes, once again, do *not* do anything through
  //    d3, htey are only for styling.
  d3.select("svg")
    .append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0," + (height - margin) + ")")
    .call(x_axis);

  d3.select("svg")
    .append("g")
      .attr("class", "y axis")
      .attr("transform", "translate(" + margin + ",0)")
    .call(y_axis);

  // create and position the circle elements
  d3.selectAll("circle")
    .attr("r", 5)
    .attr("cx", function(d) { return x_scale(d.collision_with_injury); })
    .attr("cy", function(d) { return y_scale(d.dist_between_fail); });

  // add axis labels
  //  - note that .x and .axis are two different classes here. Adding two
  //    space-separated classes in the .attr() method adds two classes, and
  //    putting both of them with dots in a CSS selectors specifies them.
  //  - we append a text element and modify its location by hand. ugh.
  //  - note that based on the x and y coords we give, they must refer to
  //    the *center* of the dom element (not its top-left)
  //  - and they are *relative to the containing element*, that is, relative
  //    to the axis "g" element, *not* relative to the window
  d3.select(".x.axis")
    .append("text")
    .text("Collisions with injury (per million miles)")
    .attr("x", (width / 2) - margin)
    .attr("y", margin / 1.5);

  // the transform is sort of confusing...
  //  - rotate is clockwise, the opposite of the norm in math... ugh
  //  - it accepts the rotation, and then the x and y coords of the location
  //    we want to rotate around. So we are rotating around x=-43, y=0,
  //    which is slightly to the left of and at the top of the element
  //    (default location is to put the top-right of the *text* at 0, 0)
  //  - then, we've rotated it way above the top of our chart, so we
  //    have to translate it down a bunch.
  //  - don't do this before the CSS... the text sizes are off so it will
  //    look terrible. The numbers you pick here depend on text size.
  d3.select(".y.axis")
    .append("text")
    .text("Mean distance between failures (in miles)")
    .attr("transform", "rotate(-90, -43, 0) translate(-280)");
}
