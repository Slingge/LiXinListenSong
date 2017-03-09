package com.lixin.listen.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.File;

/**
 * Created by admin on 2017/1/15.
 */

public class AppHelper {

    public static int getVersionCode(Context context) throws Exception
    {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
        ;
        return packInfo.versionCode;
    }

    public static File getInnerFilePath(Context context){
        return context.getFilesDir();
    }
}
