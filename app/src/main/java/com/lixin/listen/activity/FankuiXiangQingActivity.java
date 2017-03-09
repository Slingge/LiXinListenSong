package com.lixin.listen.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lixin.listen.R;
import com.lixin.listen.bean.DaySignalVO;
import com.lixin.listen.bean.FankuixiangqingVO;
import com.lixin.listen.bean.RequestVO;
import com.lixin.listen.common.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class FankuiXiangQingActivity extends AppCompatActivity {

    String questionId = "";
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.ll_title)
    LinearLayout llTitle;
    @Bind(R.id.activity_fankui_xiang_qing)
    LinearLayout activityFankuiXiangQing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fankui_xiang_qing);
        ButterKnife.bind(this);
        getParams();
        initViews();
        loadData();
    }

    private void loadData() {
        RequestVO vo = new RequestVO();
        vo.setCmd("getQuestionDetail");
        vo.setQuestionId(questionId);
        String url = Constant.URL + "json=" + new Gson().toJson(vo);
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        Toast.makeText(FankuiXiangQingActivity.this, "服务器异常,请稍后重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        FankuixiangqingVO vo = new Gson().fromJson(response, FankuixiangqingVO.class);
                        Toast.makeText(FankuiXiangQingActivity.this, vo.getResultNote(), Toast.LENGTH_SHORT).show();
                    }

                });

    }

    private void initViews() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("反馈详情");
    }

    private void getParams() {
        questionId = getIntent().getStringExtra("questionId");
    }
}
