(ns social-events.mongodb
  (:require
    [clojure.tools.logging :refer [info error]]
    [clojure.spec.alpha :as spec]
    [com.stuartsierra.component :as component]
    [monger.core :as monger]))

(spec/def ::host string?)
(spec/def ::port integer?)
(spec/def ::dbname string?)
(spec/def ::mongodb (spec/keys :req-un [::host
                                        ::port
                                        ::dbname]))

(defrecord Mongodb [config]
  component/Lifecycle
  (start [component]
    (info "Starting mongodb connection")
    (let [{:keys [host port dbname]} (:mongodb config)
          connection (-> (monger/server-address host port)
                         (monger/connect)
                         (monger/get-db dbname))]
      (assoc component :connection connection)))
  (stop [component]
    (info "Stopping mongodb connection")
    (assoc component :connection nil)))