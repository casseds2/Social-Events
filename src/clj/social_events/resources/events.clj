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
(spec/def ::empty-vector empty?)
(spec/def ::participants (or (spec/coll-of string?) ::empty-vector))
(spec/def ::event (spec/keys :req-un [::title
                                      ::description
                                      ::creator
                                      ::date-string
                                      ::participants]))

(defn extract-request-body [ctx]
  (-> (get-in ctx [:request :body])
      (clojure.edn/read-string)))

(defn- malformed? [event]
  (let [conformed (->> (clojure.edn/read-string event)
                       (spec/conform ::event))]
    (= ::spec/invalid conformed)))

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
             :available-media-types ["application/edn"]
             :allowed-methods [:get]
             :handle-ok (fn [_]
                          (-> (mc/find-maps mongodb-connection "events")
                              (convert-ids-to-string)
                              (extract-id-as-key))))

(defresource fetch-event [mongodb-connection id]
             :available-media-types ["application/edn"]
             :allowed-methods [:get]
             :handle-ok (fn [_]
                          (-> (mc/find-map-by-id mongodb-connection "events" (ObjectId. id))
                              (convert-id-to-string))))

(defresource create-event [mongodb-connection]
             :available-media-types ["application/edn"]
             :allowed-methods [:post]
             :malformed? #(malformed? (-> % :request :body))
             :post! (fn [ctx]
                      (let [body (extract-request-body ctx)]
                        (->> (mc/insert-and-return mongodb-connection "events" body)
                             (convert-id-to-string)
                             (assoc ctx :result))))
             :new? true
             :respond-with-entity? true
             :handle-created (fn [ctx]
                               (let [response (:result ctx)]
                                 {(:_id response) response})))

(defresource update-event [mongodb-connection id]
             :available-media-types ["application/edn"]
             :allowed-methods [:put]
             :malformed? #(malformed? (-> % :request :body))
             :put! (fn [ctx]
                     (let [body (extract-request-body ctx)]
                       (mc/update-by-id mongodb-connection "events" (ObjectId. id) body)))
             :new? false
             :respond-with-entity? true
             :handle-ok (fn [ctx]
                          (let [body (-> (get-in ctx [:request :body])
                                         (clojure.edn/read-string))]
                            (assoc body :_id id))))

(defresource delete-event [mongodb-connection id]
             :available-media-types ["application/edn"]
             :allowed-methods [:delete]
             :delete! (fn [_]
                        (mc/remove-by-id mongodb-connection "events" (ObjectId. id))))
