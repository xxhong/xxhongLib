package com.xxhong.lib.customcontrol;


import com.xxhong.lib.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
/**
 * 
 * copy 
  
   <cn.lenovo.assistantlib.customcontrol.LoadingImageView
    android:layout_width="wrap_content"
        android:layout_height="wrap_content"
   >
  </cn.lenovo.assistantlib.customcontrol.LoadingImageView>
  @author xxhong
 *
 */
public class LoadingImageView extends ImageView{

	public LoadingImageView(Context context) {
		super(context);
	}

	public LoadingImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public LoadingImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundDrawable(getResources().getDrawable(R.drawable.loading_leassistantlib));
		AnimationDrawable anim = (AnimationDrawable)getBackground();
		anim.start();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	
	
	

	
}
