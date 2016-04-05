(require 'cljs.build.api)

(cljs.build.api/build
  "src"
  { :main 'ch9-styled-table.core
    :output-to "out/main.js"})
