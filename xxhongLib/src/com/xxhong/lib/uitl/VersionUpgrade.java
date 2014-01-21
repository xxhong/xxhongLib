package com.xxhong.lib.uitl;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import com.xxhong.lib.R;
import com.xxhong.lib.uitl.callback.OnUpgradeSuccListener;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;
/**
 * 
 * @author xxhong
 *
 */
public class VersionUpgrade {

	public void setOnUpgradeSuccListener(OnUpgradeSuccListener onUpgradeSuccListener) {
		this.onUpgradeSuccListener = onUpgradeSuccListener;
	}

	/**
	 * @param showText
	 *            下拉时显示的文字
	 */
	public void setShowText(String showText) {
		this.showText = showText;
	}

	/**
	 * @param bottomIcon
	 *            下拉时显示的icon
	 */
	public void setBottomIcon(String bottomIcon) {
		this.bottomIcon = bottomIcon;
	}

	/**
	 * @param statusText
	 */
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	/**
	 * 
	 * @param icon
	 *            状态栏显示icon
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	private Context context;
	private NotificationManager mManager;
	private OnUpgradeSuccListener onUpgradeSuccListener;
	private static final int NOTIFICATION_ID = 90000;
	private Notification notifyDownload;
	private int _progress = 0;
	private String icon = "R.drawable.ic_launcher";
	private String bottomIcon = "R.drawable.ic_launcher";
	private String statusText = "正在下载,请稍候...";
	private String showText = "正在下载,请稍候...";
	private LayoutInflater lf;
	private View view;
	private String DOWNLOAD_DIR = "/LeDownLoad/";
	/**
	 * 防止多次点击导致多次下载
	 */
	public static boolean isdoing;
	Thread t;

	public VersionUpgrade(Context context) {
		this.context = context;
		mManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mManager.cancelAll();
	}
	/**
	 * 
	 * @param path *.apk下载地址
	 * @param onUpgradeSuccListener 下载完成回调事件 
	 */
	public void download(final String path,final OnUpgradeSuccListener onUpgradeSuccListener) {
		this.onUpgradeSuccListener = onUpgradeSuccListener;
		notifyDownload = new Notification(AppInfo.getResId(context, icon),
				statusText, System.currentTimeMillis());
		notifyDownload.icon = AppInfo.getResId(context, icon);
		notifyDownload.contentView = new RemoteViews(context.getApplicationContext().getPackageName(),
				R.layout.lib_custom_dialog);
		notifyDownload.contentView.setProgressBar(R.id.xxhong_pb, 100, 0, true);
		notifyDownload.contentView.setTextViewText(R.id.xxhong_tv_process, "进度"
				+ _progress + "%");
		notifyDownload.contentView.setImageViewResource(R.id.xxhong_iv_left,
				AppInfo.getResId(context, bottomIcon));
		notifyDownload.contentView.setTextViewText(R.id.xxhong_tv_show,
				showText);
		notifyDownload.contentIntent = PendingIntent.getActivity(context, 0,
				new Intent(), Notification.FLAG_NO_CLEAR);

		File file = new File(Environment.getExternalStorageDirectory()
				+ DOWNLOAD_DIR);
		if (!file.exists()) {
			boolean mkdirs = file.mkdirs();
		}
		String pt = file.getAbsolutePath() + File.separator + getFileName(path);
		final File newapkfile = new File(pt);
		mManager.notify(NOTIFICATION_ID, notifyDownload);
			t = new Thread() {
				public void run() {
					try {
						isdoing = true;
						URL url = new URL(path);
						HttpURLConnection conn = (HttpURLConnection) url
								.openConnection();
						conn.setConnectTimeout(5000);
						conn.connect();
//						int responseCode = conn.getResponseCode() ;
//						if (responseCode != 200) {
//							return;
//						}
						
						int max = conn.getContentLength();
						InputStream is = conn.getInputStream();
						FileOutputStream fos = new FileOutputStream(newapkfile);
						byte[] buffer = new byte[1024 * 1024];
						int len = 0;
						while ((len = is.read(buffer)) != -1) {
							if (!DeviceInfo.getInstance(context).getNetWorkStates()) {
								downloadFail(newapkfile);
								return;
							}
							String pro = new DecimalFormat("00").format(
									((_progress / (double) max) * 100)).toString();
							fos.write(buffer, 0, len);
							_progress += len;

							Message message = msghandler.obtainMessage();
							message.what = 0x110;
							message.arg1 = Integer.parseInt(pro);
							message.sendToTarget();
							Thread.sleep(700);
						}
						mManager.cancel(NOTIFICATION_ID);
						_progress = 0;
						isdoing = false;
						
						// MessageHandlerList.sendMessage(AssistService.class,
						// AssistService.DOWNLOAD_APK_SUCC_MAIN, newapkfile);
						Message msg = msghandler.obtainMessage();
						msg.what = 0x112;
						msg.obj= newapkfile;
						msghandler.sendMessage(msg);
					} catch (Exception e) {
						isdoing = false;
						_progress = 0;
						mManager.cancel(NOTIFICATION_ID);
						newapkfile.delete();
						Message msg = msghandler.obtainMessage();
						msg.what = 0x111;
						msghandler.sendMessage(msg);
						e.printStackTrace();
					}
				}

				private void downloadFail(final File newapkfile) {
					isdoing = false;
					_progress = 0;
					mManager.cancel(NOTIFICATION_ID);
					newapkfile.delete();
					Thread.currentThread().stop();
					Message msg = msghandler.obtainMessage();
					msg.what = 0x111;
					msghandler.sendMessage(msg);
				};
			};
			t.start();
	}

	public static String getFileName(String path) {
		int start = path.lastIndexOf("/") + 1;
		return path.substring(start);
	}

	Handler msghandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0X110:
				notifyDownload.contentView.setProgressBar(R.id.xxhong_pb, 100,
						msg.arg1, false);
				notifyDownload.contentView.setTextViewText(
						R.id.xxhong_tv_process, "进度" + msg.arg1 + "%");
				mManager.notify(NOTIFICATION_ID, notifyDownload);
				break;
			case 0X111:
				Toast.makeText(context, "下载失败,请检查网络", 0).show();
				break;
			case 0X112://下载成功
				File file = (File) msg.obj;
				onUpgradeSuccListener.downloadOver(file);
				break;
			default:
				break;
			}
		};
	};
}
