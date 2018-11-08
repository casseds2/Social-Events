(ns social-events.web-server
  (:require
    [clojure.tools.logging :refer [info error]]
    [clojure.spec.alpha :as spec]
    [com.stuartsierra.component :as component]
    [ring.adapter.jetty :refer [run-jetty]]
    [social-events.handler :refer [handler]]))

(spec/def ::host string?)
(spec/def ::port integer?)
(spec/def ::web-server (spec/keys :req-un [::host
                                           ::port]))

(defrecord WebServer [config]
  component/Lifecycle
  (start [component]
    (info "Starting web-server")
    (let [{:keys [host port]} (:web-server config)
          mongodb-connection (-> component :mongodb :connection)]
      (->> (run-jetty (handler mongodb-connection) {:host host :port port})
           (assoc component :web-server))))
  (stop [component]
    (info "Stopping the web-server")
    (assoc component :web-server nil)))