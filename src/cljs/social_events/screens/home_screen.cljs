(ns social-events.screens.home-screen
  (:require
    [reagent.core :as reagent]
    [social-events.events :as events]
    [re-frame.core :refer [subscribe dispatch]]
    [social-events.subs :as subs]))

(def page-state
  (reagent/atom {:selected-event-index 0
                 :selected-event {}}))

(defn- event-item [index event]
  (let [value (val event)
        id (first event)
        button-class (if (= index (:selected-event-index @page-state))
                       "button is-primary is-inverted is-full-width"
                       "button is-primary is-full-width")]
    [:tr {:class (if (= index (:selected-event-index @page-state))
                   "is-selected")
          :on-click #(swap! page-state assoc
                            :selected-event-index index
                            :selected-event value)}
     [:td (:title value)]
     [:td (:date-string value)]
     [:td (count (:participants value))]
     [:td (:creator value)]
     [:td [:a {:class button-class
               :href (str "/#/event/" id)
               :on-click #(dispatch [::events/set-selected-event id])} "View"]]]))

(defn home-screen []
  (let [events (subscribe [::subs/events])]
    [:div {:class "container is-full-height"}
     [:div {:class "columns is-full-height"}
      [:div {:class "column is-8 is-offset-2 is-vertically-and-horizontally-centered"}
       [:table {:class "table is-bordered is-hoverable is-fullwidth is-striped"}
        [:thead
         [:tr
          [:th "Title"]
          [:th "Date"]
          [:th "Participants"]
          [:th "Created By"]
          [:th "Action"]]]
        [:tbody
         (doall
           (map-indexed (fn [index event]
                          ^{:key index} [event-item index event])
                        @events))]]]]]))

