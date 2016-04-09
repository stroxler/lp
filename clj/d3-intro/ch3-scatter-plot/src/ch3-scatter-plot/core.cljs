(ns ch3-scatter-plot.core
  (:require [clojure.browser.repl :as cbrepl]))


(def d3 js/d3)
(def format clojure.core/format)

; What's shown here?
;   - using scales (remember the range and domain inputs
;     must be arrays!) and extents
;   - I think using margins the way this author is is
;     not the preferred way of embedding a plot inside
;     an svg. Although it works, I think it's more common
;     to put a new g element (like a div) inside the outer
;     one.
;   - if you get a TypeError: Cannot read property 'call'
;     of undefined, it likely means that you are trying to
;     use a function that isn't in your namespace (unfortunately
;     this is a case where clojurescript's host language shines
;     through, giving a not-very-useful-to-newbies error)
;   - if you get that error in a chain of d3 calls, it may mean
;     that the *preceding* call has some kind of error, which
;     caused it to not return.
;   - the transform attribute is really good to understand.
;     There are a few key ideas I learned trying to debug here:
;       * as you might imagine, the transforms are applied in
;         order, so rotate => translate != translate => rotate
;       * you need to remember that negative y is up, not down,
;         and also that positive rotation is clockwise
;       * any transform is relative to the current coordinate
;         system. This means, e.g.
;           * if you've rotated 90 degrees, then to move right
;             you need to go in the negative y direction
;           * if you rotate(90, 0, 0), it will swing down, with
;             its hinge as whatever the top-left corner is
;             *after* applying any previous transforms

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
  (let [height          500
        width           1000
        margin          75
        svg             (-> d3 (.select "body")
                               (.append "svg")
                               (.attr "width" width)
                               (.attr "height" height)
                               (.attr "id" "chart"))
        selection       (-> svg (.selectAll "circle")
                                (.data data))
        x-extent        (.extent d3 data #(.-collision_with_injury %))
        y-extent        (.extent d3 data #(.-dist_between_fail %))
        x-scale         (-> d3 (.-scale) (.linear)
                               (.range #js [margin (- width margin)])
                               (.domain x-extent))
        y-scale         (-> d3 (.-scale) (.linear)
                               (.range #js [(- height margin) margin])
                               (.domain y-extent))
        x-loc           (fn [d] (-> d (.-collision_with_injury) (x-scale)))
        y-loc           (fn [d] (-> d (.-dist_between_fail) (y-scale)))
        ]
    (-> selection (.enter) (.append "circle") (.attr "r" 5))
    (-> selection (.exit) (.remove))
    (-> selection
        (.attr "cx" x-loc)
        (.attr "cy" y-loc))
    (let
      [class-name "y axis"
       class-sel  ".y.axis"
       title      "mean distance between failures (miles)"
       axis-f     (-> d3 (.-svg) (.axis) (.scale y-scale) (.orient "left"))
       g-transf   (svg-translate margin 0)
       t-transf   (str (svg-translate (- (* margin 0.75)) (- height (* 2 margin))) " "
                       (svg-rotate (- 90) 0 0))]
      (println t-transf)
      (-> d3
          (.select "#chart")
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
    (let
      [class-name "x axis"
       class-sel  ".x.axis"
       title      "collisions with injury (per million miles)"
       axis-f     (-> d3 (.-svg) (.axis) (.scale x-scale))
       g-transf   (svg-translate 0 (- height margin))
       t-transf   (svg-translate (- (/ width 2) margin) (/ margin 2))]
      (-> d3
          (.select "#chart")
          (.append "g")
          (.attr "class" class-name)
          (.attr "transform" g-transf)
          (.call axis-f))
      (-> d3
          (.select class-sel)
          (.append "text")
          (.text title)
          (.attr "transform" t-transf))
   )))



(defn run-d3 []
  (println "in run-d3")
  (make-request
    "GET" "public/bus_perf.json" true draw))


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
