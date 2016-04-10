(ns ch4-interactive-plot.core
  (:require [clojure.browser.repl :as cbrepl]))


(def d3 js/d3)
(def format clojure.core/format)

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
; d3 tools

(defn svg-translate [x y]
  (str "translate(" x ", " y ")"))

(defn svg-rotate [deg x y]
  (str "rotate(" deg ", " x ", " y ")"))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; core d3 logic

(defn draw [data]
  (let [container-dims  {:width 900 :height 400}
        margins         {:top 10 :right 20 :bottom 30 :left 60}
        chart-dims      {:width (- (:width container-dims)
                                   (:right margins) (:left margins))
                         :height (- (:height container-dims)
                                    (:bottom margins) (:top margins))}
        chart           (-> d3
                            (.select "#timeseries")
                            (.append "svg")
                            (.attr "width" (:width container-dims))
                            (.attr "height" (:height container-dims))
                            (.append "g")
                            (.attr "transform"
                                   (svg-translate (:left margins) (:top margins)))
                            (.attr "id" "chart"))
        time-scale      (-> d3
                            (.-time)
                            (.scale)
                            (.range #js [0 (:width chart-dims)])
                            (.domain #js [(js/Date. 2009 0 1) (js/Date. 2011 3 1)]))
        percent-scale   (-> d3
                            (.-scale)
                            (.linear)
                            (.range #js [(:height chart-dims) 0])
                            (.domain #js [65 90]))
        time-axis-f     (-> d3
                            (.-svg)
                            (.axis)
                            (.scale time-scale))
        time-axis       (-> chart
                            (.append "g")
                            (.attr "class" "x axis")
                            (.attr "transform"
                                   (svg-translate 0 (:height chart-dims)))
                            (.call time-axis-f))
        percent-axis-f  (-> d3
                            (.-svg)
                            (.axis)
                            (.scale percent-scale)
                            (.orient "left"))
        percent-axis    (-> chart
                            (.append "g")
                            (.attr "class" "y axis")
                            (.call percent-axis-f))
        percent-label   (-> percent-axis
                            (.append "text")
                            (.attr "text-anchor" "middle")
                            (.text "percent on time")
                            (.attr "transform"
                                   (str (svg-translate -50 (* 0.3 (:height container-dims)))
                                        (svg-rotate 90 0 0)))
                            ; note that x and y, seem to be affected by the rotate
                            (.attr "y" (/ (:height container-dims) 2))
                            (.attr "x" 50))
        key-items       (-> d3
                            (.select "#key")
                            (.selectAll "div")
                            (.data data)
                            (.enter)
                            (.append "div")
                            (.attr "class" "key-line")
                            (.attr "id" #(.-line_id %)))
        key-squares     (-> key-items
                            (.append "div")
                            (.attr "id" #(str "key-square-" (.-line_id %)))
                            (.attr "class" #(str "key-square " (.-line_id %))))
        key-lines       (-> key-items
                            (.append "div")
                            (.attr "id" #(str "key-label-" (.-line_id %)))
                            (.attr "class" "key-label ")
                            (.text #(.-line_name %)))
        ]
    (letfn
      [
        (line-path-id
         [line-id as-selector?]
         "get the id for the path of a subway lines' timeseries"
         (if as-selector?
           (str "#" line-id "_path")
           (str line-id "_path")))

        (draw-line-timeseries
          [line-id line-data]
          "Draw the path for one line's time series"
          (let [short-id  (aget (.split line-id "_") 1)
                x-loc-f   #(time-scale (.-time %))
                y-loc-f   #(percent-scale (.-late_percent %))
                line-f    (-> d3
                              (.-svg)
                              (.line)
                              (.x x-loc-f)
                              (.y y-loc-f)
                              (.interpolate "linear"))
                line-g  (-> chart
                            (.append "g")
                            (.attr "id" (line-path-id line-id false))
                            (.attr "class", (str "timeseries " line-id)))
                line-path  (-> line-g
                               (.append "path")
                               (.attr "d" (line-f line-data))
                               (.attr "class" line-id))
                line-points (-> line-g
                                (.selectAll "circle")
                                (.data line-data)
                                (.enter)
                                (.append "circle")
                                (.attr "cx" x-loc-f)
                                (.attr "cy" y-loc-f)
                                (.attr "r" 4))
                ]
            (-> line-points
                ; make the dots grow on mouseover
                (.on "mouseover"
                     (fn [_]
                       (let [this-circ (js* "this")]
                         (-> d3 (.select this-circ) (.transition) (.attr "r" 7)))))
                ; make them shrink on mouseout
                (.on "mouseout"
                     (fn [_]
                       (let [this-circ (js* "this")]
                         (-> d3 (.select this-circ) (.transition) (.attr "r" 4)))))
                ; make the percentage pop up as a tooltip
                (.on "mouseover.tooltip"
                     (fn [d]
                       (-> d3 (.select (str "text#" line-id)) (.remove))
                       (-> chart
                           (.append "text")
                           (.text (str (.-late_percent d) "%"))
                           (.attr "x" (x-loc-f d))
                           (.attr "y" (y-loc-f d))
                           (.attr "id" line-id) )))
                ; add animation to make the tooltip fly away and fade
                (.on "mouseout.tooltip"
                     (fn [d]
                       (-> d3
                           (.select (str "text#" line-id))
                           (.transition)
                           (.duration 500)
                           (.style "opacity" 0)
                           (.attr "transform" (svg-translate 10 (- 10)))
                           (.remove) )))
            )
            ; add the end label
            (let [d   (last (array-seq line-data))]
              (-> line-g
                  (.append "circle")
                  (.attr "cx" (x-loc-f d))
                  (.attr "cy" (y-loc-f d))
                  (.attr "r" 10))
              (-> line-g
                  (.append "text")
                  (.text short-id)
                  (.attr "text-anchor" "middle") ; centers wrt x
                  (.attr "x" (x-loc-f d))
                  (.attr "y" (y-loc-f d))
                  (.attr "dy" "0.35em")) ; centers wrt y
              )
          ))

        (get-line-data
           [line-id callback]
           "Fetch the data for one subway line"
           (.json d3 "public/subway_wait.json"
                 (fn [wait-data]
                   (-> (.filter wait-data #(= (.-line_id %) line-id))
                       callback))
           ))

        (add-timeseries
          [line-id timeseries]
          "Add the timeseries for one line to the plot as a g wrapping a path"
          (get-line-data line-id #(draw-line-timeseries line-id %)))

        (remove-timeseries
          [line-id timeseries]
          "Remove the timeseries for one line to the plot"
          (.remove timeseries))

        (handle-click
          []
          "toggle either adding or removing the timeseries for one line"
          (let [this       (js* "this")
                line-id    (-> d3 (.select this) (.attr "id"))
                timeseries (.select d3 (line-path-id line-id true))]
            (if (.empty timeseries)
              (add-timeseries line-id timeseries)
              (remove-timeseries line-id timeseries) )))
       ]

      (-> d3
          (.selectAll ".key-line")
          (.on "click" handle-click))
  )))


(defn run-d3 []
  (println "in run-d3")
  (make-request
    "GET" "public/subway_wait_mean.json" true draw))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; dom manipulation code

(defn create-text [string]
  "create a textNode with content `string`"
  (.createTextNode js/document string))


(defn insert-at-bottom [new-elt parent-elt]
  "append a new child at the bottom of a parent element"
  (.appendChild parent-elt new-elt))


(defn create-elt
  "create an Element with tag `tag`.
  If `attr-map` and `text` are present, set attributes and
  text content accordingly"
  ([tag] (.createElement js/document tag))
  ([tag attr-map]
   (let [elt (create-elt tag)]
     (doseq [[k v] attr-map]
       (.setAttribute elt (name k) v))
     elt))
  ([tag attr-map text]
   (let [elt (create-elt tag attr-map)
         textNode (create-text text)]
     (insert-at-bottom textNode elt)
     elt)))


(defn set-up-divs []
  "Add the timeseries and key divs to an empty document"
  (let [body       (.-body js/document)
        timeseries (create-elt "div" {:id "timeseries"})
        key-div    (create-elt "div" {:id "key"})]
    (insert-at-bottom timeseries body)
    (insert-at-bottom key-div body)))


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
(add-load-func set-up-divs)
(add-load-func run-d3)
