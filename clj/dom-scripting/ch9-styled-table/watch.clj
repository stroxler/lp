(require 'cljs.build.api)

(cljs.build.api/watch
  "src"
  { :main 'ch9-styled-table.core
    :output-to "out/main.js"})
