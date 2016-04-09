(require 'cljs.build.api)

(cljs.build.api/build
  "src"
  { :main 'ch3-lines-plot.core
    :output-to "out/main.js"})
