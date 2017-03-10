package com.lixin.listen.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lixin.listen.BaseFragment;
import com.lixin.listen.R;
import com.lixin.listen.activity.AboutActivity;
import com.lixin.listen.activity.FankuiActivity;
import com.lixin.listen.activity.UserInfoActivity;
import com.lixin.listen.bean.DaySignalVO;
import com.lixin.listen.bean.MyVO;
import com.lixin.listen.bean.RequestVO;
import com.lixin.listen.common.Constant;
import com.lixin.listen.httpRequest.ServiceFileNumRequest;
import com.lixin.listen.util.AppHelper;
import com.lixin.listen.util.DirTraversal;
import com.lixin.listen.util.GlideCircleTransform;
import com.lixin.listen.util.PrefsUtil;
import com.lixin.listen.util.UpdateManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

import static android.app.Activity.RESULT_OK;

/**
 * 个人中心
 */
public class MeFragment extends BaseFragment implements ServiceFileNumRequest.ServiceFileNumCallBack {

    @Bind(R.id.ll_fankui)
    LinearLayout llFankui;
    @Bind(R.id.ll_about)
    LinearLayout llAbout;
    @Bind(R.id.iv_header)
    CircleImageView ivHeader;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.tv_local)
    TextView tvLocal;
    @Bind(R.id.tv_weiguilei)
    TextView tvWeiguilei;
    @Bind(R.id.tv_service)
    TextView tvService;
    @Bind(R.id.tv_time_count)
    EditText tvTimeCount;
    @Bind(R.id.btn_reduce)
    Button btnReduce;
    @Bind(R.id.btn_plus)
    Button btnPlus;
    @Bind(R.id.rl_update)
    RelativeLayout rlUpdate;

    private ServiceFileNumRequest serviceFileRequest;

    private static final String PLUS = "0";
    private static final String REDUCE = "1";

    private Dialog dialog;
    View view;
    private Toast mToast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        getParams();
        initViews();
        initData();
        if (TextUtils.isEmpty(PrefsUtil.getString(getActivity(), "first_set_time", ""))) {
            doRequest("30分钟");
        }
        serviceFileRequest = new ServiceFileNumRequest(getActivity());
        serviceFileRequest.setServiceFileNumCallBack(this);
        serviceFileRequest.getServiceFile();
        return view;
    }


    private void getParams() {
        final List<File> files = DirTraversal.listFiles(AppHelper.getInnerFilePath(getActivity()) + File.separator
                + getActivity().getPackageName());


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (files == null)
                    return;
                int weiguilei = 0;
                for (File file : files) {
                    if (file.getName().contains("weiguilei")) {
                        ++weiguilei;
                    }
                }
                ((TextView) view.findViewById(R.id.tv_local)).setText(files.size() + "");
                ((TextView) view.findViewById(R.id.tv_weiguilei)).setText(weiguilei + "");
            }
        }, 1000);

    }

    private void initViews() {


        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "MyriadPro-BoldCond.otf");
        tvLocal.setTypeface(tf);
        tvWeiguilei.setTypeface(tf);
        tvService.setTypeface(tf);

        tvTimeCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        doRequest(s.toString());
