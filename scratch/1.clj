((fn aux [m [x & xs]]
     (lazy-seq
      (let [m' (f m x)]
	(cons m'
	      (when (seq xs) (aux m' xs))))))
 m xs)


(defn Box' [{:keys [on? color]} childs]
  [:span {:style {:border (join-space ["3px" "solid" (if on? color "white")])}}
   childs]  )


(defn f [xs]
  (let [n (count xs)
        as (for [i (range (dec n))
                 j (range (inc i) 0 -1)]
             {:i i
              :j j})]
    (scan (fn [m {:keys [i j]}]
            (let [j' (dec j)]
              (if (< (nth xs j) (nth xs j'))
                (swap-item m j j')
                m)))
          xs as)))


(go
      (let [wait #(timeout 3000)
            reg! #(swap! state assoc %1 %2)]
        (doseq [i (range (dec n))] 
          (reg! :i i)
          (<! (wait))
          (doseq [j (range (inc i) 0 -1)]
            (reg! :j j)
            (<! (wait))
            (let [xs (@state :xs)
                  this j
                  prev (dec j)]
              (when (< (nth xs this) (nth xs prev))
                (.log js/console (prn-str xs))
                (swap! state update :xs
                       (fn [xs]
                         (swap-item xs this prev)))
                (.log js/console (prn-str xs))
                (<! (wait))))))))


(map-indexed (fn [i' x]
               [:span {:style {:padding "5px" 
                               :background-color 
                               (if (<= i' i)
                                 :red :white)}}
                [:span {:style {:background-color
                                (when (or (= i' (dec j)) (= i' j))
                                  :green)}}
                 [:div {:style {:display :inline-block
                                :text-align :center}}
                  [:div {:style {:font-size "10px"}} i']
                  [:div x]]]]
               )
             xs)

(defn insert-sort-states [xs]
  (let [inidices (for [i (range (dec n))
                       j (range (inc i) 0 -1)]
                   {:i i
                    :j j})]
    (reductions (fn [{:keys [xs]} {:keys [i j]}]
                  (let [j-prev (dec j)]
                    {:i i
                     :j j
                     :xs (if (< (nth xs j) (nth xs j-prev))
                           (swap-item xs j j-prev)
                           xs)}))
                (assoc (first indices)
                       :xs xs)
                (rest inidices))))
