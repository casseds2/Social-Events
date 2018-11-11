(ns social-events.components.navbar)

(defn navbar []
  [:nav {:class "navbar is-fixed-top is-transparent"}
   [:div {:class "navbar-brand"}
    [:a {:class "navbar-item"
         :href "/#/"}
     [:h1 {:class "title is-2" :style {:text-decoration "underline"}} "CelluSocial"]]
    [:a {:class "navbar-burger burger is-active"}
     [:span {:aria-hidden "true"}]
     [:span {:aria-hidden "true"}]
     [:span {:aria-hidden "true"}]]]
   [:div {:class "navbar-menu"}
    [:div {:class "navbar-start"}
     [:a {:class "navbar-item"
          :href "/#/"} "Home"]
     [:div {:class "navbar-item has-dropdown is-hoverable"}
      [:a {:class "navbar-link"
           :href "/#/events"} "Events"]
      [:div {:class "navbar-dropdown"}
       [:a {:class "navbar-item"
            :href "/#/create-event"} "Create Event"]]]
     [:div {:class "navbar-item has-dropdown is-hoverable"}
      [:a {:class "navbar-link"} "Social"]
      [:div {:class "navbar-dropdown"}
       [:a {:class "navbar-item"} "Blog"]]]
     [:div {:class "navbar-item has-dropdown is-hoverable"}
      [:a {:class "navbar-link"} "About"]
      [:div {:class "navbar-dropdown"}
       [:a {:class "navbar-item"
            :href "/#/about"} "About"]
       [:a {:class "navbar-item"} "Contribute"]]]]
    [:div {:class "navbar-end"}
     [:div {:class "navbar-item"}
      [:div {:class "buttons"}
       [:a {:class "button is-primary"}
        [:strong "Sign up"]]
       [:a {:class "button is-light"} "Log in"]]]]]])