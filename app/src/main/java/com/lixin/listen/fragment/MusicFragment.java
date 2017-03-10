package com.lixin.listen.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.lixin.listen.R;
import com.lixin.listen.util.MediaPlayerUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MusicFragment extends Fragment {

    @Bind(R.id.remote)
    View remote;
    @Bind(R.id.ll_remote)
    LinearLayout llRemote;
    @Bind(R.id.local)
    View local;
    @Bind(R.id.ll_local)
    LinearLayout llLocal;
    @Bind(R.id.id_content)
    FrameLayout idContent;

    LocalFileFragment localFileFragment = new LocalFileFragment();
    ServerFileFragment serverFileFragment = new ServerFileFragment();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        ButterKnife.bind(this, view);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.id_content, serverFileFragment);
        transaction.commit();
        return view;
    }


    public void setClick() {
        doLoacl();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                localFileFragment.setClick();
            }
        }, 500);

    }

    @OnClick(R.id.ll_remote)
    public void doClickRemote() {
        remote.setVisibility(View.VISIBLE);
        local.setVisibility(View.GONE);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.id_content, serverFileFragment);
        transaction.commit();

        MediaPlayerUtil.getInStance().pause();
    }

    @OnClick(R.id.ll_local)
    public void doLoacl() {
        remote.setVisibility(View.GONE);
        local.setVisibility(View.VISIBLE);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.id_content, localFileFragment);
        transaction.commit();

        MediaPlayerUtil.getInStance().pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
