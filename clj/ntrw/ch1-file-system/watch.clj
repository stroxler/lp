(require 'cljs.build.api)

(cljs.build.api/watch
  "src"
  { :main 'ntrw.core
    :output-to "main.js"
    :target :nodejs})
