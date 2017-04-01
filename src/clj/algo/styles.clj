(ns algo.styles
    (:require [garden.def :refer [defrule defstyles]]
              [garden.stylesheet :refer [rule]]))

(defstyles base
  [:.boxed
   {:border [[:1px :solid :red]]}]

  [:*
   {:box-sizing :border-box}]
  
  [:body :html :#app
   {:height :100%}]
  
  [:body
   {:background-color :white
    :font-family      "Arial"}]

  [:.section
   {:margin :10px}]
  
  [:h1
   {:font-size   :17pt
    :font-weight :bold}]

  [:.button
   {:cursor           :pointer
    :padding          :4px
    :margin-right     :4px
    :margin-top       :4px
    :margin-bottom    :4px
    :background-color :#dfe2d9}]

  [:.bars
   {:border        [[:1px :solid :black]]
    :margin-top    :10px
    :margin-bottom :20px
    :max-width     :600px
    :height        :200px}]
  
  [:.bar
   {:display    :inline-block
    :position   :relative}
   [:div
    {:width        :50%
     :height       :100%
     :position     :absolute
     :bottom       :0px
     :margin-left  :25%
     :margin-right :25%}]
   
   [:.active
    {:background-color :#77e06d}]

   [:.sorted
    {:background-color :#725f6c}]

   [:.unsorted
    {:background-color :#dfe2d9}]])
