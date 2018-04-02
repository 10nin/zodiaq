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
(defn html [res]
  (assoc res :headers {"Content-Type" "text/html; charset=utf-8"}))

(defn root-view [req]
  "<h1>Zodiaq how to use...</h1>
  <p>call ://uri/zodiac/yyyymmdd (yyyymmdd like to 20180301) <br>
  it return the zodiac of yyyymmdd day.</p>")

(defn zodiac-view [req]
  (if (date-format? req)
    (get-zodiac (t/month-day (t/local-date "yyyyMMdd" req)))
    (str req " is can't convert to zodiac.")))

(defn root-handler [req]
  (-> (root-view req)
      res/response
      html))

(defn zodiac-handler [req]
  (-> (zodiac-view req)
      res/response
      html))

(defroutes handlers
  (GET "/" req root-handler)
  (GET "/zodiac/:d" [d] (zodiac-handler d))
  (route/not-found "<h1>HTTP 404 : Not found</h1>"))

(defn match-route [uri]
  (get handlers uri))

(defn start-server [& {:keys [host port join?]
                       :or {host "localhost" port 3000 join? false}}]
  (let [port (if (string? port) (Integer/parseInt port) port)]
    (when-not @server
      (reset! server (server/run-jetty #'handlers {:host host :port port :join? join?})))))

(defn stop-server []
  (when @server
    (.stop @server)
    (reset! server nil)))

(defn restart-server []
  (when @server
    (stop-server)
    (start-server)))
