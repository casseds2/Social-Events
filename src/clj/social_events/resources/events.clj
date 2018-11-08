(ns social-events.resources.events
  (:require
    [monger.collection :as mc]
    [liberator.core :refer [defresource]]))

(defn- convert-ids-to-string [coll]
  (mapv (fn [doc]
          (let [id (:_id doc)]
            (assoc doc :_id (str id)))) coll))

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
