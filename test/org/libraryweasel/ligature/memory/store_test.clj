; Copyright (c) 2019-2020 Alex Michael Berry
;
; This program and the accompanying materials are made
; available under the terms of the Eclipse Public License 2.0
; which is available at https://www.eclipse.org/legal/epl-2.0/
;
; SPDX-License-Identifier: EPL-2.0

(ns org.libraryweasel.ligature.memory.store-test
  (:require [clojure.test :refer :all]
            [org.libraryweasel.ligature.memory.store :refer :all]
            [org.libraryweasel.ligature.core :refer :all]))

(deftest store-test
  (testing "Create new store."
    (is (not (= (ligature-memory-store) nil))))
  (testing "Basic store functionality."
    (let [store (ligature-memory-store)]
      (is (not (= (get-dataset store "test") nil)))
      (is (= (all-datasets store) #{"test"}))
      (is (not (= (get-dataset store "test2") nil)))
      (is (= (all-datasets store) #{"test" "test2"}))
      (delete-dataset store "test")
      (is (= (all-datasets store) #{"test2"}))
      (testing "Basic dataset functionality."
        (let [dataset (get-dataset store "test")]
          (is (= (dataset-name dataset) "test"))
          (is (= (set (all-statements dataset)) #{}))
          (add-statements dataset [(statement "This" :a "test")])
          (is (= (set (all-statements dataset)) #{(statement "This" :a "test")}))
          (add-statements dataset [(statement "a" :a "a") (statement "b" :a "b")])
          (add-statements dataset [(statement "c" :a "c" "c")])
          (is (= (set (all-statements dataset)) #{(statement "This" :a "test")
                                                  (statement "a" :a "a")
                                                  (statement "b" :a "b")
                                                  (statement "c" :a "c" "c")})))))))
