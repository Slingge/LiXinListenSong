package com.lixin.listen.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/9.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    private int layoutId;
    private int select = 0;

    public CommonAdapter(Context context, List<T> datas, int layoutId)
    {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        if (datas == null){
            this.mDatas = new ArrayList<>();
        }
        else {
            this.mDatas = datas;
        }

        this.layoutId = layoutId;
    }

    public void loadDataList(List<T> list){
        this.mDatas = list;
        notifyDataSetChanged();
    }

    public void refreshDataList(List<T> list){
        this.mDatas.clear();
        this.mDatas = list;
        notifyDataSetChanged();
    }

    public void refreshSingList(List<T> list){
        List<T> temp = list;
        this.mDatas.clear();
        this.mDatas.addAll(temp);
        notifyDataSetChanged();
    }

    public void addDataList(List<T> list){
        this.mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public void clearDataList(){
        this.mDatas.clear();
        notifyDataSetChanged();
    }

    public List<T> getDataList(){
        return mDatas;
    }

    public void deleteItem(int position){
        List<T> newArr = new ArrayList<>();
        for (int i=0; i<mDatas.size(); i++){
            T t = mDatas.get(i);
            if ( position != i){
                newArr.add(t);
            }
        }
        this.mDatas.clear();
        this.mDatas.addAll(newArr);
        notifyDataSetChanged();
    }

    public void addSignItem(String path){

    }

    @Override
    public int getCount()
    {
        return mDatas.size();
    }

    @Override
    public T getItem(int position)
    {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = ViewHolder.get(mContext, convertView, parent,
                layoutId, position);
        convert(holder, getItem(position));
        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, T t);
}
