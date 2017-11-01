(ns site.common)

(defn head
  []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1, maximum-scale=1"}]
   [:meta {:itemprop "author" :name "author" :content "Johannes Staffans"}]
   [:title "Johannes Staffans"]
   [:link {:href "https://fonts.googleapis.com/css?family=Merriweather:400,700|Open+Sans:400,700"
           :rel "stylesheet"}]
   [:link {:type "text/css" :rel "stylesheet"
           :href "/styles/main.css"}]])

(defn header
  []
  [:div.header
   [:div.header__content.clearfix
    [:div.sm-col.sm-col-9
     [:div "personal website and blog of"]
     [:h1.header__headline "Johannes Staffans"]]
    [:div.sm-col
     [:a {:href "#"} "about"]]]
   ;; <svg xmlns="http://www.w3.org/2000/svg" version="1.1" class="svg-triangle">
   ;;   <polygon points="0,0 100,0 50,100"/>
   ;; </svg>
   [:div.clearfix
    [:svg.left.border-triangle {:xmlns "http://www.w3.org/2000/svg" :version "1.1"}
     [:polygon.mask {:points "0,0 30,0 30,30 0,30"}]
     [:polygon {:points "0,15 30,0 30,30"}]]
    [:svg.right.border-triangle {:xmlns "http://www.w3.org/2000/svg" :version "1.1"}
     [:polygon.mask {:points "0,0 30,0 30,30 0,30"}]
     [:polygon {:points "30,15 0,30 0,0"}]]]])
