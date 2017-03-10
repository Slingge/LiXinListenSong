package com.lixin.listen.httpRequest;

import android.content.Context;

import com.lixin.listen.common.Constant;
import com.lixin.listen.util.PrefsUtil;
import com.lixin.listen.util.ProgressDialog;
import com.lixin.listen.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * 获取服务器文件数
 * Created by Slingge on 2017/3/9 0009.
 */

public class ServiceFileNumRequest {

    private Context context;


    public interface ServiceFileNumCallBack {
        void serviceFiles(String num);
    }

    public ServiceFileNumCallBack serviceFileNumCallBack;

    public void setServiceFileNumCallBack(ServiceFileNumCallBack serviceFileNumCallBack) {
        this.serviceFileNumCallBack = serviceFileNumCallBack;
    }

    public ServiceFileNumRequest(Context context) {
        this.context = context;
    }


    public void getServiceFile() {
        ProgressDialog.showProgressDialog(context, null);
        String json = "{\"cmd\":\"getPersonalInfo\",\"uid\":\"" + PrefsUtil.getString(context, "userid", "") + "\"}";
        OkHttpUtils.post().url(Constant.URL).addParams("json", json).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtil.showToast("网络错误");
                ProgressDialog.dismissDialog();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("result").equals("0")) {
                        String userInfo = obj.getString("userInfo");
                        obj = new JSONObject(userInfo);
                        String fileCount = obj.getString("fileCount");
                        serviceFileNumCallBack.serviceFiles(fileCount);
                    } else {
                        ToastUtil.showToast(obj.getString("resultNote"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ProgressDialog.dismissDialog();
            }
        });

    }
}
