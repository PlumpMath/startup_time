(ns timsg.startup-time)

(defmacro from-compiled-m []
  *compile-files*)

(def from-compiled? (from-compiled-m))

;; ==================================================
;; simple-test

(def simple-test-runs 3000)

;; To startup, no require: NR
;; To require: R

;; 0:
;; NR: ~ 6s
;; R: ~ 0s

;; 1000: 
;; NR: ~ 6s
;; R: 1719, 1711 ms

;; 2000: 
;; NR: ~ 6s
;; R: 6812, 6868, 6776 ms

;; 3000:
;; NR: ~ 6s
;; R: 16327 ms

;; 4000: ~ 6s

;; 8000: ~6s

;; (defmacro simple-test []
;;   (cons `do
;;     (for [i (range simple-test-runs)]
;;       `(defn ~(gensym "simple_test") [x#]
;;          x#))))

;; (simple-test)

;; ==================================================
;;

(def simpler-test-runs 3000)

(defmacro simpler-test []
  `(def ~'lots-o-fns
     ~(vec
        (for [i (range simpler-test-runs)]
          `(fn [x#] x#)))))

;; 3000:
;; R: 365
;; HUH.

;; 8000:
;; R: 1319

;; (simpler-test)


;; ==================================================
;;

(def simpler-test-meta-runs 3000)

(defmacro simpler-test-meta []
  `(def ~'lots-o-fns
     ~(vec
        (for [i (range simpler-test-meta-runs)]
          `(with-meta (fn [x#] x#)
             {:flag :anything})))))

;; 3000:
;; R: 12166


;; (simpler-test-meta)


;; ==================================================
;;

(def simpler-test-vec-map-runs 3000)

(defmacro simpler-test-vec-map []
  `(def ~'lots-o-fns
     ~(vec
        (for [i (range simpler-test-vec-map-runs)]
          `[(fn [x#] x#) {:flag ~i}]))))

;; 3000:
;; 501 ms

;; huuuuhhhh...

;; (simpler-test-vec-map)



;; ============================================================
;;

(def simpler-test-bigfun-runs 3000)

(defmacro simpler-test-bigfun []
  `(def ~'lots-o-fns
     ~(vec
        (for [i (range simpler-test-bigfun-runs)]
          `(fn [x#]
             (last
               (take 300
                 (for [smoo# (partition 2 1 (range 300))
                       kump# (concat smoo# (reverse smoo#))
                       :when (< (count smoo#) (count kump#))
                       :let [twun# 7]]
                   (loop [y# (int 0)]
                     (if (< y# twun#)
                       (recur (inc y#))
                       x#))))))))))


(simpler-test-bigfun)

;; 3000:
;; R: 

(comment
  (arcadia.compiler/aot-namespaces
    "Assets/Arcadia/Compiled"
    '[timsg.startup-time]))
