(ns site.core
  (:require [hiccup.page :as hp]
            [clojure.string :refer [split-lines]]
            [site.common :refer [head header post-date]]))

(defn trace
  [x]
  (prn x)
  x)

(defn base
  [content & {:keys [title]}]
  (hp/html5
   (head title)
   [:div
    (header)
    [:div.container
     content]]))

(defn- teaser
  [entry]
  (let [text (-> entry :content split-lines first)]
    [:div
     text
     [:p [:a {:href (:permalink entry)} "Read more ..."]]]))

(defn index
  [{:keys [entries]}]
  (base
   [:div
    [:p "Hi, welcome to my blog! Here's my latest post:"]
    (let [latest (-> entries first)]
      [:div
       [:h3 (:title latest)]
       (teaser latest)])
    [:h2 "Other posts"]
    [:div.posts
     (for [entry (rest entries)]
       [:div.sm-flex
        [:div.posts__date (post-date entry)]
        [:div [:a {:href (:permalink entry)} (:title entry)]]])]]))

(defn post
  [{:keys [entry]}]
  (base
   [:div
    [:div.flex.items-baseline.justify-between
     [:h2 (:title entry)]
     [:div (post-date entry)]]
    [:div
     (:content entry)]
    [:div [:a {:href "/"} "Back"]]]
   :title (:title entry)))

(defn page
  [data]
  (base
   (-> data :entry :content)))
