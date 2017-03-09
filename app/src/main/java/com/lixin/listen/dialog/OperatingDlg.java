package com.lixin.listen.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;

import com.lixin.listen.R;
import com.lixin.listen.common.Constant;
import com.lixin.listen.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * 操作说明
 * Created by Slingge on 2017/3/9 0009.
 */

public class OperatingDlg {

    private Activity context;
    AlertDialog builder;

    private WebView webView;

    public OperatingDlg(Activity context) {
        this.context = context;
    }


    public void showDialog() {
        builder = new AlertDialog.Builder(context, R.style.Dialog).create(); // 先得到构造器
        builder.show();
        View view = LayoutInflater.from(context).inflate(R.layout.dlg_operating_instructions, null);
        builder.getWindow().setContentView(view);
        builder.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        ImageView image_cancal = (ImageView) view.findViewById(R.id.image_cancel);
        image_cancal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
            }
        });

        webView = (WebView) view.findViewById(R.id.webview);
//        getOperating();

        Window dialogWindow = builder.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);//显示在底部
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.8); // 高度设置为屏幕的0.5
        p.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);


    }

//    private void getOperating() {
//        com.lixin.listen.util.ProgressDialog.showProgressDialog(context, null);
//        String json = "{\"cmd\":\"getExplain\"" + "}";
//        OkHttpUtils.post().url(Constant.URL).addParams("json", json).build().execute(new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                com.lixin.listen.util.ProgressDialog.dismissDialog();
//                ToastUtil.showToast("网路错误，获取操作说明失败");
//            }
//
//            @Override
//            public void onResponse(String response, int id) {
//                com.lixin.listen.util.ProgressDialog.dismissDialog();
//                try {
//                    JSONObject obj = new JSONObject(response);
//                    if (obj.getString("result").equals("0")) {
//                        webView.loadUrl(obj.getString("newAboutMe"));
//                    } else {
//                        ToastUtil.showToast(obj.getString("resultNote") + "，获取操作说明失败");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//    }


    public void disDialog() {
        if (builder != null) {
            builder.dismiss();
            builder = null;
        }

    }


}
