(ns social-events.handler
  (:require [compojure.core :refer [GET routes defroutes]]
            [compojure.route :refer [resources]]
            [ring.util.response :refer [resource-response]]
            [ring.middleware.reload :refer [wrap-reload]]
            [social-events.resources.events :refer [fetch-events]]))

(defn wrap-header [app header value]
  (fn [request]
    (let [response (app request)]
      (assoc-in response [:headers header] value))))

(defn handler [mongodb-connection]
  (-> (routes (GET "/" [] (resource-response "index.html" {:root "public"}))
              (GET "/api/events" [] (fetch-events mongodb-connection))
              (resources "/"))
      (wrap-header "Access-Control-Allow-Origin" "*")
      (wrap-header "Access-Control-Allow-Headers" "Origin, X-Requested-With, Content-Type, Accept, Authorization")
      (wrap-header "Access-Control-Allow-Methods" "POST, GET, OPTIONS, DELETE, PUT")))

;;TODO Wrap Routes with MongoDB connection in figwheel reload
(defroutes dev-routes []
  (GET "/" [] (resource-response "index.html" {:root "public"}))
  (resources "/"))

(def dev-handler (-> #'dev-routes wrap-reload))