package com.lixin.listen.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lixin.listen.R;

/**
 * Created by LiPeng on 2017/1/6.
 */

public class AbsBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    public void initToolbar(boolean showHomeAsUp) {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.titleLayout);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(showHomeAsUp);
//        if (showHomeAsUp) {
//            Drawable indicator = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
//            getSupportActionBar().setHomeAsUpIndicator(indicator);
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    /**
     * 判断网络是否能用
     *
     * @return true/false
     */
    public boolean isNetAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return (info != null && info.isAvailable());
    }
}
