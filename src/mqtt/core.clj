(ns mqtt
  (:gen-class)
  (:require [clojurewerkz.machine-head.client :as mh]
            [compojure.core    :refer [defroutes GET]]
            [compojure.handler :refer [site]]
            [compojure.route   :as route]
            [ring.adapter.jetty :as jetty]
            [environ.core      :refer [env]]))

(def mqtt-url (get (System/getenv) "CLOUDMQTT_URL" "tcp://127.0.0.1:1833"))

(defroutes app
           (GET "*" []
                (let [id   "Clojure"
                      conn (mh/connect {:uri mqtt-url} id)]
                  (mh/subscribe conn ["hello"] (fn [^String topic _ ^bytes payload]
                                                 (println (String. payload "UTF-8"))))
                  (mh/publish conn "hello" "Hello, world")
                  {:status 200
                   :headers {"Content-Type" "text/plain"}
                   :body "Published message!"})))

(defn -main
  [& args]
  (let [id   "Clojure"
        conn (mh/connect {:uri mqtt-url} id)]
    (mh/subscribe conn ["hello"] (fn [^String topic _ ^bytes payload]
                                   (println (String. payload "UTF-8"))))
    (mh/publish conn "hello" "Hello, world")))")))