(ns site.common
  (:require [clj-time.core :as t]
            [clj-time.coerce :as coerce]
            [clj-time.format :as fmt]
            [clj-time.local :as local]))

(defn head
  ([] (head nil))
  ([title]
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1, maximum-scale=1"}]
    [:meta {:itemprop "author" :name "author" :content "Johannes Staffans"}]
    [:title (if title (str "Johannes Staffans - " title) "Johannes Staffans")]
    [:link {:href "https://fonts.googleapis.com/css?family=Merriweather:400,700|Open+Sans:400,700"
            :rel "stylesheet"}]
    [:link {:type "text/css" :rel "stylesheet"
            :href "/styles/main.css"}]]))

(defn header
  []
  [:div.header
   [:div.header__content.clearfix
    [:div.sm-col.sm-col-8
     [:div "personal website and blog of"]
     [:h1.header__headline [:a {:href "/"} "Johannes Staffans"]]]
    [:div.sm-col.flex-column
     [:a {:href "/pages/about.html"} "about"]
     [:div.pt1 "elsewhere"]
     [:div
      [:a {:href "https://github.com/jstaffans"} "github"]
      [:span " &middot; "]
      [:a {:href "https://www.linkedin.com/in/jstaffans"} "linkedin"]
      [:span " &middot; "]
      [:a {:href "https://twitter.com/jstaffans"} "@jstaffans"]]]]
   [:div.clearfix
    [:svg.left.border-triangle {:xmlns "http://www.w3.org/2000/svg" :version "1.1"}
     [:polygon.mask {:points "0,0 30,0 30,30 0,30"}]
     [:polygon {:points "0,15 30,0 30,30"}]]
    [:svg.right.border-triangle {:xmlns "http://www.w3.org/2000/svg" :version "1.1"}
     [:polygon.mask {:points "0,0 30,0 30,30 0,30"}]
     [:polygon {:points "30,15 0,30 0,0"}]]]])

(defn footer
  []
  [:div.footer
   [:p (str "&copy; 2015-" (fmt/unparse (fmt/formatter "YYYY") (t/now)) " Johannes Staffans")]])

(defn post-date
  [entry]
  (let [d (coerce/from-date (:date entry))]
    [:span (fmt/unparse (fmt/formatter "dd.MM.YYYY") d)]))
