package com.lixin.listen.util;

import android.util.Log;


/**
 * Created by Slingge on 2017/2/21 0021.
 */

public class abLog {

    public static Boolean E = true;


    public static void e(String tag, String text) {
        if (E) {
            Log.e(tag, text);
        }
    }

}
