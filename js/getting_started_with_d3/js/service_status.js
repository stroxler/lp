var saved_data = null;
function draw(data) {
  "use strict";
  // Reference for each func below:
  // ------------------------------
  // https://github.com/mbostock/d3/wiki/Selections#d3_select
  // https://github.com/mbostock/d3/wiki/Selections#append
  // https://github.com/mbostock/d3/wiki/Selections#selectAll
  // https://github.com/mbostock/d3/wiki/Selections#data
  // https://github.com/mbostock/d3/wiki/Selections#enter
  // https://github.com/mbostock/d3/wiki/Selections#append (again)
  // https://github.com/mbostock/d3/wiki/Selections#text

  d3.select("body")   // matches first jquery-style dom elem matching selector
    .append("ul")     // append new elem as last child of each elem in cur sel
    .selectAll("li")  // for each elem in cur sel, select all matching desc
    .data(data)       // joins specified array of data w current selection
    .enter()          // selects elems of selection that aren't on screen yet
    .append("li")     // creates an actual list element for each data point
    .text(            // modifies each entry in the selection; d is an object
       function(d) {
         return d.name + ": " + d.status;
       }
    );

  /* NOTES
   * The selectAll("li") is needed after appending the "ul" because otherwise,
   * when you do .data(data).enter(), the prospective DOM elements don't
   * actually wind up isnside the ul. You don't really notice it in this
   * example, but if you look at plaza_traffic_barchart and you remove the
   * equivalent line, you'll notice that the css on the chart doesn't propogate
   * down... because the elements aren't actually under the chart div, they
   * are directly inside the body!
   *
   * So basically, what we do is:
   *   select the body
   *   add an unlabeled list
   *   attach the data to the list
   *   enter, which attaches us to just the data elements that don't
   *     exist on the page (I'm still kinda fuzzy on this)
   *   append a li for each entry in the array
   *   add text for each li, which depends on the object d that was pulled
   *     from the json array
   *  
   *  The input to a d3 script should be a json array. This is the json
   *  equivalent of a csv: the outer layer is an array, and the inner layer
   *  is a set of key-value pairs which should all have the same keys and
   *  datatypes.
   */

  // now, let's add some css styling that depends on the content:
  // https://github.com/mbostock/d3/wiki/Selections#style
  d3.selectAll("li")
    .style(
       "font-weight",
       function(d) {
         if (d.status == "GOOD SERVICE") {
           return "normal";
         } else {
           return "bold";
         }
       }
     );

}
