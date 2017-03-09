package com.lixin.listen.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lixin.listen.R;
import com.lixin.listen.bean.MusicBean;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuileiActivity extends Activity {

    @Bind(R.id.iv_guilei)
    ImageView ivGuilei;
    @Bind(R.id.activity_guilei)
    RelativeLayout activityGuilei;

    MusicBean musicBean;
    @Bind(R.id.iv_close)
    ImageView ivClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guilei);
        ButterKnife.bind(this);
        musicBean = (MusicBean) getIntent().getSerializableExtra("vo");


    }

    @OnClick(R.id.iv_guilei)
    public void doGuilei() {
        Intent intent = new Intent();
        intent.putExtra("vo", musicBean);
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.iv_close)
    public void doClose(){
        finish();
    }


}
