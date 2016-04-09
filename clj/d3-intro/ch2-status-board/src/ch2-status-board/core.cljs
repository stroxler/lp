(ns ch2-status-board.core
  (:require [clojure.browser.repl :as cbrepl]))


(def d3 js/d3)

; What's shown here?
;   - a minimal example of using d3 for dom manipulation
;   - using vanilla ajax to feed d3 data, not the d3 ajax
;     functions (the later code really doesn't care how
;     you got the data, a fact which can be obscured if you're
;     always using the d3 stuff)
;   - in my vanilla ajax function, I don't convert to
;     clojurescript data types, hence I now have two almost
;     identical make-request calls.

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; development hacks

(defn view-computed-style [elt style-key]
  "If style-key is a string, get the computed style for an element"
  (aget (.getComputedStyle js/window elt) style-key))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; general purpose tools

(defn make-request-clj [method url async handler]
  "Make an ajax json request, and pass response to `handler`
  as clojurescript data"
  (let [req         (js/XMLHttpRequest.)
        req-ok      (fn [] (= (.-status req) 200))
        data-ready  (fn [] (= (.-readyState req) 4))
        req-done    (fn [] (and (req-ok) (data-ready)))
        parse-json  (fn [string] (-> string (JSON/parse) (js->clj)))
        parse-resp  (fn [] (-> req (.-responseText) (parse-json)))
        handle-resp (fn [_]
                      (when (req-done)
                        (let [response (parse-resp)]
                          (handler response))))]
    (set! (.-onreadystatechange req) handle-resp)
    (.open req method url async)
    (.send req)))

(defn make-request-js [method url async handler]
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
  (let [selection      (-> d3
                           (.select "body")
                           (.append "ul")
                           (.selectAll "li")
                           (.data data))
        get-status     (fn [d] (-> d (.-status) (aget 0)))
        service-good   (fn [d] (= (get-status d) "GOOD SERVICE"))]
    (-> selection (.enter) (.append "li"))
    (-> selection (.exit) (.remove))
    (-> selection
        (.text (fn [d] (str (.-name d) ": " (.-status d))))
        (.attr "class"
               (fn [d] (if (service-good d) "good-service" "")))
    )
  ))

(defn run-d3 []
  (println "in run-d3")
  (make-request-js
    "GET" "public/service_status.json" true draw))


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
