(ns social-events.core
  (:require
    [clojure.tools.logging :refer [info error]]
    [com.stuartsierra.component :as component]
    [social-events.config :as config]
    [social-events.mongodb :as mongodb]
    [social-events.web-server :as web-server]
    [clojure.tools.cli :as cli])
  (:gen-class))

(def system nil)

(defn- create-system [config-path]
  (component/system-map
    :config (config/map->Configuration {:config-path config-path})
    :mongodb (component/using (mongodb/->Mongodb {}) [:config])
    :web-server (component/using (web-server/->WebServer {}) [:config :mongodb])))

(defn- init [config-path]
  (alter-var-root #'system (fn [_]
                             (create-system config-path))))

(defn stop []
  (info "Stopping Social Events")
  (alter-var-root #'system
                  (fn [sys]
                    (if sys
                      (component/stop sys)
                      (error "System not initialised. Stop failed.")))))

(defn start []
  (info "Starting Social Events")
  (if system
    (try
      (alter-var-root #'system component/start)
      (catch Exception e
        (error e "Could not start system")
        (stop)))
    (error "System not initialised")))

(defn add-shutdown-hook []
  (.addShutdownHook (Runtime/getRuntime)
                    (Thread. (fn []
                               (do (info "Initiated shutdown")
                                   (stop))))))

(defn add-uncaught-exception-handler []
  (Thread/setDefaultUncaughtExceptionHandler
    (reify Thread$UncaughtExceptionHandler
      (uncaughtException [_ thread ex]
        (error ex "Uncaught exception on" (.getName thread))
        (System/exit 1)))))

(def cli-options
  [["-c" "--config-path CONFIG-PATH" "Path to config.edn"
    :default "resources/config.edn"]])

(defn -start [this]
  (add-shutdown-hook)
  (start))

(defn -stop [this]
  (stop))

(defn -main [& args]
  (add-uncaught-exception-handler)
  (try
    (let [{:keys [config-path]} (:options (cli/parse-opts args cli-options))]
      (init config-path)
      (start))
    (catch Exception e
      (error e "Exception thrown in main"))))