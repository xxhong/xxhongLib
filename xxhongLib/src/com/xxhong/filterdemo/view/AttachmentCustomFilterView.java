package com.xxhong.filterdemo.view;


import java.util.ArrayList;
import java.util.List;

import com.nineoldandroids.animation.ObjectAnimator;
import com.xxhong.filterdemo.adapter.AttachMentFilterAdapter;
import com.xxhong.filterdemo.domain.CustomFilterItem;
import com.xxhong.filterdemo.view.AttachMentFilterPopWindow.PopWindowDismissListener;
import com.xxhong.filterdemo.view.AttachMentFilterPopWindow.PopWindowOnItemClickListener;
import com.xxhong.lib.R;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AttachmentCustomFilterView extends LinearLayout {
	public interface AttachmentCustomFilterViewItemClickListener{
		void onItemClick(CustomFilterItem customFilter);
	}
	private AttachmentCustomFilterViewItemClickListener attachmentCustomFilterViewItemClickListener;
	private TextView tvCurrName;
	private  ImageView ivNarro;
	RelativeLayout rlMain;
	AttachMentFilterPopWindow  popWindow;
	private Context context;
	public AttachmentCustomFilterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		View.inflate(context, R.layout.attachment_custom_filter_view, this);
		initView(context);
		setOnClickListeners();
		this.context =context;
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
		rlMain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean showing = popWindow.isShowing();
				if(showing){
					popWindow.dismisss();
				}else{
					popWindow.show(AttachmentCustomFilterView.this);
					startRote();
				}
			}
		});
		
	}


	public void initView(Context context){
		tvCurrName = (TextView) findViewById(R.id.tv_attachment_custom_filter_textView);
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
	public void startRote(){
		ObjectAnimator visToInvis = ObjectAnimator.ofFloat(ivNarro, "rotation", 0f, 180f);  
		visToInvis.start();
	}
	public void endRote(){
		ObjectAnimator visToInvis = ObjectAnimator.ofFloat(ivNarro, "rotation", 180f, 0f);  
		visToInvis.start();
	}
	public void setOnAttachmentCustomFilterViewItemClickListener(AttachmentCustomFilterViewItemClickListener attachmentCustomFilterViewItemClickListener){
		this.attachmentCustomFilterViewItemClickListener = attachmentCustomFilterViewItemClickListener;
	}
}
