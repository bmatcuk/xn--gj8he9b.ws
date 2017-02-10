(ns xn--gj8he9b.ws.core
  (:require [goog.events :as events]
            [goog.events.EventType]
            [xn--gj8he9b.ws.donutcat :as donutcat])
  (:import [goog.events EventType]))

(enable-console-print!)

(def donutcat-chance 0.05)
(def velocity-min    5)
(def velocity-max    10)

(defonce state (atom {:width           (.-innerWidth js/window)
                      :height          (.-innerHeight js/window)
                      :resize-listener nil
                      :animation-frame nil
                      :donutcats       {}}))

(defonce camera (js/THREE.PerspectiveCamera.))
(set! (.. camera -fov)         60)
(set! (.. camera -near)        0.1)
(set! (.. camera -far)         25)

(defonce renderer (doto (js/THREE.WebGLRenderer.)
                    (#(.appendChild (.-body js/document) (.-domElement %)))))

(defn resize! [& _]
  (let [width (.-innerWidth js/window)
        height (.-innerHeight js/window)
        aspect (/ width height)]
    (set! (.-aspect camera) aspect)
    (.updateProjectionMatrix camera)
    (.setSize renderer width height)
    (swap! state assoc :width width :height height)))
(when-some [id (:resize-listener @state)] (events/unlistenByKey id))
(let [id (events/listen js/window EventType/RESIZE resize!)]
  (swap! state assoc :resize-listener id))
(resize!)

(defonce ambient-light (js/THREE.AmbientLight.))
(set! (.-color ambient-light) (js/THREE.Color. 0x444444))

(defonce point-light (js/THREE.PointLight.))
(set! (.-color point-light)     (js/THREE.Color. 0xffffff))
(set! (.-intensity point-light) 1.25)
(set! (.-distance point-light)  1000)
(.set (.-position point-light) 0 0 600)

(defonce scene (doto (js/THREE.Scene.)
                 (.add ambient-light)
                 (.add point-light)))

; Assumes the camera is pointed in the negative z direction
(defn get-z-extents []
  (let [camera-z (.. camera -position -z)]
    [(- camera-z (.-near camera))
     (- camera-z (.-far camera))]))

(defn degrees-to-radians [degrees]
  (* js/Math.PI (/ degrees 180)))

; Assumes the camera is pointed in the negative z direction
(defn get-xy-extents [z]
  (let [camera-x      (.. camera -position -x)
        camera-y      (.. camera -position -y)
        camera-z      (.. camera -position -z)
        distance      (- camera-z z)
        height        (* (js/Math.tan (degrees-to-radians (/ (.-fov camera) 2))) distance)
        width         (* (.-aspect camera) height)]
    [(- camera-x width)
     (+ camera-x width)
     (- camera-y height)
     (+ camera-y height)]))

(defn rand-range [mn mx] (+ (rand (- mx mn)) mn))

(defn add-donutcat [ts]
  (let [[min-z max-z] (get-z-extents)
        dc (donutcat/make-donutcat)
        z (rand-range (+ min-z (* 0.2 (- max-z min-z))) max-z)
        [min-x max-x min-y max-y] (get-xy-extents z)
        from-left (< (rand) 0.5)
        ; little padding so it isn't immediately marked off-screen
        x (if from-left (+ min-x 0.1) (- max-x 0.1))
        y (rand-range min-y max-y)
        v (rand-range velocity-min velocity-max)
        v (if from-left v (- v))]
    (.set (.-position dc) x y z)
    (set! (.. dc -rotation -x) (/ js/Math.PI 2.0))
    (.add scene dc)
    (swap! state update-in [:donutcats] assoc dc {:velocity  v
                                                  :initial-x x
                                                  :start     ts})))

; Assumes the camera is pointed in the negative z direction
(defn in-frustum? [vector3]
  (let [x (.-x vector3)
        y (.-y vector3)
        z (.-z vector3)
        [min-x max-x min-y max-y] (get-xy-extents z)
        [min-z max-z] (get-z-extents)]
    (and (<= min-x x max-x)
         (<= min-y y max-y)
         (>= min-z z max-z))))

(defn remove-donutcat [model]
  (donutcat/remove-donutcat model)
  (swap! state update-in [:donutcats] dissoc model)
  (.remove scene model))

(defn render [ts]
  (let [animation-frame (.requestAnimationFrame js/window render)]
    (swap! state assoc :animation-frame animation-frame))

  (if (< (rand) donutcat-chance) (add-donutcat ts))

  (let [radius (- (donutcat/get-donutcat-radius))]
    (doseq [[model {:keys [velocity initial-x start]}] (@state :donutcats)]
      (let [distance (* velocity (/ (- ts start) 1000.0))]
        (set! (.. model -position -x) (+ initial-x distance))
        (set! (.. model -rotation -y) (/ distance radius))
        (when (not (in-frustum? (.-position model)))  (remove-donutcat model)))))

  (.render renderer scene camera))

(let [old-animation-frame (@state :animation-frame)
      animation-frame (.requestAnimationFrame js/window render)]
  (when old-animation-frame (.cancelAnimationFrame js/window old-animation-frame))
  (swap! state assoc :animation-frame animation-frame))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