//                    }
//                }, 10000);

            }
        });
        tvTimeCount.setEnabled(false);

        //如果登录过就先读取缓存的头像和昵称，以及每日目标
        doSetHeaderAndNickName();
    }

    private void doSetHeaderAndNickName() {
        String nickname = PrefsUtil.getString(getActivity(), "nickname", "");
        String avatar = PrefsUtil.getString(getActivity(), "avatar", "");
        String daycount = PrefsUtil.getString(getActivity(), "daycount", "");
        Glide.with(getActivity()).load(avatar).dontAnimate().transform(new GlideCircleTransform(getActivity())).into(ivHeader);
        tvNickname.setText(nickname);
        tvTimeCount.setText(daycount + "分钟");
    }

    Handler handler = new Handler();

    /**
     * 设置每日时间
     *
     * @param s
     */
    private void doRequest(CharSequence s) {
        RequestVO vo = new RequestVO();
        vo.setCmd("updateDayCountAll");
        vo.setUid(PrefsUtil.getString(getActivity(), "userid", ""));
        vo.setDayCount(s.toString().replace("分钟", ""));

        String url = Constant.URL + "json=" + new Gson().toJson(vo);
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        ShowToast("服务器异常,请稍后重试");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        DaySignalVO vo = new Gson().fromJson(response, DaySignalVO.class);
                        tvTimeCount.setText(vo.getDayCount() + "分钟");
                        PrefsUtil.putString(getActivity(), "first_set_time", "30");
                    }

                });
    }

    private void initData() {

        String url = Constant.URL + "json=" + new Gson().toJson(new RequestVO("getPersonalInfo", PrefsUtil.getString(getActivity(), "userid", "")));
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        ShowToast("服务器异常,请稍后重试");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        MyVO vo = new Gson().fromJson(response, MyVO.class);
                        updataViews(vo);
                    }

                });
    }

    private void updataViews(MyVO vo) {
        Glide.with(getActivity()).load(vo.getUserInfo().getUserIcon()).dontAnimate().transform(new GlideCircleTransform(getActivity())).into(ivHeader);
        tvNickname.setText(vo.getUserInfo().getNickName());
        tvService.setText(vo.getUserInfo().getFileCount());
        tvTimeCount.setText(vo.getUserInfo().getDayCount() + "分钟");
        PrefsUtil.putString(getActivity(), "avatar", vo.getUserInfo().getUserIcon());
        PrefsUtil.putString(getActivity(), "nickname", vo.getUserInfo().getNickName());
        PrefsUtil.putString(getActivity(), "daycount", vo.getUserInfo().getDayCount());
    }

    private void doRequestTime(final String type) {

        if (Integer.valueOf(tvTimeCount.getText().toString().replace("分钟", "")) == 5 && type.equals(REDUCE)) {
            ShowToast("每日目标最低为5分钟");
            return;
        }

        if (type.equals(REDUCE)) {
            btnReduce.setEnabled(false);
        }

        RequestVO vo = new RequestVO();
        vo.setCmd("updateDayCount");
        vo.setUid(PrefsUtil.getString(getActivity(), "userid", ""));
        vo.setType(type);

        String url = Constant.URL + "json=" + new Gson().toJson(vo);
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        ShowToast("服务器异常,请稍后重试");
                        btnReduce.setEnabled(true);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        DaySignalVO vo = new Gson().fromJson(response, DaySignalVO.class);
                        ShowToast(vo.getResultNote());
                        tvTimeCount.setText(vo.getDayCount() == null ? "0分钟" : vo.getDayCount() + "分钟");
                        btnReduce.setEnabled(true);
                    }

                });
    }

    @OnClick(R.id.btn_plus)
    public void btnPlus() {
//        int time = Integer.valueOf(tvTimeCount.getText().toString().replace("分钟", ""));
//        tvTimeCount.setText((time + 5) + "分钟");

        doRequestTime(PLUS);
    }

    @OnClick(R.id.btn_reduce)
    public void setBtnReduce() {
//
//        int time = Integer.valueOf(tvTimeCount.getText().toString().replace("分钟", ""));
//        if (time <= 0) {
//            tvTimeCount.setText("0分钟");
//        } else {
//            tvTimeCount.setText((time - 5) + "分钟");
//        }


        doRequestTime(REDUCE);

    }

    @OnClick(R.id.ll_fankui)
    public void toFankui() {
        startActivity(new Intent(getActivity(), FankuiActivity.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000 && resultCode == RESULT_OK) {
            initData();
        }
    }

    @OnClick(R.id.ll_about)
    public void toAbout() {
        startActivity(new Intent(getActivity(), AboutActivity.class));
    }

    @OnClick(R.id.rl_update)
    public void rlUpdate() {
        new UpdateManager(getActivity()).checkUpdate(getActivity());
    }

    DialogInterface.OnClickListener posi = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    };

    DialogInterface.OnClickListener nega = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }

    };

    @OnClick(R.id.iv_header)
    public void doEditUserInfo() {
        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
        startActivityForResult(intent, 2000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void ShowToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
            mToast.show();
        } else {
            mToast.setText(text);
            mToast.show();
        }
    }

    @Override
    public void serviceFiles(String num) {
        tvService.setText(num);
    }

    @Override
    public void loadData() {
        serviceFileRequest.getServiceFile();
    }
}
