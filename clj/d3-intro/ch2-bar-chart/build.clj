(require 'cljs.build.api)

(cljs.build.api/build
  "src"
  { :main 'ch2-bar-chart.core
    :output-to "out/main.js"})
