(defproject talk "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2138"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                 [om "0.3.6"]
                 [com.facebook/react "0.8.0.1"]]

  :plugins [[lein-cljsbuild "1.0.2"]
            [lein-garden "0.1.5"]]

  :source-paths ["src"]

  :garden {:builds [{;; Optional name of the build.
                     :id "screen"
                     ;; The var containing your stylesheet.
                     :stylesheet talk.style/screen
                     ;; Compiler flags passed to `garden.core/css`.
                     :compiler {;; Where to save the file.
                                :output-to "public/screen.css"
                                ;; Compress the output?
                                :pretty-print? false}}]}

  :cljsbuild {
    :builds [{:id "talk"
              :source-paths ["src"]
              :compiler {
                :output-to "talk.js"
                :output-dir "out"
                :optimizations :none
                :source-map true}}]})
