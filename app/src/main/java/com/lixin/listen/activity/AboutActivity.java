package com.lixin.listen.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lixin.listen.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.ll_title)
    LinearLayout llTitle;
    @Bind(R.id.iv)
    ImageView iv;
    @Bind(R.id.tv)
    TextView tv;
    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.activity_about)
    RelativeLayout activityAbout;
    @Bind(R.id.iv_shadow)
    ImageView ivShadow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("关于我们");
        ivShadow.setVisibility(View.GONE);
    }
}
