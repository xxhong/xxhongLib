package com.xxhong.lib.uitl;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
/**
 * 
 *  @author xxhong
 *
 */
public class AppInfo {
	private static Context context;

	private AppInfo(Context ct) {
		context = ct;
	}
	private static AppInfo appInfo;
	private AppInfo(){}
	public static AppInfo getInstance(Context ct){
		if(appInfo==null){
			return new AppInfo(ct);
		}
		return appInfo;
	}
	/**
	 * 获取当前程序的versionCode
	 * 
	 * @return versionCode
	 */
	public int getClientVersionCode() throws Exception {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo = packageManager.getPackageInfo(
				context.getPackageName(), 0);
		return packInfo.versionCode;
	}

	/**
	 * 获取当前程序的versionName
	 * 
	 * @return versionName
	 */
	public String getClientVersionName() throws Exception {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo = packageManager.getPackageInfo(
				context.getPackageName(), 0);
		return packInfo.versionName;
	}

	/**
	 * 
	 * @param context 上下文
	 * @param resName 资源文件名字  "R.drawable.logo"
	 * @return
	 */
	public static int getResId(Context context, String resName) {
		int cut = resName.lastIndexOf('.');
		String name = resName.substring(cut + 1);
		String type = resName.substring(2, cut);
		Resources res = context.getResources();
		int ret = res.getIdentifier(name, type, context.getPackageName());
		return ret;
	}
	/**
	 * 安装apk
	 * @param file *.apk文件
	 */
	public void installApk(File file) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}
}
