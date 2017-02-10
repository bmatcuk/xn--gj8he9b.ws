(ns xn--gj8he9b.ws.donutcat)

(defonce state (atom {:donut-geometry nil
                      :donut-material nil
                      :icing-geometry nil
                      :icing-material nil
                      :decal-geometry nil
                      :decal-material nil
                      :cat-texture    nil
                      :donut-radius   nil
                      :donuts         #{}}))

(defonce json-loader (js/THREE.JSONLoader.))
(defonce texture-loader (js/THREE.TextureLoader.))

(defn load-donut []
  (.load json-loader "models/donut.json"
         ; Second parameter is an array of materials,
         ; but we know there's only one in the file
         (fn [geometry [material & _]]
           (let [buffered (.fromGeometry (js/THREE.BufferGeometry.) geometry)]
             ; There's a bug in the JSONLoader where [1,1] gets encoded [[1,1], [1,1]]
             (set! (.-normalScale material) (js/THREE.Vector2. 1 1))

             (swap! state assoc :donut-geometry buffered :donut-material material)))))

(defn load-icing []
  (.load json-loader"models/icing.json"
         ; Second parameter is an array of materials,
         ; but we know there's only one in the file
         (fn [geometry [material & _]]
           (let [buffered (.fromGeometry (js/THREE.BufferGeometry.) geometry)]
             ; Fix some more bugs in the JSONLoader
             (set! (.-bumpMap material) nil)
             (set! (.-bumpScale material) 2)

             (swap! state assoc :icing-geometry geometry :icing-material material)))))

(defn load-decal []
  (.load texture-loader "models/cat.png"
         (partial swap! state assoc :cat-texture)))

(defn set-name [object3d n]
  (set! (.-name object3d) n)
  object3d)

(defn make-donutcat []
  (let [donut-geometry (@state :donut-geometry)
        donut-material (@state :donut-material)
        icing-geometry (@state :icing-geometry)
        icing-material (@state :icing-material)
        decal-geometry (@state :decal-geometry)
        decal-material (@state :decal-material)
        donut (js/THREE.Mesh.)
        icing (js/THREE.Mesh.)
        decal (js/THREE.Mesh.)]
    (when donut-geometry (set! (.-geometry donut) donut-geometry))
    (when donut-material (set! (.-material donut) donut-material))
    (when icing-geometry (set! (.-geometry icing) icing-geometry))
    (when icing-material (set! (.-material icing) icing-material))
    (when decal-geometry (set! (.-geometry decal) decal-geometry))
    (when decal-material (set! (.-material decal) decal-material))
    (doto (js/THREE.Group.)
      (.add (set-name donut "donut"))
      (.add (set-name icing "icing"))
      (.add (set-name decal "decal"))
      (->> (swap! state update-in [:donuts] conj)))))

(defn remove-donutcat [dc]
  (swap! state update-in [:donuts] disj dc))

(defn get-donutcat-radius [] (@state :donut-radius))

(defn update-donuts [donuts mesh-name property value]
  (let [meshes (map #(.getObjectByName % mesh-name) donuts)]
    (doseq [donut meshes] (aset donut property value))))

(defn build-decal-material [icing-material diffuse]
  (let [material (js/THREE.MeshPhongMaterial.)]
    (when icing-material (.copy material icing-material))
    (.setValues material #js {
                     :color 0xffffff
                     :specular 0xffffff
                     :map diffuse
                     :transparent true
                     :depthTest true
                     :depthWrite false
                     :polygonOffset true
                     :polygonOffsetFactor -4
                     :wireframe false })
    (swap! state assoc :decal-material material)))

(add-watch state :donuts
           (fn [_ _ old-state new-state]
             (when (not= (old-state :donut-geometry) (new-state :donut-geometry))
               (update-donuts (new-state :donuts) "donut" "geometry" (new-state :donut-geometry)))
             (when (not= (old-state :donut-material)  (new-state :donut-material))
               (update-donuts (new-state :donuts) "donut" "material" (new-state :donut-material)))
             (when (not= (old-state :icing-geometry) (new-state :icing-geometry))
               (update-donuts (new-state :donuts) "icing" "geometry" (new-state :icing-geometry))
               (let [icing-geometry (new-state :icing-geometry)
                     mesh (js/THREE.Mesh. icing-geometry)
                     box (.setFromObject (js/THREE.Box3.) mesh)
                     position (.getCenter box)
                     rotation (js/THREE.Vector3. (/ js/Math.PI 2) 0 0)
                     dimensions-x (- (.. box -max -x) (.. box -min -x))
                     dimensions-y (- (.. box -max -z) (.. box -min -z))
                     dimensions-z (- (.. box -max -y) (.. box -min -y))
                     dimensions (js/THREE.Vector3.
                                  dimensions-x
                                  dimensions-y
                                  dimensions-z)
                     check (js/THREE.Vector3. 1 1 1)
                     decal-geometry (js/THREE.DecalGeometry. mesh position rotation dimensions check)
                     radius (/ (max dimensions-x dimensions-y) 2)]
                 (swap! state assoc :decal-geometry decal-geometry :donut-radius radius)))
             (when (not= (old-state :icing-material)  (new-state :icing-material))
               (update-donuts (new-state :donuts) "icing" "material"  (new-state :icing-material))
               (build-decal-material (new-state :icing-material)  (new-state :cat-texture)))
             (when (not= (old-state :decal-geometry) (new-state :decal-geometry))
               (update-donuts (new-state :donuts) "decal" "geometry" (new-state :decal-geometry)))
             (when (not= (old-state :decal-material)  (new-state :decal-material))
               (update-donuts (new-state :donuts) "decal" "material"  (new-state :decal-material)))
             (when (not= (old-state :cat-texture)  (new-state :cat-texture))
               (build-decal-material (new-state :icing-material) (new-state :cat-texture)))))

(defonce load-models
  (do
    (load-donut)
    (load-icing)
    (load-decal)))

