(ns social-events.events
  (:require
   [re-frame.core :refer [reg-event-db reg-event-fx reg-fx dispatch]]
   [social-events.db :as db]
   [social-events.config :refer [api-url]]
   [day8.re-frame.http-fx]
   [day8.re-frame.async-flow-fx]
   [ajax.edn :refer [edn-request-format edn-response-format]]
   [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]))

(reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

;; HELPER EVENTS ;;
(reg-fx
  ::navigate
  (fn [url]
    (set! (.-hash js/location) url)))

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
    (let [selected-event (get-in db [:events id])]
     (if selected-event
       (assoc db :selected-event (get-in db [:events id]))
       (dispatch [::fetch-event id])))))

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
             (assoc-in [:app-state :fetched-all-events?] true)
             (assoc :events response))}))

(reg-event-fx
  ::fetch-events-failure
  (fn [{:keys [db]} [_ response]]
    {:db (assoc-in db [:app-state :fetching-events?] false)}))

(reg-event-fx
  ::fetch-event-success
  (fn [{:keys [db]} [_ response]]
    {:db (-> (assoc-in db [:app-state :fetching-event?] false)
             #_(assoc :events (:_id response) response)
             (assoc :selected-event response))}))

(reg-event-fx
  ::fetch-event-failure
  (fn [{:keys [db]} [_ response]]
    {:db (assoc-in db [:app-state :fetching-event?] false)}))

(reg-event-fx
  ::create-event-success
  (fn [{:keys [db]} [_ response]]
    {:db (-> (assoc-in db [:app-state :creating-event?] false)
             (assoc :events (conj (:events db) response)))
     ::navigate "/"}))

(reg-event-fx
  ::create-event-failure
  (fn [{:keys [db]} [_ response]]
    {:db (assoc-in db [:app-state :creating-event?] false)}))

(reg-event-fx
  ::update-event-success
  (fn [{:keys [db]} [_ response]]
    {:db (-> (assoc-in db [:app-state :updating-event?] false)
             (assoc-in [:events (:_id response)] response)
             (assoc :selected-event response))
     ::navigate (str "/event/" (:_id response))}))

(reg-event-fx
  ::update-event-failure
  (fn [{:keys [db]} [_ response]]
    {:db (assoc-in db [:app-state :updating-event?] false)}))

;; HTTP EFFECTS ;;
(reg-event-fx
  ::fetch-events
  (fn [{:keys [db]} _]
    (if-not (-> db :app-state :fetched-all-events?)
      {:db (assoc-in db [:app-state :fetching-events?] true)
       :http-xhrio {:method :get
                    :uri (str api-url "/api/events")
                    :timeout 8000
                    :response-format (edn-response-format)
                    :on-success [::fetch-events-success]
                    :on-failure [::fetch-events-failure]}})))

(reg-event-fx
  ::fetch-event
  (fn [{:keys [db]} [_ id]]
    {:db (assoc-in db [:app-state :fetching-event?] true)
     :http-xhrio {:method :get
                  :uri (str api-url "/api/events/" id)
                  :timeout 8000
                  :response-format (edn-response-format)
                  :on-success [::fetch-event-success]
                  :on-failure [::fetch-event-failure]}}))

(reg-event-fx
  ::create-event
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db [:app-state :creating-event?] true)
     :http-xhrio {:method :post
                  :uri (str api-url "/api/events")
                  :params value
                  :timeout 8000
                  :format (edn-request-format)
                  :response-format (edn-response-format)
                  :on-success [::create-event-success]
                  :on-failure [::create-event-failure]}}))

(reg-event-fx
  ::update-event
  (fn [{:keys [db]} [_ id value]]
    {:db (assoc-in db [:app-state :updating-event?] true)
     :http-xhrio {:method :put
                  :uri (str api-url "/api/events/" id)
                  :params value
                  :timeout 8000
                  :format (edn-request-format)
                  :response-format (edn-response-format)
                  :on-success [::update-event-success]
                  :on-failure [::update-event-failure]}}))