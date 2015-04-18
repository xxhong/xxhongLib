//package com.xxhong.filterdemo;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.annotation.TargetApi;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.widget.Toast;
//
//import com.xxhong.filterdemo.R;
//import com.xxhong.filterdemo.adapter.AttachMentFilterAdapter;
//import com.xxhong.filterdemo.domain.CustomFilterItem;
//import com.xxhong.filterdemo.view.AttachmentCustomFilterView;
//import com.xxhong.filterdemo.view.AttachmentCustomFilterView.AttachmentCustomFilterViewItemClickListener;
//
//@TargetApi(Build.VERSION_CODES.HONEYCOMB)
//public class MainActivity extends FragmentActivity {
//	AttachmentCustomFilterView acfv;
//
//	private List<CustomFilterItem> filters =new ArrayList<CustomFilterItem>();
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		acfv = (AttachmentCustomFilterView) findViewById(R.id.acfv);
//		initData();
//		acfv.setAdapter(new AttachMentFilterAdapter(this, filters));
//		acfv.setOnAttachmentCustomFilterViewItemClickListener(new AttachmentCustomFilterViewItemClickListener() {
//			
//			@Override
//			public void onItemClick(CustomFilterItem customFilter) {
//			}
//		});
//
//	}
//	private void initData() {
//		for(int i=0;i<5;i++){
//			CustomFilterItem cf1 = 	new CustomFilterItem("全部"+i);
//			filters.add(cf1);
//		}
//	}
//
//}
