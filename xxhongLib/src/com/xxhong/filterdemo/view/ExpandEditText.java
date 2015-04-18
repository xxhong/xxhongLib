package com.xxhong.filterdemo.view;


import java.util.ArrayList;
import java.util.List;

import com.nineoldandroids.animation.ObjectAnimator;
import com.xxhong.filterdemo.adapter.AttachMentFilterAdapter;
import com.xxhong.filterdemo.domain.CustomFilterItem;
import com.xxhong.filterdemo.view.AttachMentFilterPopWindow.PopWindowDismissListener;
import com.xxhong.filterdemo.view.AttachMentFilterPopWindow.PopWindowOnItemClickListener;
import com.xxhong.filterdemo.view.AttachmentCustomFilterView.AttachmentCustomFilterViewItemClickListener;
import com.xxhong.lib.R;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ExpandEditText extends LinearLayout {
	public interface ExpandEditTextItemClickListener{
		void onItemClick(CustomFilterItem customFilter);
	}
	private ExpandEditTextItemClickListener attachmentCustomFilterViewItemClickListener;
	private EditText tvCurrName;
	private  ImageView ivNarro;
	RelativeLayout rlMain;
	private AttachMentFilterPopWindow  popWindow;
	public ImageView getRightImageView(){
		return ivNarro;
	}
	private Context context;
	public ExpandEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		View.inflate(context, R.layout.expand_edittext_view, this);
		initView(context);
		setOnClickListeners();
		this.context =context;
	}
	public AttachMentFilterPopWindow getAttachMentFilterPopWindow(){
		return popWindow;
	}
	public void setAdapter(AttachMentFilterAdapter attachMentFilterAdapter){
		popWindow.setAttachMentFilterAdapter(attachMentFilterAdapter);
	}
	private void setOnClickListeners() {
		popWindow.setOnPopWindowItemClickListener(new PopWindowOnItemClickListener() {
			
			@Override
			public void onItemClick(CustomFilterItem customFilter) {
				if(attachmentCustomFilterViewItemClickListener!=null){
					tvCurrName.setText(customFilter.getName());
					attachmentCustomFilterViewItemClickListener.onItemClick(customFilter);
					popWindow.dismisss();
				}
			}
		});
		popWindow.setOnDismissListener(new PopWindowDismissListener() {
			@Override
			public void onDismiss() {
				endRote();
			}
		});
//		rlMain.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				boolean showing = popWindow.isShowing();
//				if(showing){
//					popWindow.dismisss();
//				}else{
//					popWindow.show(ExpandEditText.this);
//					startRote();
//				}
//			}
//		});
		
	}
	public void showExpandView(){
		popWindow.show(ExpandEditText.this);
		startRote();
	}
	public void dismissExpandView(){
		popWindow.dismisss();
	}
	public void showExpandViewAuto(){
		boolean showing = popWindow.isShowing();
		if(showing){
			popWindow.dismisss();
		}else{
			popWindow.show(ExpandEditText.this);
			startRote();
		}
	}
	public boolean isExpandViewShowing(){
		return popWindow.isShowing();
	}
	public void initView(Context context){
		tvCurrName = (EditText) findViewById(R.id.tv_attachment_custom_filter_textView);
		ivNarro = (ImageView)findViewById(R.id.iv_attachment_custom_filter_imageView);
		rlMain = (RelativeLayout) this.findViewById(R.id.rl_tv_attachment_custom_filter_main);
		popWindow = 	new AttachMentFilterPopWindow(context);
		
	}
	
	public  void setOnClickListener(View.OnClickListener onClickListener) {
		rlMain.setOnClickListener(onClickListener);
	}

	public void setCurrName(String currTextViewName){
		tvCurrName.setText(currTextViewName);
	}
	public String getCurrName(){
		return tvCurrName.getText().toString();
	}
	public void startRote(){
		ObjectAnimator visToInvis = ObjectAnimator.ofFloat(ivNarro, "rotation", 0f, 180f);  
		visToInvis.start();
	}
	public void endRote(){
		ObjectAnimator visToInvis = ObjectAnimator.ofFloat(ivNarro, "rotation", 180f, 0f);  
		visToInvis.start();
	}
	public void setOnExpandEditTextItemClickListener(ExpandEditTextItemClickListener attachmentCustomFilterViewItemClickListener){
		this.attachmentCustomFilterViewItemClickListener = attachmentCustomFilterViewItemClickListener;
	}
}
