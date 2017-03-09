package com.lixin.listen.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 保存数据工具类
 */
public class PrefsUtil {
    private static final String PREFS_NAME = "xiaozhiq";

    private static SharedPreferences sInstance;

    public static boolean putString(Context context, String key, String value) {
        if (sInstance == null) {
            sInstance = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getString(Context context, String key, String defValue) {
        if (sInstance == null) {
            sInstance = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }
        String value = sInstance.getString(key, defValue);
        return value;
    }

    public static boolean putLong(Context context, String key, long value) {
        if (sInstance == null) {
            sInstance = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public static long getLong(Context context, String key, long defValue) {
        if (sInstance == null) {
            sInstance = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }

        return sInstance.getLong(key, defValue);
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        if (sInstance == null) {
            sInstance = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }

        return sInstance.getBoolean(key, defValue);
    }

    public static boolean putBoolean(Context context, String key, boolean value) {
        if (sInstance == null) {
            sInstance = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }
}
