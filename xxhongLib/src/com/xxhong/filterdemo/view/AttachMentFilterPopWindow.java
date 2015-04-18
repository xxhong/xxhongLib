package com.xxhong.filterdemo.view;


import java.util.ArrayList;
import java.util.List;

import com.xxhong.filterdemo.adapter.AttachMentFilterAdapter;
import com.xxhong.filterdemo.domain.CustomFilterItem;
import com.xxhong.lib.R;
import com.xxhong.lib.uitl.AndroidUtil;

import android.R.anim;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout.LayoutParams;
/**
 * new this
 * setAdapter
 * setOnItemClickListener
 * show(view);
 * @author xxhong
 *
 */
public class AttachMentFilterPopWindow {
	public interface PopWindowDismissListener {
		void onDismiss();
	}
	public interface PopWindowOnItemClickListener{
		void onItemClick(CustomFilterItem customFilter);
	}
	PopWindowOnItemClickListener popWindowOnItemClickListener;
	public  PopWindowDismissListener onDismissListener;
	private Context mContext;
	private PopupWindow mPopupWindow;
	private LayoutInflater lf;
	private GridView lvMain;
	private int currPosition;
	private AttachMentFilterAdapter mAdapter;
	public AttachMentFilterPopWindow(Context context) {
		this.mContext = context;
		lf = LayoutInflater.from(context);
		initPopuptWindow();
	}
	public void setAttachMentFilterAdapter(AttachMentFilterAdapter adapter){
		this.mAdapter =  adapter;
		lvMain.setAdapter(adapter);
	}
	
	/*
	 * 创建PopupWindow
	 */
	private void initPopuptWindow() {
		View view = lf.inflate(R.layout.attachment_custom_filter_popview, null);
		lvMain = (GridView) view.findViewById(R.id.gv_popfilter);
//		lvMain.setDividerHeight(1);
//		lvMain.setDivider(mContext.getResources().getDrawable(R.drawable.list_divider));
		mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,false);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.update();
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		lvMain.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(mAdapter==null){
					return ;
				}
				List<CustomFilterItem> filters = mAdapter.getFilters();
				if(popWindowOnItemClickListener!=null&&filters!=null&&filters.size()>0){
					popWindowOnItemClickListener.onItemClick(filters.get(arg2));
					currPosition = arg2;
				}
			}
		});
		mPopupWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				Log.i("xxhong", "onDismiss");
				if(onDismissListener!=null){
					onDismissListener.onDismiss();
				}
			}
		});
	}
	public void setOnDismissListener( PopWindowDismissListener onDismissListener){
		this.onDismissListener=onDismissListener;
	}

	public void setOnPopWindowItemClickListener(PopWindowOnItemClickListener listeners){
		this.popWindowOnItemClickListener = listeners;
	}
	public void setOnItemClickListener(OnItemClickListener listener){
		lvMain.setOnItemClickListener(listener);
	}
	public void show(View view) {
		   if(mAdapter==null)
			   throw new RuntimeException("目有adapter show你妹呀");
			mAdapter.setPosition(currPosition);
			mAdapter.notifyDataSetChanged();
			mPopupWindow.showAsDropDown(view);
	}
	public void dismisss(){
		mPopupWindow.dismiss();
	}
	public boolean isShowing(){
		return 	mPopupWindow.isShowing();
	}
}
