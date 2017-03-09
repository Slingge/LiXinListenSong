package com.lixin.listen.widget.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/3.
 */
public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T>
{
	protected Context mContext;
	protected int mLayoutId;
	protected List<T> mDatas;
	protected LayoutInflater mInflater;

	public CommonAdapter(final Context context, List<T> dataList, final int layoutId)
	{
		super(context, dataList);
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mLayoutId = layoutId;
		if (dataList == null){
			mDatas = new ArrayList<>();
		}
		else {
			mDatas = dataList;
		}

		addItemViewDelegate(new ItemViewDelegate<T>()
		{
			@Override
			public int getItemViewLayoutId()
			{
				return layoutId;
			}

			@Override
			public boolean isForViewType( T item, int position)
			{
				return true;
			}

			@Override
			public void convert(ViewHolder holder, T t, int position)
			{
				CommonAdapter.this.convert(holder, t, position);
			}
		});
	}


	public void loadData(List<T> mDatas){
		this.mDatas.addAll(mDatas);
		notifyDataSetChanged();
	}


	protected abstract void convert(ViewHolder holder, T t, int position);


}