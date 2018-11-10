(ns social-events.config
  (:require
    [clojure.tools.logging :refer [info error]]
    [aero.core :as aero]
    [clojure.spec.alpha :as spec]
    [clojure.pprint :as pprint]
    [social-events.web-server :as web-server]
    [social-events.mongodb :as mongodb]
    [com.stuartsierra.component :as component]))

(spec/def ::config (spec/keys :req-un [::web-server/web-server
                                       ::mongodb/mongodb]))

(defn- conform [config spec]
  (let [conformed (spec/conform spec config)]
    (if (= ::spec/invalid conformed)
      (let [reason (spec/explain spec config)]
        (error "Invalid configuration" (with-out-str (pprint/pprint reason)))
        (throw (ex-info "Invalid configuration supplied" reason)))
      conformed)))

(defrecord Configuration [config-path
                          web-server
                          mongodb]
  component/Lifecycle
  (start [component]
    (info "Config Path" config-path)
    (let [config (-> (aero/read-config config-path)
                     (conform ::config))]
      (info "Starting with config:\n"
            (with-out-str (pprint/pprint (select-keys config [:web-server :mongodb]))))
      (merge component (select-keys config [:web-server :mongodb]))))
  (stop [component]
    (assoc component :web-server nil :mongodb nil)))