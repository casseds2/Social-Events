(ns social-events.events
  (:require
   [re-frame.core :refer [reg-event-db]]
   [social-events.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]))
   
(reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(reg-event-db
 ::set-active-panel
 (fn-traced [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

(reg-event-db
  ::update-auth-field
  (fn-traced [db [_ id value]]
    (assoc-in db [:auth id] value)))

(reg-event-db
  ::set-selected-event
  (fn-traced [db [_ id]]
    (assoc db :selected-event (get-in db [:events id]))))

(reg-event-db
  ::add-user-to-event
  (fn-traced [db [_ id value]]
     (-> (update-in db [:events id :participants] conj value)
         (update-in [:selected-event :participants] conj value))))
