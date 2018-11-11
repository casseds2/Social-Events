(ns social-events.screens.event-screen
  (:require
    [re-frame.core :refer [subscribe dispatch]]
    [reagent.core :as reagent]
    [social-events.subs :as subs]
    [social-events.events :as events]))

(def page-state
  (reagent/atom {:show-participants? true
                 :show-delete-modal? false
                 :username ""}))

(def delete-string
  (reagent/atom ""))

(defn- event-banner [event]
  [:section {:class "hero is-primary"}
   [:div {:class "hero-body"}
    [:div {:class "container"}
     [:h1 {:class "title"} (:title event)]
     [:h2 {:class "subtitle"} (str "by " (:creator event))]
     [:h1 {:class "title"} (:date-string event)]]]])

(defn event-body [event]
  [:section {:class "section"}
   [:div {:class "container is-full-width"}
    [:p {:class "is-text-primary is-size-3"}
     (:description event)]]])

(defn- participant-item [member]
  [:li
   [:h6 {:class "subtitle is-6"} member]])

(defn- delete-modal [selected-event]
  (let [{:keys [show-delete-modal?]} @page-state
        {:keys [_id title]} selected-event
        modal-class (if show-delete-modal? "modal is-active" "modal")]
    [:div {:class modal-class}
     [:div {:class "modal-background"
            :on-click #(swap! page-state assoc :show-delete-modal? false)}]
     [:div {:class "modal-content" :style {:text-align "center"}}
      [:input {:class "input"
               :type "text"
               :placeholder "Event Name"
               :on-change #(reset! delete-string (-> % .-target .-value))}]
      [:a {:class "button is-danger"
           :style {:margin-top "2%"}
           :disabled (not= @delete-string title)
           :on-click #(do (dispatch [::events/delete-event _id])
                          (swap! page-state assoc :show-delete-modal? false))} "Delete"]]
     [:button {:class "modal-close is-large"
               :on-click #(swap! page-state assoc :show-delete-modal? false)}]]))

(defn- add-participant [event-id]
  (let [{:keys [username]} @page-state]
    [:div
     [:input {:class "is-primary is-full-width is-family-monospace"
              :type "text"
              :placeholder "Name"
              :on-change #(swap! page-state assoc :username (-> % .-target .-value))
              :value (:username @page-state)
              :style {:padding "10px"}}]
     [:button {:class "button is-primary is-full-width"
               :disabled (clojure.string/blank? username)
               :on-click #(do (swap! page-state assoc :username "")
                              (dispatch [::events/add-user-to-event event-id username]))} "Add"]]))

(defn- participants [event]
  (let [{:keys [show-participants?]} @page-state
        icon (if show-participants? "fas fa-angle-down" "fas fa-angle-up")
        count-participants (count (:participants event))
        header-title (if show-participants? "Participants" (str "Participants (" count-participants ")"))]
    [:div {:class "card is-overflow-capable"}
     [:header {:class "card-header"}
      [:p {:class "card-header-title"} header-title]
      [:a {:class "card-header-icon"
           :on-click #(swap! page-state assoc :show-participants? (not show-participants?))}
       [:span {:class "icon"}
        [:i {:class icon}]]]]
     (if show-participants?
       [:div {:class "card-content"}
        [:div {:class "content"}
          [:ol {:type "1"}
           (doall
             (map-indexed (fn [index participant]
                            ^{:key index} [participant-item participant])
                          (:participants event)))]]])
     [add-participant (:_id event)]]))

(defn event-screen []
  (let [selected-event (subscribe [::subs/selected-event])]
    [:div {:class "container is-full-height" :style {:padding "5%"}}
     [delete-modal @selected-event]
     [:div {:class "columns is-full-height"}
      [:div {:class "column is-9" :style {:max-width "100%"}}
       [:div {:class "is-full-height" :style {:background-color "#efefef"}}
        [event-banner @selected-event]
        [event-body @selected-event]]
       [:a {:class "button is-primary"
            :href (str "/#/event/update/" (:_id @selected-event))} "Edit"]
       [:button {:class "button is-danger"
                 :on-click #(swap! page-state assoc :show-delete-modal? true)} "Delete"]]
      [:div {:class "column is-3"}
       [participants @selected-event]]]]))