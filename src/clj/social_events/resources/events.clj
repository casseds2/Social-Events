(ns social-events.resources.events
  (:require
    [monger.collection :as mc]
    [liberator.core :refer [defresource]])
  (:import (org.bson.types ObjectId)))

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