(ns social-events.resources.events
  (:require
    [monger.collection :as mc]
    [clojure.spec.alpha :as spec]
    [liberator.core :refer [defresource]])
  (:import (org.bson.types ObjectId)))

(spec/def ::title string?)
(spec/def ::description string?)
(spec/def ::creator string?)
(spec/def ::date-string string?)
(spec/def ::participants (spec/coll-of string?))
(spec/def ::event (spec/keys :req-un [::title
                                      ::description
                                      ::creator
                                      ::date-string
                                      ::participants]))

(defn- convert-id-to-string [doc]
  (assoc doc :_id (str (:_id doc))))

(defn- convert-ids-to-string [coll]
  (mapv (fn [doc]
          (convert-id-to-string doc)) coll))

(defn- extract-id-as-key [docs]
  (reduce (fn [coll val]
            (let [id (:_id val)]
              (assoc coll id val))) {} docs))

(defresource fetch-events [mongodb-connection]
             :available-media-types ["application/edn" "application/json"]
             :allowed-methods [:get]
             :handle-ok (fn [_]
                          (-> (mc/find-maps mongodb-connection "events")
                              (convert-ids-to-string)
                              (extract-id-as-key))))

(defresource fetch-event [mongodb-connection id]
             :available-media-types ["application/edn" "application/json"]
             :allowed-methods [:get]
             :handle-ok (fn [_]
                          (-> (mc/find-map-by-id mongodb-connection "events" (ObjectId. id))
                              (convert-id-to-string))))

(defresource create-event [mongodb-connection]
             :available-media-types ["application/edn" "application/json"]
             :allowed-methods [:post]
             :post! (fn [ctx]
                      (let [body (-> (get-in ctx [:request :body])
                                     (clojure.edn/read-string))]
                        (->> (mc/insert-and-return mongodb-connection "events" body)
                             (convert-id-to-string)
                             (assoc ctx :result))))
             :respond-with-entity? true
             :new? true
             :handle-created (fn [ctx]
                               (let [response (:result ctx)]
                                 {(:_id response) response})))

(defresource update-event [mongodb-connection id]
             :available-media-types ["application/edn"]
             :allowed-methods [:put]
             :put! (fn [ctx]
                     (let [body (-> (get-in ctx [:request :body])
                                    (clojure.edn/read-string)
                                    (dissoc :_id))]
                       (mc/update-by-id mongodb-connection "events" (ObjectId. id) body)))
             :respond-with-entity? true
             :new? false
             :handle-ok (fn [ctx]
                          (let [body (-> (get-in ctx [:request :body])
                                         (clojure.edn/read-string))]
                            body)))
