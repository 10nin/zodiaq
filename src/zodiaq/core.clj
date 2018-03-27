(ns zodiaq.core
  (:require [compojure.core :refer [defroutes context GET]]
            [compojure.route :as route]
            [ring.adapter.jetty :as server]
            [ring.util.response :as res]
            [java-time :as t]))

(defonce server (atom nil))

(def zodiacs
  [{:zodiac "Aries"       :range {:from "0321", :to "0420"}},
   {:zodiac "Taurus"      :range {:from "0421", :to "0521"}},
   {:zodiac "Gemini"      :range {:from "0522", :to "0621"}},
   {:zodiac "Cancer"      :range {:from "0622", :to "0723"}},
   {:zodiac "Leo"         :range {:from "0724", :to "0823"}},
   {:zodiac "Virgo"       :range {:from "0824", :to "0923"}},
   {:zodiac "Libra"       :range {:from "0924", :to "1023"}},
   {:zodiac "Scorpio"     :range {:from "1024", :to "1122"}},
   {:zodiac "Sagittarius" :range {:from "1123", :to "1222"}},
   {:zodiac "Capricorn"   :range {:from "1223", :to "0120"}},
   {:zodiac "Aquarius"    :range {:from "0121", :to "0219"}},
   {:zodiac "Pisces"      :range {:from "0220", :to "0320"}}])

(defn get-zodiac [date]
  "Return the zodiac of date"
  nil)

(defn root-handler [req]
  nil)

(defn zodiac-handler [req]
  nil)

(defroutes handlers
  (GET "/" req root-handler)
  (GET ["/zodiac/:d" :d #"\d{4}\d{2}\d{2}"] req zodiac-handler)
  (route/not-found "<h1>HTTP 404 : Not found</h1>"))

(defn match-route [uri]
  (get handlers uri))

(defn start-server []
  (when-not @server
    (reset! server (server/run-jetty #'handlers {:port 3000 :join? false}))))

(defn stop-server []
  (when @server
    (.stop @server)
    (reset! server nil)))

(defn restart-server []
  (when @server
    (stop-server)
    (start-server)))
