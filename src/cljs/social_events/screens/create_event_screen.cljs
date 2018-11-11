(ns social-events.screens.create-event-screen
  (:require
    [reagent.core :as reagent]
    [social-events.events :as events]
    [re-frame.core :refer [dispatch]]))

(def form
  (reagent/atom {:title ""
                 :description ""
                 :date-string ""
                 :participants []
                 :creator "Stephen"}))

(defn- form-header []
  [:div {:class "hero-head" :style {:padding-top "2%"}}
   [:section {:class "section"}
    [:h1 {:class "title is-1"} "Create An Event"]]])

(defn- form-body []
  (let [{:keys [title description date-string]} @form]
    [:div {:class "hero-body"}
     [:div {:class "container is-full-width"}
      [:div {:class "columns"}
       [:div {:class "column is-6"}
        [:input {:class "input"
                 :type "text"
                 :value title
                 :placeholder "Event Name"
                 :on-change #(swap! form assoc :title (-> % .-target .-value))}]]
       [:div {:class "column is-6"}
        [:input {:class "input"
                 :type "text"
                 :value date-string
                 :placeholder "Event Date (DD/MM/YYYY)"
                 :on-change #(swap! form assoc :date-string (-> % .-target .-value))}]]]
      [:hr]
      [:textarea {:class "textarea is-medium"
                  :rows "8"
                  :value description
                  :placeholder "Event Description"
                  :on-change #(swap! form assoc :description (-> % .-target .-value))}]
      [:hr]]]))

(defn- form-footer []
  [:div {:class "hero-foot"}
   [:div {:class "columns is-full-width is-horizontally-centered"}
    [:div {:class "column is-4" :style {:text-align "center" :padding-bottom "5%"}}
     [:button {:class "button is-rounded"
               :style {:width "50%"}
               :on-click #(dispatch [::events/create-event @form])} "Create Event"]]
    [:div {:class "column is-4" :style {:text-align "center" :padding-bottom "5%"}}
     [:button {:class "button is-rounded"
               :style {:width "50%"}
               :on-click #(swap! form assoc :title "" :description "" :date-string "")} "Cancel"]]]])

(defn- form-container []
  [:section {:class "hero is-primary is-full-height"}
   [form-header]
   [form-body]
   [form-footer]])

(defn create-event-screen []
  [:div {:class "container is-full-height"}
   [:div {:class "columns is-full-height"}
    [:div {:class "column is-8 is-offset-2 is-full-width"}
     [form-container]]]])