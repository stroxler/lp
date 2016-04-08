(require 'cljs.build.api)

(cljs.build.api/build
  "src"
  { :main 'ntrw.core
    :output-to "main.js"
    :optimizations :simple
    :target :nodejs})
