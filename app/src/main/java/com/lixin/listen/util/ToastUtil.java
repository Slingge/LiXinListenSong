package com.lixin.listen.util;

import android.widget.Toast;

import com.lixin.listen.activity.Application;


/**
 * 单例Toast
 */

public class ToastUtil {

    private static Toast mToast;

    public static void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(Application.getInstance(), text, Toast.LENGTH_SHORT);
        }
        mToast.setText(text);
        mToast.show();
    }


    public static void showLongToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(Application.getInstance(), text, Toast.LENGTH_LONG);
        }
        mToast.setText(text);
        mToast.show();
    }

}
