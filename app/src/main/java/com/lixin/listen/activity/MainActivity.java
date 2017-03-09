package com.lixin.listen.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lixin.listen.R;
import com.lixin.listen.common.Constant;
import com.lixin.listen.dialog.OperatingDlg;
import com.lixin.listen.event.RefreshEvent;
import com.lixin.listen.event.StopPlayingEvent;
import com.lixin.listen.fragment.MeFragment;
import com.lixin.listen.fragment.MusicFragment;
import com.lixin.listen.fragment.StartFragment;
import com.lixin.listen.util.BusProvider;
import com.lixin.listen.util.DialogHelper;
import com.lixin.listen.util.MediaPlayerUtil;
import com.lixin.listen.util.ProgressDialog;
import com.squareup.otto.Subscribe;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_info)
    ImageView ivInfo;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private int imageResId[] = {R.drawable.img_tab_start, R.drawable.img_tab_music, R.drawable.img_tab_me};

    private String[] titles = {"听曲", "我的曲库", "个人中心"};

    private Fragment[] fragments;
    private StartFragment startFragment;
    private MusicFragment musicFragment;
    private MeFragment meFragment;

    private OperatingDlg operatingDlg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        BusProvider.getInstance().register(this);
        initViews();
        MediaPlayerUtil.getInStance().init();

    }

    private void initViews() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        final SampleFragmentPagerAdapter pagerAdapter =
                new SampleFragmentPagerAdapter(getSupportFragmentManager(), this);

        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvTitle.setText(titles[position]);
                if (position == 1) {
                    ivInfo.setVisibility(View.VISIBLE);
                } else {
                    ivInfo.setVisibility(View.GONE);
                    MediaPlayerUtil.getInStance().pause();
                    BusProvider.getInstance().post(new StopPlayingEvent());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(pagerAdapter.getTabView(i));
            }
        }

        tabLayout.getTabAt(0).getCustomView().setSelected(true);

        startFragment = new StartFragment();
        musicFragment = new MusicFragment();
        meFragment = new MeFragment();
        fragments = new Fragment[]{startFragment, musicFragment, meFragment};


        operatingDlg = new OperatingDlg(this);

    }

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 3;
        private Context context;

        public View getTabView(int position) {
            View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
            ImageView img = (ImageView) v.findViewById(R.id.imageView);
            img.setBackgroundResource(imageResId[position]);

            return v;
        }

        public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {

            return fragments[position];
        }

    }

    @Subscribe
    public void onEvent(RefreshEvent event) {

        viewPager.setCurrentItem(1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                musicFragment.setClick();
            }
        }, 500);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
        operatingDlg.disDialog();
    }

    @OnClick(R.id.iv_info)
    public void ivInfo() {
        operatingDlg.showDialog();
    }


}
