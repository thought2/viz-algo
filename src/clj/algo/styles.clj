(ns algo.styles
    (:require [garden.def :refer [defrule defstyles]]
              [garden.stylesheet :refer [rule]]))

(defstyles base
  [:body :html :#app {:height "100%"}]
  
  [:body {:background-color :white
          :font-family "Arial"}]

  [:h1 {:font-size "17pt"
        :font-weight :bold}]

  [:.boxed {:border "1px solid red"}])
