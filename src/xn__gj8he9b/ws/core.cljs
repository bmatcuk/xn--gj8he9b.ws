(ns xn--gj8he9b.ws.core
  (:require [goog.events :as events]
            [goog.events.EventType]
            [xn--gj8he9b.ws.donutcat :as donutcat])
  (:import [goog.events EventType]))

(enable-console-print!)

(def initial-state {:width           (.-innerWidth js/window)
                    :height          (.-innerHeight js/window)
                    :resize-listener nil})

(defonce state (atom initial-state))

(defonce camera (js/THREE.PerspectiveCamera.))
(set! (.. camera -fov)         75)
(set! (.. camera -near)        0.1)
(set! (.. camera -far)         1000)
(set! (.. camera -position -z) 5)
(set! (.. camera -position -y) 1)

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

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
