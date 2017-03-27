(ns algo.core
  (:require
   [reagent.core :as r]
   [clojure.string :as s]
   [cljs.core.async :refer [put! <! chan timeout]
    :as as]) 
  (:require-macros
   [cljs.core.async.macros  :refer [go]]))


(defonce xs (vec (repeatedly 40 #(rand-int 100))))
(defonce n (count xs))
(def set-timeout #(.setTimeout js/window %1 %2))
(def by-id #(.getElementById js/document %))
(def perc #(str % "%"))
(def last-i #(dec (count %)))
(def zip (partial map vector))
(def zip-range (partial zip (range)))

(defn swap-shorthands [state]
  [(partial swap! state assoc)
   (partial swap! state update)
   (partial swap! state)])

(defn join-space [xs]
  (s/join " " xs))

(defn swap-item [xs n1 n2]
  (let [t (nth xs n1)]
    (assoc xs
           n1 (nth xs n2)
           n2 t)))

(defn insertion-sort-steps [xs]
  (let [n (count xs)
        i (atom 0)
        j (atom 1)
        xs (atom xs)
        steps (atom [])]
    (doseq [i' (range (dec n))]
      (reset! j (inc i'))
      (while (do (swap! steps conj {:i @i
                                    :j @j
                                    :xs @xs})
                 (and (>= @j 1)
                      (< (@xs @j) (@xs (dec @j))))) 
        (swap! xs swap-item  @j (dec @j))
        (swap! j dec))
      (reset! i i'))
    
    (concat [{:xs (-> @steps first :xs)}]
            @steps
            [{:xs (-> @steps last :xs)
              :i (dec n)}])))


(defn Button [{:keys [label event]}]
  [:span {:on-click event
          :style {:cursor :pointer
                  :padding 4
                  :margin-right 4
                  :margin-top 4
                  :margin-bottom 4
                  :background-color :#dfe2d9}}
   label])


(defn Bar [{:keys [mode percent ]} n]
  [:div {:style {:display :inline-block
                 :text-align :center 
                 :height :100%
                 :width (perc (/ 100 n))
                 :position :relative}}
   [:div {:style {:height (perc percent)
                  :width :5%
                  :background-color
                  (condp = mode
                    :active :#77e06d
                    :sorted :#725f6c
                    :unsorted :#dfe2d9)
                  :position :absolute
                  :bottom 0
                  :left 0 
                  :padding-left :25%
                  :padding-right :25%
                  :font-size 10}}]])


(defn Bars [{:keys [xs]}]
  (let [n (count xs)]
    [:div {:style {:border "1px solid black"
                   :margin-top 10
                   :margin-bottom 20
                   :max-width 600
                   :height 200}}
     (map-indexed
      (fn [i' x]
        ^{:key i'} [Bar x n])
      xs)]))


(defonce InsertSortState (r/atom {:pos 0
                                  :running false
                                  :speed 200}))

(defn InsertSort []
  (let [state InsertSortState
        steps (insertion-sort-steps xs)
        [a! u!] (swap-shorthands state)
        pause #(a! :running false) 
        reset (fn [] (pause) (a! :pos 0))

        pos!
        (fn [x] (u! :pos #(mod (+ % x) (count steps)))) 
        
        next-tick
        (fn aux []
          (let [{:keys [running speed pos]} @state]
            (set-timeout
             (fn []
               (if (and (not= pos (last-i steps))
                        running)
                 (do (pos! 1)
                     (aux))
                 (pause)))
             speed)))
        
        start
        (fn []
          (let [{:keys [pos running]} @state]
            (when (= pos (last-i steps))
              (a! :pos 0))
            (when (= running false)
              (a! :running true)
              (next-tick))))]
    
    (fn []
      (let [{:keys [pos]} @state
            {:keys [i j xs]} (nth steps pos)]
        [:div {:style {:margin 10}}
         [:h1 "Insert Sort"]
         [:div
          [Bars
           {:xs
            (for [[i' x] (zip-range xs)]
              {:percent x
               :mode
               (cond (and i j (<= (dec j) i' j)) :active
                     (and i (<= i' i)) :sorted
                     :else :unsorted)})}]
          [:div
           (map (fn [[l e]]
                  [Button {:label l
                           :event e}])
                [["Play" start]
                 ["Pause" pause]
                 ["Reset" reset]
                 ["Next" #(pos! 1)]
                 ["Previous" #(pos! -1)]])]]]))))


(defn Page []
  [:div
   [InsertSort]])

(defn main []
  (r/render [Page] (by-id "app")))

(defn init []
  (main))
