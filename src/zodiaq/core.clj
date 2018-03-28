(ns zodiaq.core
  (:require [compojure.core :refer [defroutes context GET]]
            [compojure.route :as route]
            [ring.adapter.jetty :as server]
            [ring.util.response :as res]
            [java-time :as t]))

(defonce server (atom nil))

;;-- main routine part
;; defined: from < taget-day < to
(def zodiacs
  [{:zodiac "Aries"       :from "--03-20", :to "--04-21"},
   {:zodiac "Taurus"      :from "--04-20", :to "--05-22"},
   {:zodiac "Gemini"      :from "--05-21", :to "--06-22"},
   {:zodiac "Cancer"      :from "--06-21", :to "--07-24"},
   {:zodiac "Leo"         :from "--07-23", :to "--08-24"},
   {:zodiac "Virgo"       :from "--08-23", :to "--09-24"},
   {:zodiac "Libra"       :from "--09-23", :to "--10-24"},
   {:zodiac "Scorpio"     :from "--10-23", :to "--11-23"},
   {:zodiac "Sagittarius" :from "--11-22", :to "--12-23"},
   {:zodiac "Capricorn"   :from "--12-22", :to "--01-21"},
   {:zodiac "Aquarius"    :from "--01-20", :to "--02-20"},
   {:zodiac "Pisces"      :from "--02-19", :to "--03-21"}])

(defn zodiac- [^java.time.MonthDay d z]
  "Find zodiac of d, and return the zodiac from 'zodiacs' map."
  (if (t/before? (t/month-day (:from z)) d (t/month-day (:to z)))
    (:zodiac z)))

(defn get-zodiac [^java.time.MonthDay d]
  "Return the zodiac of d"
  (if (t/month-day? d)
    (first (filter some? (map #(zodiac- d %) zodiacs)))))

(defn date-format? [^String d]
  "Valid target format of d"
  (try
    (t/local-date "yyyyMMdd" d) true
    (catch Exception _ false)))

;; web-service part
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
