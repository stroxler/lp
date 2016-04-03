(require 'cljs.build.api)

(cljs.build.api/build
  "src"
  { :main 'ch3.core
    :output-to "out/main.js"})
