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
       [:a {:class "navbar-item"} "Blog"]
       [:a {:class "navbar-item"} "Suggestion Box"]]]
     [:div {:class "navbar-item has-dropdown is-hoverable"}
      [:a {:class "navbar-link"} "About"]
      [:div {:class "navbar-dropdown"}
       [:a {:class "navbar-item"
            :href "/#/about"} "About"]
       [:a {:class "navbar-item"} "Contribute"]]]]]])