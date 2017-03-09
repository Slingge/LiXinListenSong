package com.lixin.listen.util;

import com.squareup.otto.Bus;

/**
 * Created by admin on 2017/1/19.
 */

public final class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }
}
