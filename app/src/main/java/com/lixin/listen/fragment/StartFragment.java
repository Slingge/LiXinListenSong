package com.lixin.listen.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lixin.listen.R;
import com.lixin.listen.activity.RecordActivity;
import com.lixin.listen.bean.RequestVO;
import com.lixin.listen.bean.StartRecordVo;
import com.lixin.listen.common.Constant;
import com.lixin.listen.util.PrefsUtil;
import com.lixin.listen.util.ProgressDialog;
import com.lixin.listen.util.ToastUtil;
import com.lixin.listen.view.ClickImageView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment implements ClickImageView.ClickImageViewCallBack{

    ClickImageView ivStartRecord;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        ButterKnife.bind(this, view);
        ivStartRecord= (ClickImageView) view.findViewById(R.id.iv_start_record);
        ivStartRecord.setClickImageViewCallBack(this);
        return view;
    }



    StartRecordVo vo;

    private void doStart() {
        ProgressDialog.showProgressDialog(getActivity(), null);
        String url = Constant.URL + "json=" + new Gson().toJson(new RequestVO("getTimeLength", PrefsUtil.getString(getActivity(), "userid", "")));
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "服务器异常,请稍后重试", Toast.LENGTH_SHORT).show();
                        ProgressDialog.dismissDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        vo = new Gson().fromJson(response, StartRecordVo.class);
                        if (vo.getResult().equals("0")) {
                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                Intent intent = new Intent(getActivity(), RecordActivity.class);
                                intent.putExtra("vo", vo);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getActivity(), "请开启录音权限", Toast.LENGTH_LONG).show();
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            }
                            ProgressDialog.dismissDialog();
                        } else {
                            ProgressDialog.dismissDialog();
                            ToastUtil.showToast(vo.getResultNote());
                        }
                        ProgressDialog.dismissDialog();
                    }
                });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(getActivity(), RecordActivity.class);
                    intent.putExtra("vo", vo);
                    startActivity(intent);
                } else {
                    dialog(getActivity(), "需要录制音频和写入内存卡权限");//申请权限dialog，跳转权限页面
                }
                break;
        }
    }


    public void dialog(final Context context, String title) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage("是否开启？");
        dialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0,
                                        int arg1) {
                        // TODO Auto-generated method stub
                        Uri packageURI = Uri.parse("package:" + context.getPackageName());
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                        context.startActivity(intent);
                    }
                });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0,
                                int arg1) {
            }
        });

        dialog.show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void clickImage() {
        doStart();
    }
}
