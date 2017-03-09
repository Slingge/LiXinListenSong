package com.lixin.listen.activity;

import com.lixin.listen.common.Constant;
import com.lixin.listen.util.abLog;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by admin on 2017/1/2.
 */
public class Application extends android.app.Application {

    public static IWXAPI api;
    private static Application myApplication;

    public static Application getInstance() {
        // if语句下是不会走的，Application本身已单例
        if (myApplication == null) {
            synchronized (Application.class) {
                if (myApplication == null) {
                    myApplication = new Application();
                }
            }
        }
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        abLog.E = true;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);

        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, true);
        api.registerApp(Constant.APP_ID);
    }


}
