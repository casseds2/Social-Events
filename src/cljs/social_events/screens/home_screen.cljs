(ns social-events.screens.home-screen
  (:require
    [reagent.core :as reagent]
    [re-frame.core :refer [subscribe]]
    [social-events.subs :as subs]))

(def page-state
  (reagent/atom {:selected-event-index nil
                 :selected-event {}
                 :modal-open? false}))

(defn- event-item [index event]
  (let [value (val event)]
    [:tr {:class (if (= index (:selected-event-index @page-state))
                   "is-selected")
          :on-click #(swap! page-state assoc
                            :selected-event-index index
                            :selected-event value
                            :modal-open? (not (:modal-open? @page-state)))}
     [:td (:title value)]
     [:td (:date-string value)]
     [:td (count (:participants value))]
     [:td (:creator value)]]))

(defn- event-modal []
  (let [{:keys [selected-event modal-open?]} @page-state
        modal-class (if modal-open? "modal is-active" "modal")]
    [:div {:class modal-class}
     [:div {:class "modal-background"
            :on-click #(swap! page-state assoc :modal-open? false)}]
     [:div {:class "modal-content"}
      [:section {:class "modal-card-body"}
        [:div {:class "container" :style {:max-width "100%"}}
         [:h3 {:class "subtitle is-3"}  (:title selected-event)]
         [:h3 {:class "subtitle is-3"} (str "Participants: " (count (:participants selected-event)))]]]]
     [:button {:class "modal-close is-large"
               :on-click #(swap! page-state assoc :modal-open? false)}]
     [:footer {:class "modal-card-foot"}
      [:a {:class "button is-success"
           :href (str "/#/event/" (:id selected-event))
           :on-click #(swap! page-state assoc :modal-open? false)} "View Event"]
      [:button {:class "button"
                :on-click #(swap! page-state assoc :modal-open? false)} "Cancel"]]]))

(defn home-screen []
  (let [events (subscribe [::subs/events])]
    [:div {:class "container is-full-height"}
     [event-modal]
     [:div {:class "columns is-full-height"}
      [:div {:class "column is-8 is-offset-2 is-vertically-and-horizontally-centered"}
       [:table {:class "table is-bordered is-hoverable is-fullwidth is-striped"}
        [:thead
         [:tr
          [:th "Title"]
          [:th "Date"]
          [:th "Participants"]
          [:th "Created By"]]]
        [:tbody
         (doall
           (map-indexed (fn [index event]
                          ^{:key index} [event-item index event])
                        @events))]]]]]))
