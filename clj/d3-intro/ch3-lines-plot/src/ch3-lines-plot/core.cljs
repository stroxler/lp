(ns ch3-lines-plot.core
  (:require [clojure.browser.repl :as cbrepl]))


(def d3 js/d3)
(def format clojure.core/format)

; What's shown here?
;   - creating and using a time scale (the data should
;     be in millis)
;   - my handling of the axes is slightly better than the
;     last example, although still not that great
;   - using d3 line to add paths to a plot
;   - using css and classes to color data differently
;     depending on its origin

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
  (let [outer-height    500
        outer-width     1000
        margin          75
        width           (- outer-width (* 2 margin))
        height          (- outer-height (* 2 margin))
        outer-chart     (-> d3
                            (.select "body")
                            (.append "svg")
                            (.attr "width" outer-width)
                            (.attr "height" outer-height)
                            (.attr "class" "outer-chart"))
        chart           (-> outer-chart
                            (.append "g")
                            (.attr "class" "chart")
                            (.attr "transform" (svg-translate margin 0)))
        times-square    (-> chart
                            (.selectAll "circle.times_square")
                            (.data (.-times_square data))
                            (.enter)
                            (.append "circle")
                            (.attr "class" "times-square"))
        grand-central   (-> chart
                            (.selectAll "circle.grand_central")
                            (.data (.-grand_central data))
                            (.enter)
                            (.append "circle")
                            (.attr "class" "grand-central"))
        all-data        (.concat (.-grand_central data) (.-times_square data))
        y-extent        (.extent d3 all-data #(.-count %))
        x-extent        (.extent d3 all-data #(.-time %))
        y-scale         (-> d3 (.-scale) (.linear)
                               (.range #js [height 0])
                               (.domain y-extent))
        x-scale         (-> d3 (.-time) (.scale)
                               (.range #js [0 width])
                               (.domain x-extent))
        y-loc           (fn [d] (-> d (.-count) (y-scale)))
        x-loc           (fn [d] (-> d (.-time) (x-scale)))
        selection       (.selectAll chart "circle")
        ]
    ; NOTE: this code isn't following enter / exit / update very well at all.
    (-> selection
        (.attr "r" 3)
        (.attr "cx" x-loc)
        (.attr "cy" y-loc))
    ; add the y axis
    (let
      [class-name "y axis"
       class-sel  ".y.axis"
       title      "mean number of turnstile revolutions"
       axis-f     (-> d3 (.-svg) (.axis) (.scale y-scale) (.orient "left"))
       g-transf   (svg-translate margin 0)
       t-transf   (str (svg-translate (- (* margin .65)) (- height margin)) " "
                       (svg-rotate (- 90) 0 0))
       ]
      (-> d3
          (.select ".outer-chart")
          (.append "g")
          (.attr "class" class-name)
          (.attr "transform" g-transf)
          (.call axis-f))
      (-> d3
          (.select class-sel)
          (.append "text")
          (.text title)
          (.attr "transform" t-transf))
      )
    ; add the x axis
    (let
      [class-name "x axis"
       class-sel  ".x.axis"
       title      "time"
       axis-f     (-> d3 (.-svg) (.axis) (.scale x-scale))
       g-transf   (svg-translate margin height)
       t-transf   (svg-translate (/ width 2) (/ margin 2))]
      (-> d3
          (.select ".outer-chart")
          (.append "g")
          (.attr "class" class-name)
          (.attr "transform" g-transf)
          (.call axis-f))
      (-> d3
          (.select class-sel)
          (.append "text")
          (.text title)
          (.attr "transform" t-transf))
      )
    ; add the lines to what would otherwise be a scatterplot
    (let
      [line-func    (-> d3 (.-svg) (.line) (.x x-loc) (.y y-loc))]
      (-> chart
          (.append "path")
          (.attr "d" (line-func (.-times_square data)))
          (.attr "class" "times-square"))
      (-> chart
          (.append "path")
          (.attr "d" (line-func (.-grand_central data)))
          (.attr "class" "grand-central"))
      )
    ))



(defn run-d3 []
  (println "in run-d3")
  (make-request
    "GET" "public/turnstile_traffic.json" true draw))


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
