(ns social-events.events
  (:require
   [re-frame.core :refer [reg-event-db reg-event-fx]]
   [social-events.db :as db]
   [social-events.config :refer [api-url]]
   [day8.re-frame.http-fx]
   [ajax.edn :refer [edn-request-format edn-response-format]]
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

;; HTTP SUCCESS/FAILURE HANDLERS ;;
(reg-event-fx
  ::fetch-events-success
  (fn [{:keys [db]} [_ response]]
    {:db (-> (assoc-in db [:app-state :fetching-events?] false)
             (assoc :events response))}))

(reg-event-fx
  ::fetch-events-failure
  (fn [{:keys [db]} [_ response]]
    {:db (assoc-in db [:app-state :fetching-events?] false)}))

(reg-event-fx
  ::create-event-success
  (fn [{:keys [db]} [_ response]]
    {:db (assoc-in db [:app-state :creating-event?] false)}))

(reg-event-fx
  ::create-event-failure
  (fn [{:keys [db]} [_ response]]
    {:db (assoc-in db [:app-state :creating-event?] false)}))

;; HTTP EFFECTS ;;
(reg-event-fx
  ::fetch-events
  (fn [{:keys [db]} _]
    {:db (assoc-in db [:app-state :fetching-events?] true)
      :http-xhrio {:method :get
                   :uri (str api-url "/api/events")
                   :timeout 8000
                   :response-format (edn-response-format)
                   :on-success [::fetch-events-success]
                   :on-failure [::fetch-events-failure]}}))

(reg-event-fx
  ::create-event
  (fn [{:keys [db]} _ [_ value]]
    {:db (assoc-in db [:app-state :creating-event] true)
     :http-xhrio {:method :post
                  :uri (str api-url "/api/create-event")
                  :params value
                  :timeout 8000
                  :format (edn-request-format)
                  :response-format (edn-response-format)
                  :on-success [::create-event-success]
                  :on-failure [::create-event-failure]}}))