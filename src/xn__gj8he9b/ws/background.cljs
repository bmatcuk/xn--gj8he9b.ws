(ns xn--gj8he9b.ws.background)

(defonce donutcat "🍩😻")

(defonce background (.getElementById js/document "background"))

(defonce donutcat-dimensions (let [div (.createElement js/document "div")]
                               (set! (.. div -style -visibility) "hidden")
                               (set! (.. div -innerText) donutcat)
                               (.appendChild background div)
                               [(+ 1 (.-clientWidth div))
                                (+ 1 (.-clientHeight div)) ]))

(defn update-background [width height]
  (loop [child (.-lastChild background)]
    (when child
      (.removeChild background child)
      (recur (.-lastChild background))))

  (let [[dcwidth dcheight] donutcat-dimensions
        row (apply str (repeat (+ 1 (quot (+ dcwidth width) dcwidth)) donutcat))]
    (doseq [[y alt] (partition 2 (interleave (range 0 (+ dcheight height) dcheight) (iterate (partial bit-xor 1) 0)))]
      (let [div (.createElement js/document "div")]
        (.setAttribute div "style" (apply str ["top:" (str y) "px;left:" (str (* alt (/ dcwidth -2))) "px"]))
        (set! (.-innerText div) row)
        (.appendChild background div)))))

