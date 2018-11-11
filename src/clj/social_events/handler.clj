(ns social-events.handler
  (:require [compojure.core :refer [GET POST PUT routes defroutes]]
            [compojure.route :refer [resources]]
            [ring.util.response :refer [resource-response]]
            [ring.util.request :refer [body-string]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [social-events.resources.events :refer [fetch-events
                                                    fetch-event
                                                    create-event
                                                    update-event]]))

(defn wrap-header [app header value]
  (fn [request]
    (let [response (app request)]
      (assoc-in response [:headers header] value))))

(defn wrap-body-string [handler]
  (fn [request]
    (let [body-str (body-string request)]
      (handler (assoc request :body body-str)))))

(defn handler [mongodb-connection]
  (-> (routes (GET "/" [] (resource-response "index.html" {:root "public"}))
              (GET "/api/events" [] (fetch-events mongodb-connection))
              (GET "/api/events/:id" [id] (fetch-event mongodb-connection id))
              (POST "/api/events" [] (create-event mongodb-connection))
              (PUT "/api/events/:id" [id] (update-event mongodb-connection id))
              (resources "/"))
      (wrap-body-string)
      (wrap-header "Access-Control-Allow-Origin" "*")
      (wrap-header "Access-Control-Allow-Headers" "Origin, X-Requested-With, Content-Type, Accept")
      (wrap-header "Access-Control-Allow-Methods" "POST, GET, OPTIONS, DELETE, PUT")))

(defroutes dev-routes []
  (GET "/" [] (resource-response "index.html" {:root "public"}))
  (resources "/"))

(def dev-handler (-> #'dev-routes wrap-reload))