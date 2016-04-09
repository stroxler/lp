(ns ch2-bar-chart.core
  (:require [clojure.browser.repl :as cbrepl]))


(def d3 js/d3)

; What's shown here?
;   - a minimal example of using d3 for a bar chart:
;     we aren't even using svg here!
;   - Note that the enter/update/exit breakdown isn't
;     very clean here. I need to learn a better way.

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; development hacks

(defn view-computed-style [elt style-key]
  "If style-key is a string, get the computed style for an element"
  (aget (.getComputedStyle js/window elt) style-key))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; general purpose tools

(defn make-request [method url async handler]
  "Make an ajax json request, and pass response to `handler`
  as javascript data"
  (let [req         (js/XMLHttpRequest.)
        req-ok      (fn [] (= (.-status req) 200))
        data-ready  (fn [] (= (.-readyState req) 4))
        req-done    (fn [] (and (req-ok) (data-ready)))
        parse-json  (fn [string] (JSON/parse string))
        parse-resp  (fn [] (-> req (.-responseText) (parse-json)))
        handle-resp (fn [_]
                      (when (req-done)
                        (let [response (parse-resp)]
                          (handler response))))]
    (set! (.-onreadystatechange req) handle-resp)
    (.open req method url async)
    (.send req)))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; core d3 logic

(defn draw [data]
  (let [chart      (-> d3    (.select "body")
                             (.append "div")
                             (.attr "class" "chart"))
        selection  (-> chart (.selectAll ".line")
                             (.data (.-cash data)))
        get-label  (fn [d] (.-name d))
        get-text   (fn [d] (str (.round js/Math (.-count d))))
        get-width  (fn [d] (str (/ (.-count d) 1000) "em" ))]
    ; append the line divs themselves
    (-> selection (.enter) (.append "div") (.attr "class" "line"))
    (-> selection (.exit) (.remove))
    ; add the labels to each line
    (-> selection
        (.append "div")
        (.attr "class" "label")
        (.text get-label))

    (-> selection
        (.append "div")
        (.attr "class" "bar")
        (.text get-text)
        (.style "width" get-width))
  ))

(defn run-d3 []
  (make-request
    "GET" "public/plaza_traffic.json" true draw))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; code to actually run everything


(defn add-load-func [f]
  (let [old-onload (.-onload js/window)]
    (set! (.-onload js/window)
          (if (nil? old-onload)
            f
            (fn [_] (old-onload) (f))))))


(defn set-up-dev []
  (enable-console-print!)
  (defonce conn (cbrepl/connect "http://localhost:9000/repl")))


(add-load-func set-up-dev)
(add-load-func run-d3)
