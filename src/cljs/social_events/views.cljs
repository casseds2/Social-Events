(ns social-events.views
  (:require
   [re-frame.core :as re-frame]
   [re-com.core :as re-com]
   [social-events.events :as events]
   [social-events.subs :as subs]))

(defn bulma-home-panel []
 [:div {:class "is-centered-x-y"}
  [:section {:class "container" :style {:max-width "15%"}}
   [:h1 {:class "title is-3" :style {:text-align "center"}} "Welcome"]
   [:div {:class "field"}
    [:p {:class "control has-icons-left has-icons-right"}
     [:input {:class "input" :type "email" :placeholder "Email"}]
     [:span {:class "icon is-small is-left"}
      [:i {:class "fas fa-envelope"}]]]]
   [:div {:class "field"}
    [:p {:class "control has-icons-left"}
     [:input {:class "input" :type "password" :placeholder "Password"}]
     [:span {:class "icon is-small is-left"}
      [:i {:class "fas fa-lock"}]]]]
   [:div {:class "field"}
    [:p {:class "control" :style {:text-align "center"}}
     [:a {:class "button is-primary is-rounded is-medium"
          :href "#/bulma-about"} "Submit"]]]]])

(defn bulma-about-panel []
  [:div {:class "columns"}
   [:div {:class "column is-2"}
    [:aside {:class "menu"}
     [:p {:class "menu-label"}]
     [:ul {:class "menu-list"}
      [:li {:class "menu-item-radius"}
       [:a "Dashboard"]]
      [:li
       [:a {:href "#/bulma-home"} "Customers"]]]
     [:p {:class "menu-label"}]
     [:ul {:class "menu-list"}
      [:li
       [:a {:href "#/bulma-home"} "Team Settings"]]
      [:li
       [:a {:href "#/bulma-home"} {:class "is-active"} "Manage Your Team"]]
      [:li
       [:a {:href "#/bulma-home"} "Invitations"]]
      [:li
       [:a "Cloud Storage Environment Settings"]]
      [:li
       [:a {:href "#/bulma-home"} "Authentication"]]]
     [:p {:class "menu-label"}]
     [:ul {:class "menu-list"}
      [:li
       [:a {:href "#/bulma-home"} "Payments"]]
      [:li
       [:a {:href "#/bulma-home"} "Transfers"]]
      [:li
       [:a {:href "#/bulma-home"} "Balance"]]
      [:li
       [:a {:href "#/spectre-home"} "Spectre Home"]]
      [:li
       [:a {:href "#/spectre-about"} "Spectre About"]]]]]])

(defn spectre-home-panel []
  [:div {:class "is-centered-x-y"}
   [:div {:class "container"}
    [:div {:class "columns"}
     [:div {:class "col-2 col-mx-auto"}
      [:div {:class "form-group"}
       [:label {:class "form-label", :for "email-input"} "Name"]
       [:input {:class "form-input"
                :type "email"
                :id "email"
                :placeholder "Email"}]
       [:label {:class "form-label"
                :for "password-input"} "Password"]
       [:input {:class "form-input"
                :type "password"
                :id "password"
                :placeholder "Password"}]]
      [:div {:class "col-2 col-mx-auto"}
       [:a {:class "btn btn-primary circle"
            :href "#/spectre-about"} "Submit"]]]]]])

(defn spectre-about-panel []
  [:div {:class "container"}
   [:div {:class "columns"}
    [:div {:class "col-1"}
     [:ul {:class "nav"}
      [:li {:class "nav-item"}
       [:a {:href "#/spectre-home"} "Elements"]]
      [:li {:class "nav-item active"}
       [:a {:href "#/spectre-home"} "Layout"]
       [:ul {:class "nav"}
        [:li {:class "nav-item"}
         [:a {:href "#/spectre-home"} "Flexbox grid"]]
        [:li {:class "nav-item"}
         [:a {:href "#/spectre-home"} "Responsive"]]
        [:li {:class "nav-item"}
         [:a {:href "#/spectre-home"} "Navbar"]]
        [:li {:class "nav-item"}
         [:a {:href "#/spectre-home"} "Empty states"]]]]
      [:li {:class "nav-item"}
       [:a {:href "#/spectre-home"} "Components"]]
      [:li {:class "nav-item"}
       [:a {:href "#/spectre-home"} "Utilities"]]
      [:li {:class "nav-item"}
       [:a {:href "#/bulma-home"} "Bulma Home"]]
      [:li {:class "nav-item"}
       [:a {:href "#/bulma-about"} "Bulma About"]]]]
    [:div {:class "col-11"}
     [:h1 "Hello!"]]]])


(defn- panels [panel-name]
  (case panel-name
    :spectre-home-panel [spectre-home-panel]
    :spectre-about-panel [spectre-about-panel]
    :bulma-home-panel [bulma-home-panel]
    :bulma-about-panel [bulma-about-panel]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [:div {:style {:height "100vh" :width "100%"}}
     [panels @active-panel]]))
