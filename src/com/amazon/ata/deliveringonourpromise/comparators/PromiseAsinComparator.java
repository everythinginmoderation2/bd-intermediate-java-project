package com.amazon.ata.deliveringonourpromise.comparators;

import com.amazon.ata.deliveringonourpromise.types.Promise;

import java.util.Comparator;

public class PromiseAsinComparator implements Comparator<Promise> {

    @Override
    public int compare(Promise promise1, Promise promise2) {
        String promiseAsin1 = promise1.getAsin();
        String promiseAsin2 = promise2.getAsin();
        if (!promiseAsin1.equals(promiseAsin2)) {
            return promiseAsin1.compareTo(promiseAsin2);
        }
        return 0;
    }
}
