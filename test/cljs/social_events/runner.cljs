(ns social-events.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [social-events.core-test]))

(doo-tests 'social-events.core-test)
