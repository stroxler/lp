(require 'cljs.build.api)

(cljs.build.api/watch
  "src"
  { :main 'ch3.core
    :output-to "out/main.js"})
