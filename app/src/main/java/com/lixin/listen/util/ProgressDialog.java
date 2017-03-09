package com.lixin.listen.util;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lixin.listen.R;


/**
 * 类说明: 自定义ProgressDialog
 */
public class ProgressDialog {

    public static android.app.ProgressDialog proDialog;

    public static Dialog progressDlg;

    public static void dismissDialog() {
        if (proDialog != null && proDialog.isShowing()) {
            proDialog.dismiss();
            proDialog = null;
        }
    }


    public static void showProgressDialog(Context context, String msg) {
        if (proDialog == null) {
            if (msg == null) {
                proDialog = android.app.ProgressDialog.show(context, null, "加载中...");
            } else {
                proDialog = android.app.ProgressDialog.show(context, null, msg);
            }
            proDialog.setProgressStyle(android.app.ProgressDialog.STYLE_HORIZONTAL);
            proDialog.setCancelable(true);//点击返回消失
            proDialog.setCanceledOnTouchOutside(false);//点击屏幕不消失
            proDialog.show();
        }
    }



}
