(ns shopit.routes.home
  (:require [shopit.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :refer [ok]]
            [clojure.java.io :as io]
            [shopit.db.core :as db]
            [bouncer.core :as b]
            [bouncer.validators :as v]
            [ring.util.response :refer [redirect]]))

(defn home-page [{:keys [flash]}]
  (layout/render
   "home.html"
   (merge {:products (db/list-products)}
          (select-keys flash [:name :description :price :errors]))))

(defn about-page []
  (layout/render "about.html"))

(defn validate-product [params]
  (first
    (b/validate
      params
      :name v/required
      :price v/required
      :description [v/required [v/min-count 10]])))

(defn create-product! [{:keys [params]}]
  (if-let [errors (validate-product params)]
    (-> (redirect "/")
        (assoc :flash (assoc params :errors errors)))
    (do
      (db/create-product! params)
      (redirect "/"))))

(defroutes home-routes
  (GET "/" request (home-page request))
  (POST "/" request (create-product! request))
  (GET "/about" [] (about-page)))

