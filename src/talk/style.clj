(ns talk.style
  (:require [garden.def :refer [defstylesheet defstyles]]))


(defstyles screen
  [:*
   {:box-sizing "border-box"}]

  [:body
   {:font-family "sans-serif"}]

  [:.message
   {:padding-left "40px"}]

  [:.message__author
   {:margin-left "-40px"
    :float       "left"}])
