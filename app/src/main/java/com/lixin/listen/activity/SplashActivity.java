package com.lixin.listen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.lixin.listen.R;
import com.lixin.listen.util.PrefsUtil;
import com.lixin.listen.util.ProgressDialog;
import com.tencent.mm.sdk.openapi.SendAuth;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lixin.listen.activity.Application.api;

public class SplashActivity extends AbsBaseActivity {

    @Bind(R.id.iv_bottom)
    ImageView ivBottom;
    @Bind(R.id.iv_login)
    ImageView ivLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        // 已经登陆过一次
        if (!TextUtils.isEmpty(PrefsUtil.getString(SplashActivity.this, "userid", ""))) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
//      else {
//            // 删除本地所有文件
//            try {
//                List<File> files = DirTraversal.listFiles(AppHelper.getInnerFilePath(SplashActivity.this) + File.separator
//                        + getPackageName());
//                for (int i = 0; i < files.size(); i++) {
//
//                    File file = files.get(i);
//                    if (file.exists()) {
//                        file.delete();4
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

    }

    @OnClick(R.id.iv_login)
    public void doLogin() {
        ProgressDialog.showProgressDialog(this, "正在跳转...");
        if (!TextUtils.isEmpty(PrefsUtil.getString(SplashActivity.this, "userid", ""))) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            if (!api.isWXAppInstalled()) {
                Toast.makeText(SplashActivity.this, "您还未安装微信客户端",
                        Toast.LENGTH_SHORT).show();
                ProgressDialog.dismissDialog();
                return;
            }
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo_test";
            api.sendReq(req);//执行完毕这句话之后，会在WXEntryActivity回调
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        ProgressDialog.dismissDialog();
    }
}
