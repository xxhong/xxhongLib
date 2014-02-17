package com.xxhong.lib.widget;


import com.xxhong.lib.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

public class LoadingDialog extends Dialog {
	private TextView tvMsg;
	public LoadingDialog(Context context) {
		this(context,R.style.pdDialog);
	}
	
	public LoadingDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
		setContentView(R.layout.custom_dialog);
		tvMsg = (TextView) this.findViewById(R.id.tv_msg);
	}

	@Override
	public void setContentView(int layoutResID) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.setContentView(layoutResID);
	}
	public void setMsg(String title){
		tvMsg.setText(title);
	}
	public void setMsg(int title){
		tvMsg.setText(title);
	}
}
