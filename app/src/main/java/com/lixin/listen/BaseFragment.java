package com.lixin.listen;


import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * 是没有title的Fragment
 */
public abstract class BaseFragment extends Fragment {


    protected boolean isViewInitiated;
    protected boolean isVisibleToUser;
    protected boolean isDataInitiated;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }

    public abstract void loadData();

    public static boolean refreshData;

    public boolean prepareFetchData() {
        return prepareFetchData(refreshData);
    }

    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            loadData();
            isDataInitiated = true;
            refreshData = true;//false,只加载一次，true每次进入页面都加载
            return true;
        }
        return false;
    }

}
