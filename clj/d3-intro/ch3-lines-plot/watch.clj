(require 'cljs.build.api)

(cljs.build.api/watch
  "src"
  { :main 'ch3-lines-plot.core
    :output-to "out/main.js"})
