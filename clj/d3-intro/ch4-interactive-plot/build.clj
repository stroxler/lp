(require 'cljs.build.api)

(cljs.build.api/build
  "src"
  { :main 'ch4-interactive-plot.core
    :output-to "out/main.js"})
