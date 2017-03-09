package com.lixin.listen.widget.recyclerview;

/**
 * Created by Administrator on 2016/8/3.
 */
public interface ItemViewDelegate<T>
{

	int getItemViewLayoutId();

	boolean isForViewType(T item, int position);

	void convert(ViewHolder holder, T t, int position);

}