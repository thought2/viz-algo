(ns algo.start
    (:gen-class)
    (:require [algo.server          :refer [handler]]
              [ring.adapter.jetty       :refer [run-jetty]]))

(defn -main [& [port]]
  (run-jetty handler {:port (if port (Integer. port) 3000)}))
