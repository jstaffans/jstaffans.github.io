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
     [:h1.header__headline.pb2 "Johannes Staffans"]]
    [:div.sm-col
     [:a {:href "#"} "about"]]]]
  )
