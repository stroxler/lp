(require 'cljs.build.api)

(cljs.build.api/watch
  "src"
  { :main 'ch2-bar-chart.core
    :output-to "out/main.js"})
