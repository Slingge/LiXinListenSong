package com.lixin.listen.httpRequest;

import android.content.Context;

import com.lixin.listen.common.Constant;
import com.lixin.listen.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * 获取专辑Id
 * Created by Slingge on 2017/3/14 0014.
 */

public class GetZhuanJiIdRequset {

    private Context context;

    public GetZhuanJiIdRequset(Context context) {
        this.context = context;
    }

    public interface ZhuanJiIdCallBack {
        void ZhuanJiId(String firstTypeId, String secondTypeId, String thirdTypeId);
    }

    public ZhuanJiIdCallBack zhuanJiIdCallBack;

    public void setZhuanJiIdCallBack(ZhuanJiIdCallBack zhuanJiIdCallBack) {
        this.zhuanJiIdCallBack = zhuanJiIdCallBack;
    }

    public void ZhuanJiId(String id) {
        String json = "{\"cmd\":\"getMessage\",\"albumId\":\"" + id + "\"}";
        OkHttpUtils.post().url(Constant.URL).addParams("json", json).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                zhuanJiIdCallBack.ZhuanJiId("", "", "");
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("result").equals("0")) {
                        zhuanJiIdCallBack.ZhuanJiId(obj.getString("firstTypeId"), obj.getString("secondTypeId"), obj.getString("thirdTypeId"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }


}
