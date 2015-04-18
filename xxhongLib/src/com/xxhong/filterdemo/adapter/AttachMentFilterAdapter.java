package com.xxhong.filterdemo.adapter;

import java.util.List;

import com.xxhong.filterdemo.domain.CustomFilterItem;
import com.xxhong.lib.R;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AttachMentFilterAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater lf;
	private List<CustomFilterItem> filters;
	private int mPosition;
	public void setPosition(int position){
		this.mPosition =position;
	}
	public AttachMentFilterAdapter(Context context, List<CustomFilterItem> filters) {
		super();
		this.context = context;
		this.filters = filters;
		lf = LayoutInflater.from(context);
	}
	public  List<CustomFilterItem> getFilters(){
		return filters;
	}
	@Override
	public int getCount() {
		return filters.size();
	}

	@Override
	public Object getItem(int position) {
		return filters.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view ;
		if(convertView!=null){
			view =convertView;
		}else{
			view = lf.inflate(R.layout.attachment_custom_filter_popview_item, null);
		}
//		ImageView ivNaro = (ImageView) view.findViewById(R.id.iv_attachment_custom_filter_imageView);
//		if(mPosition == position ){
//			ivNaro.setVisibility(View.VISIBLE);
//		}else{
//			ivNaro.setVisibility(View.GONE);
//		}
		TextView tvName = (TextView) view.findViewById(R.id.tv_attachment_custom_filter_name);
		CustomFilterItem customFilter = filters.get(position);
		tvName.setText(customFilter.getName());
		return view;
	}

}
