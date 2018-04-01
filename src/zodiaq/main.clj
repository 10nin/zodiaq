(ns zodiaq.main
  (:require [zodiaq.core :as core])
  (:gen-class))

(defn -main[& {:as args}]
  (core/start-server))

