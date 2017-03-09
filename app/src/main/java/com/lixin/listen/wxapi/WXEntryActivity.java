package com.lixin.listen.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lixin.listen.activity.Application;
import com.lixin.listen.activity.MainActivity;
import com.lixin.listen.bean.LoginVo;
import com.lixin.listen.bean.RequestVO;
import com.lixin.listen.bean.TokenVo;
import com.lixin.listen.bean.UserInfoVo;
import com.lixin.listen.common.Constant;
import com.lixin.listen.util.PrefsUtil;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by admin on 2017/1/11.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = WXEntryActivity.class.getSimpleName();

    private Bundle bundle;
    //获取用户个人信息
    public static String GetUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Application.api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Application.api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, true);
        Application.api.registerApp(Constant.APP_ID);
        Application.api.handleIntent(getIntent(), WXEntryActivity.this);  //必须调用此句话
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        bundle = getIntent().getExtras();
        SendAuth.Resp resp = new SendAuth.Resp(bundle);
        //获取到code之后，需要调用接口获取到access_token
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            String code = resp.token;
//            if(BaseApplication.isWxLogin){
            getToken(code);
//            } else{
//                WXEntryActivity. this.finish();
//            }
        } else {
            WXEntryActivity.this.finish();
        }
    }

    private void getToken(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + Constant.APP_ID
                + "&secret="
                + Constant.SECRET
                + "&code="
                + code
                + "&grant_type=authorization_code";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(WXEntryActivity.this, "服务器异常,请稍后重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String str = response;
                        TokenVo vo = new Gson().fromJson(str, TokenVo.class);
                        getUserMesg(vo.getAccess_token(), vo.getOpenid());
                    }
                });

    }

    private void getUserMesg(String access_token, String openid) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(WXEntryActivity.this, "服务器异常,请稍后重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String str = response;
                        final UserInfoVo vo = new Gson().fromJson(str, UserInfoVo.class);
                        doLogin(vo);
                    }
                });
    }

    private void doLogin(UserInfoVo vo) {
        String json = "json=" + new Gson().toJson(new RequestVO("thirdLogin", vo.getUnionid(), vo.getNickname(), vo.getHeadimgurl()));
        OkHttpUtils
                .get()
                .url(Constant.URL+ json)
                .build()
                .execute(new StringCallback()
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(WXEntryActivity.this, "服务器异常,请稍后重试", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            String str = response;
                            LoginVo loginVo = new Gson().fromJson(str, LoginVo.class);
                            if (loginVo.getResult().endsWith("0")){
                                PrefsUtil.putString(WXEntryActivity.this, "userid", loginVo.getUid());
                                startActivity(new Intent(WXEntryActivity.this, MainActivity.class));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }


}
