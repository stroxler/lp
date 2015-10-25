function draw(data) {

  /* NOTES
   * -----
   * For the most part this works. The remaining issues are
   *   - the line width doesn't seem to be right. Somehow my css 3px width
   *     isn't hooked up.
   *   - The labels are fuzzy. Somehow opacity 1 isn't cutting it, they are
   *     getting mixed up with the background.
   *   - This one isn't my problem: the book doesn't deal with it. But, if you
   *     mouse over the last label, when you mouse out the circle shrinks back
   *     down. If this were a production-grade plot that wouldn't be
   *     acceptable.
   */
  var container_dimensions = { width: 900, height: 400 };
  var margins = { top: 10, right:20, bottom: 30, left: 60 };
  var chart_dimensions = {
      width: container_dimensions.width - (margins.right + margins.left),
      height: container_dimensions.height - (margins.top + margins.bottom),
  };
  var chart = d3.select('#timeseries')
    .append('svg')
      .attr('width', container_dimensions.width)
      .attr('height', container_dimensions.height)
    .append('g')
      .attr('transform', 'translate(' + margins.left + ', ' + margins.top + ')')
      .attr('id', 'chart');

  var time_scale = d3.time.scale()
    .range([0, chart_dimensions.width])
    .domain([new Date(2009, 0, 1), new Date(2011, 3, 1)]) // probably could get
                                                          // dates from data...
  var percent_scale = d3.scale.linear()
    .range([chart_dimensions.height, 0])
    .domain([65, 90])

  var time_axis = d3.svg.axis()
    .scale(time_scale);

  var percent_axis = d3.svg.axis()
    .scale(percent_scale)
    .orient('left');

  chart.append('g')
    .attr('class', 'x axis')
    .attr('transform', 'translate(0, ' + chart_dimensions.height + ')')
    .call(time_axis);

  chart.append('g')
    .attr('class', 'y axis')
    .call(percent_axis);

  d3.select('.y.axis')
    .append('text')
      .attr('text-anchor', 'middle')
      .text('percent on time')
      .attr('transform', 'rotate(-270, 0 0)')
      .attr('x', container_dimensions.height / 2) // I don't understand this
      .attr('y', 50);

  // we attach the data to the key_line divs, which are containers for
  //   the key_square and key_label elements. Then to actually plot,
  //   we will attach callbacks to the squares.
  var key_items = d3.select('#key')
    .selectAll('div')
    .data(data).enter()
    .append('div')
      .attr('class', 'key_line')
      .attr('id', function(d) { return d.line_id; });
  // note that we can perform operations on all lines as if there were just one
  key_items.append('div')
    .attr('id', function(d) { return 'key_square_' + d.line_id; })
    .attr('class', function(d) { return 'key_square ' + d.line_id; });
  key_items.append('div')
    .attr('class', 'key_label')
    .text(function(d) { return d.line_name; });

  d3.selectAll('.key_line')
    .on('click', toggleGraphLine);

  function toggleGraphLine() {
    // in this function, `this` refers to the element for which we assigned
    // this function as the callback. So it refers to a '.key_line' div.
    var id = d3.select(this).attr('id');
    var lineInGraph = d3.select('#' + id + '_path');
    if (lineInGraph.empty()) {
      d3.json('data/subway_wait.json', function(data) {
        drawTimeseries(
          data.filter(function(d) { return d.line_id == id; }), id
        );
      });
    } else {
      lineInGraph.remove();
      d3.select(this).attr('background', 'white');
    }
  }

  function drawTimeseries(data, id) {
    // note: line actually creates a function, which is called at the end
    var line = d3.svg.line()
      .x(function(d) { return time_scale(d.time); })
      .y(function(d) { return percent_scale(d.late_percent); })
      .interpolate('linear');
    // note that we set the id, so that get_timeseries_data knows whether
    // we should remove or add the data.
    var thisPathG = d3.select('#chart')
      .append('g')
      .attr('id', id + '_path')
      .attr('class', id);
    thisPathG.append('path')
      .attr('d', line(data));
    // add the circles, with animation
    //   I think you can also add the animations in a separate block, globally
    //   for all circles in `thisPathG`. Then the circles you add would inherit
    //   them
    var DURATION = 500;
    thisPathG.selectAll('circle')
      .data(data).enter()
      .append('circle')
        .attr('cx', function(d) { return time_scale(d.time); })
        .attr('cy', function(d) { return percent_scale(d.late_percent); })
        .transition()  // start the animation
        .delay(function(d, i) { return DURATION * i / data.length; })
        .attr('r', 5)
        .each('end', function(d, i) {
           if (i === data.length - 1) { addLabelToLine(thisPathG, this, d); }
        });
    // add mousover animation and text
    thisPathG.selectAll('circle')
      .on('mouseover', function(d) {
         // here `this` refers to the circle element
         d3.select(this).transition().attr('r', 9);
      })
      .on('mouseout', function(d) {
         d3.select(this).transition().attr('r', 5);
      })
      .on('mouseover.tooltip', function(d) {
         d3.select('text#' + d.line_id).remove(); // get rid of any old tooltip
         d3.select('#chart')
           .append('text')
           .text(d.late_percent + '%')
           .attr('x', time_scale(d.time) + 10) // slightly to the right
           .attr('y', percent_scale(d.late_percent) - 10) // slightly above
           .attr('id', d.line_id);
      })
      .on('mouseout.tooltip', function(d) {
         d3.select('text#' + d.line_id)
           .transition()
           .duration(DURATION)
           .style('opacity', 0)
           .attr('transform', 'translate(10, -10)')
           .remove();
      });
  }

  function addLabelToLine(thisPathG, circle, d) {
    // make the circle big
    d3.select(circle)
      .transition()
      .attr('r', 9);
    // add text to the thisPathG element in the correct spot.
    thisPathG.append('text')
      .text(d.line_id.split('_')[1])
      .attr('x', time_scale(d.time))
      .attr('y', percent_scale(d.late_percent))
      .attr('dy', '0.35em') // not really sure what this does. apparently
                            // something similar to the 'dominant-baseline'
                            // style
      .attr('class', 'linelabel')
      .attr('text-anchor', 'middle')
      .style('opacity', 0)
      .style('fill', 'white')
      .transition()
        .style('opacity', 100);
  }

}
