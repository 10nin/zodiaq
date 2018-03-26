(ns zodiaq.core
  (:require [clj-time.format :as f]
            [compojure.core :refer [defroutes context GET]]
            [compojure.route :as route]
            [ring.adapter.jetty :as server]
            [ring.util.response :as res]))

(defn root-handler [req]
  nil)

(defn zodiac-handler [req]
  nil)

(defroute handlers
  (GET "/" req root-handler)
  (GET ["/zodiac/:d" :d #"\d{4}\d{2}\d{2}"] req zodiac-handler)
  (route/not-found "<h1>HTTP 404 : Not found</h1>"))

(defn match-route [uri]
  (get handlers uri))

(defn get-zodiac [date]
  "Return the zodiac of date"
      nil)

(defn start-server []
  (when-not @server
    (reset! server (server/run-jetty #'handler {:port 3000 :join? false}))))

(defn stop-server []
  (when @server
    (.stop @server)
    (reset! server nil)))

(defn restart-server []
  (when @server
    (stop-server)
    (start-server)))
