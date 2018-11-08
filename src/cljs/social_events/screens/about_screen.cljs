(ns social-events.screens.about-screen
  (:require
    [reagent.core :as reagent]))

(def page-state
  (reagent/atom {:show-snapcode? false}))

(defn- snapcode-modal []
  (let [{:keys [show-snapcode?]} @page-state
        modal-class (if show-snapcode? "modal is-active" "modal")]
    [:div {:class modal-class}
     [:div {:class "modal-background"
            :on-click #(swap! page-state assoc :show-snapcode? false)}]
     [:div {:class "modal-content"}
      [:img {:src "/assets/snapcode.png"}]]
     [:button {:class "modal-close is-large"
               :on-click #(swap! page-state assoc :show-snapcode? false)}]]))

(defn- card-body []
  [:div {:class "media"}
   [:div {:class "media-content"}
    [:p {:class "title is-4"} "Stephen Cassedy"]
    [:p {:class "subtitle is-6"} "I do this from the goodness of my heart and the shameless C.V. filler"]]])

(defn- card-footer []
  [:div {:class "columns is-full-width"}
   [:div {:class "column is-4 is-horizontally-centered"}
    [:a {:href "https://github.com/casseds2/"}
     [:span {:class "icon"}
      "Github"
      [:i {:class "fab fa-github"}]]]]
   [:div {:class "column is-4 is-horizontally-centered"}
    [:a {:on-click #(swap! page-state assoc :show-snapcode? true)}
     [:span {:class "icon"}
      "Snapchat"
      [:i {:class "fab fa-snapchat"
           :on-click #(swap! page-state assoc :show-snapcode? true)}]]]]
   [:div {:class "column is-4 is-horizontally-centered"}
    [:a  {:href "https://www.linkedin.com/in/stephen-cassedy-178454ba/"}
     [:span {:class "icon"}
      "Linked"
      [:i {:class "fab fa-linkedin-in"}]]]]])


(defn about-screen []
  [:div {:class "container is-centered-x-y"}
   [snapcode-modal]
   [:div {:class "card" :style {:border-radius "20px"}}
    [:div {:class "card-image is-horizontally-centered"}
     [:figure {:class "image"}
      [:img {:src "/assets/navbar-brand.png"}]]]
    [:div {:class "card-content"}
     [card-body]]
    [:footer {:class "card-footer"}
     [card-footer]]]])
