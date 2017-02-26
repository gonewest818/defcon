(ns defcon.core-test
  (:require [midje.sweet :refer :all]
            [midje.util :refer [testable-privates]]
            [defcon.core :refer :all]))

(testable-privates defcon.core
                   cast-to
                   mandatory-var-exception
                   get-config-or-default)

(facts "cast-to"
  (fact "can cast strings to integers"
    (cast-to :integer "1") => 1
    (cast-to :integer "1") => 1
    (cast-to :integer "-1") => -1
    (cast-to :integer "100") => 100
    (cast-to :integer "-100") => -100
    (cast-to :integer "0") => 0)
  (fact "can cast strings to float"
    (cast-to :float "1.123") => (roughly 1.123)
    (cast-to :float "-99.675") => (roughly -99.675)
    (cast-to :float "1e-10") => (roughly 0.0000000001))
  (fact "can cast strings to boolean values"
    (cast-to :boolean "true") => true
    (cast-to :boolean "false") => false
    (cast-to :boolean "True") => true
    (cast-to :boolean "False") => false
    (cast-to :boolean "TRUE") => true
    (cast-to :boolean "FALSE") => false
    (cast-to :boolean nil) => false)
  (fact "can cast non-nil values to strings"
    (cast-to :string "foo" => "foo")
    (cast-to :string "" => "")
    (cast-to :string true) => "true"
    (cast-to :string false) => "false")
  (fact "can cast nil to nil"
    (cast-to :string nil => nil))
  (fact "throws when value cannot be interpreted"
    (cast-to :integer "") => (throws NumberFormatException)
    (cast-to :integer "foo") => (throws NumberFormatException)
    (cast-to :integer "1.2") => (throws NumberFormatException)
    (cast-to :integer 1.1) => (throws ClassCastException)
    (cast-to :integer 1) => (throws ClassCastException)
    (cast-to :integer true) => (throws ClassCastException)
    (cast-to :integer false) => (throws ClassCastException)    
    (cast-to :float "abcde") => (throws NumberFormatException)
    (cast-to :float  4.3) => (throws ClassCastException)
    (cast-to :float true) => (throws ClassCastException)
    (cast-to :float false) => (throws ClassCastException)
    (cast-to :boolean 0) => (throws ClassCastException)
    (cast-to :boolean 1) => (throws ClassCastException)
    (cast-to :boolean true) => (throws ClassCastException)
    (cast-to :boolean false) => (throws ClassCastException))
  (fact "returns value unchanged when type keyword is unknown"
    (cast-to :unknown-type 0) => 0
    (cast-to :unknown-type 1.23) => 1.23
    (cast-to :unknown-type "hello world") => "hello world"
    (cast-to :unknown-type true) => true
    (cast-to :unknown-type nil) => nil))

(facts "mandatory-var-exception"
  (fact "throws an Exception with a descriptive string"
    (mandatory-var-exception "name") => (throws Exception #"Required setting not provided")
    (mandatory-var-exception nil) => (throws Exception #"Required setting not provided")))

(facts "get-config-or-default"
  (fact "reads variable from environment if available"
    (get-config-or-default {:name :foo :type :string  :default nil}) => [:foo "hello"]
    (provided (environ.core/env :foo) => "hello")
    (get-config-or-default {:name :bar :type :integer :default nil}) => [:bar 123]
    (provided (environ.core/env :bar) => "123")
    (first (get-config-or-default {:name :baz :type :float :default nil})) => :baz
    (provided (environ.core/env :baz) => "4.56")
    (second (get-config-or-default {:name :baz :type :float :default nil})) => (roughly 4.56)
    (provided (environ.core/env :baz) => "4.56")
    (get-config-or-default {:name :qux :type :boolean :default nil}) => [:qux true]
    (provided (environ.core/env :qux) => "TRUE"))
  (tabular
    (fact "returns default if variable not set in environment"
      (get-config-or-default {:name :foo :type ?type :default ?default}) => [:foo ?default])
      ?type    ?default
      :integer 1
      :float   2.3
      :string  "hi"
      :boolean true))

(facts "init"
  (fact "returns hash-map of configuration settings (or defaults) cast to the desired types"
    (init []) => {}
    (init [{:name :foo :type :integer :default 1}
           {:name :bar :type :float   :default 3}]) => {:foo 1 :bar 3}
    (init [{:name :foo :type :integer :default 1}
           {:name :bar :type :float   :default 3}]) => {:foo 1 :bar 4.5}
    (provided
      (environ.core/env :foo) => nil
      (environ.core/env :bar) => "4.5")
    ))
