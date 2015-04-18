package com.xxhong.lib.uitl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

@SuppressLint("SdCardPath")
public class AndroidUtil {
	private static final String CONTACT_REGEX = "((.+))?.*?<(.*)>";

	private static final Pattern EMAIL_PATTERN = Pattern
			.compile("^[\\w|\\d|\\_]([0-9]|[a-z]|[A-Z]|\\.|\\+|\\-|\\_|\\#|\\!|\\?\\~|\\*)*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$");

	private static final Pattern PHONE_ALL = Pattern
			.compile("13[0-9]|15[0-3]|15[7-9]|18[0-9]|145|147|15[5-6]|184");

	private static final Pattern PHONE_CMCC = Pattern
			.compile("13[4-9]|15[0-2]|15[7-9]|18[2-3]|18[7-8]|147|184");

	private static final Pattern PHONE_TIANYI = Pattern
			.compile("133|153|18[0-1]|189");

	private static final Pattern PHONE_WO = Pattern
			.compile("13[0-2]|15[5-6]|18[5-6]|145");

	public static final float TEXT_SIZE_320_240 = 7;
	public static final float TEXT_SIZE_480_320 = 8;
	public static final float TEXT_SIZE_800_480 = 12;
	public static final float TEXT_SIZE_960_640 = 13;
	public static final float TEXT_SIZE_960_540 = 13;
	public static final float TEXT_SIZE_1280_720 = 14;
	public static final float TEST_SIZE_1920_1080 = 15;





	public static int getVerCode(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (Exception e) {
			return -1;
		}
	}

	public static String getVerName(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (Exception e) {
			return "no version name";
		}
	}

	/**
	 * 获取DeviceId
	 * 
	 * @param context
	 * @return 当获取到的TelephonyManager为null时，将返回"null"
	 */
	public static String getDeviceId(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm == null) {
			return "null";
		} else {
			String id = tm.getDeviceId();
			return id == null ? "null" : id;
		}
	}

	public static String getIMSI(Context context) {
		TelephonyManager telManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = telManager.getSubscriberId();
		return imsi == null ? "" : imsi;
	}

	public static enum SIMOperator {
		ChinaMobile, ChinaUnicom, ChinaTelecom, None
	}

	public static SIMOperator getSIMOperator(Context context) {
		TelephonyManager telManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		/**
		 * 获取SIM卡的IMSI码 SIM卡唯一标识：IMSI 国际移动用户识别码（IMSI：International Mobile
		 * Subscriber Identification Number）是区别移动用户的标志，
		 * 储存在SIM卡中，可用于区别移动用户的有效信息。IMSI由MCC、MNC、MSIN组成，其中MCC为移动国家号码，由3位数字组成，
		 * 唯一地识别移动客户所属的国家，我国为460；MNC为网络id，由2位数字组成，
		 * 用于识别移动客户所归属的移动网络，中国移动为00，中国联通为01,中国电信为03；MSIN为移动客户识别码，采用等长11位数字构成。
		 * 唯一地识别国内GSM移动通信网中移动客户。所以要区分是移动还是联通，只需取得SIM卡中的MNC字段即可
		 */
		String imsi = telManager.getSubscriberId();
		if (imsi != null) {
			if (imsi.startsWith("46000") || imsi.startsWith("46002")) {// 因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号
				// 中国移动
				return SIMOperator.ChinaMobile;
			} else if (imsi.startsWith("46001")) {
				// 中国联通
				return SIMOperator.ChinaUnicom;
			} else if (imsi.startsWith("46003")) {
				// 中国电信
				return SIMOperator.ChinaTelecom;
			}
		}
		return SIMOperator.None;
	}

	/**
	 * 显示或隐藏IME
	 * 
	 * @param context
	 * @param bHide
	 */
	public static void hideIME(Activity context, boolean bHide) {
		if (bHide) {
			try {
				((InputMethodManager) context
						.getSystemService(Activity.INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(context.getCurrentFocus()
								.getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
			} catch (NullPointerException npe) {
				npe.printStackTrace();
			}
		} else { // show IME
			try {
				((InputMethodManager) context
						.getSystemService(Activity.INPUT_METHOD_SERVICE))
						.showSoftInput(context.getCurrentFocus(),
								InputMethodManager.SHOW_IMPLICIT);
			} catch (NullPointerException npe) {
				npe.printStackTrace();
			}
		}
	}

	/**
	 * 在dialog开启前确定需要开启后跳出IME
	 * 
	 * @param dialog
	 */
	public static void showIMEonDialog(AlertDialog dialog) {
		try {
			Window window = dialog.getWindow();
			window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		} catch (Exception e) {
		}
	}

	/**
	 * 判断一个apk是否安装
	 * 
	 * @param ctx
	 * @param packageName
	 * @return
	 */
	public static boolean isPkgInstalled(Context ctx, String packageName) {
		PackageManager pm = ctx.getPackageManager();
		try {
			pm.getPackageInfo(packageName, 0);
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
		return true;
	}

	public static boolean isGooglePlayInstalled(Context ctx) {
		return isAndroidMarketInstalled(ctx);
	}

	/**
	 * @deprecated use isGooglePlayInstalled(Context ctx) instead
	 * @param ctx
	 * @return
	 */
	public static boolean isAndroidMarketInstalled(Context ctx) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://search?q=foo"));
		PackageManager pm = ctx.getPackageManager();
		List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static void installApkWithPrompt(File apkFile, Context context) {
		Intent promptInstall = new Intent(Intent.ACTION_VIEW);
		promptInstall.setDataAndType(Uri.fromFile(apkFile),
				"application/vnd.android.package-archive");
		context.startActivity(promptInstall);
	}

	/**
	 * @param context
	 *            used to check the device version and DownloadManager
	 *            information
	 * @return true if the download manager is available
	 */
	public static boolean isDownloadManagerAvailable(Context context) {
		try {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
				return false;
			}
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setClassName("com.android.providers.downloads.ui",
					"com.android.providers.downloads.ui.DownloadList");
			List<ResolveInfo> list = context.getPackageManager()
					.queryIntentActivities(intent,
							PackageManager.MATCH_DEFAULT_ONLY);
			return list.size() > 0;
		} catch (Exception e) {
			return false;
		}
	}


	public static boolean networkStatusOK(final Context context) {
		boolean netStatus = false;

		try {
			ConnectivityManager connectManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectManager
					.getActiveNetworkInfo();
			if (activeNetworkInfo != null) {
				if (activeNetworkInfo.isAvailable()
						&& activeNetworkInfo.isConnected()) {
					netStatus = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return netStatus;
	}

	public static boolean isWifiActive(Context icontext) {
		Context context = icontext.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info;
		if (connectivity != null) {
			info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getTypeName().equals("WIFI")
							&& info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 检查邮箱的格式
	 * 
	 * @param emailAddress
	 * @return
	 */
	public static boolean checkEmailFormate(String emailAddress) {
		boolean isFormated = false;
		if (EMAIL_PATTERN.matcher(emailAddress.trim()).find()) {
			isFormated = true;
		} else {
			isFormated = false;
		}
		return isFormated;
	}

	/**
	 * 判断移动联通电信号码
	 * 
	 * @param phonenum
	 * @return
	 */
	public static boolean checkPhoneNum(String phonenum) {
		if (phonenum.length() != 11)
			return false;
		String phone = phonenum.substring(phonenum.length() - 11,
				phonenum.length() - 11 + 3);
		if (PHONE_ALL.matcher(phone).find())
			return true;
		else
			return false;
	}

	/**
	 * 看看是不是移动的号码
	 * 
	 * @param phonenum
	 * @return
	 */
	public static boolean checkPhoneCMCC(String phonenum) {
		if (phonenum.length() < 11)
			return false;
		String phone = phonenum.substring(phonenum.length() - 11,
				phonenum.length() - 11 + 3);
		if (PHONE_CMCC.matcher(phone).find())
			return true;
		else
			return false;
	}

	/**
	 * 号码返回邮箱后缀
	 * 
	 * @param phonenum
	 * @return
	 */
	public static String checkPhoneForResult(String phonenum) {
		if (phonenum.length() < 11)
			return "";
		String phone = phonenum.substring(phonenum.length() - 11,
				phonenum.length() - 11 + 3);
		if (PHONE_CMCC.matcher(phone).find())
			return "@139.com";
		else if (PHONE_TIANYI.matcher(phone).find())
			return "@189.cn";
		else if (PHONE_WO.matcher(phone).find()) {
			return "@wo.com.cn";
		} else
			return "";
	}

	/**
	 * 调用显示软键盘
	 * 
	 * @param view
	 * @param context
	 */
	public static void showKeyBoard(Context context) {
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 调用显示软键盘
	 * 
	 * @param view
	 * @param context
	 */
	public static void showKeyBoard(View view, Context context) {
		view.setFocusableInTouchMode(true); // 在view获取焦点之前需要把其他已经拥有焦点的控件
											// setFocusable(false)
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @param context
	 */
	public static void hideKeyBoard(Activity context) {
		if (context == null) {
			return;
		}
		View focusView = context.getCurrentFocus();
		if (focusView != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(
					focusView.getWindowToken(), 0);
		}
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @param context
	 */
	public static void hideKeyBoard(Context context, View view) {
		if (view != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager
					.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	/**
	 * @param cx
	 * 
	 * @param titleName
	 *            快捷方式名称
	 * 
	 * @return
	 */
	public static boolean hasShortcut(Context cx) {
		boolean result = false;
		Cursor c = null;
		// 获取当前应用名称
		String title = null;
		try {
			final PackageManager pm = cx.getPackageManager();
			title = pm.getApplicationLabel(
					pm.getApplicationInfo(cx.getPackageName(),
							PackageManager.GET_META_DATA)).toString();

			final String uriStr;
			if (android.os.Build.VERSION.SDK_INT < 8) {
				uriStr = "content://com.android.launcher.settings/favorites?notify=true";
			} else {
				uriStr = "content://com.android.launcher2.settings/favorites?notify=true";
			}
			final Uri CONTENT_URI = Uri.parse(uriStr);
			c = cx.getContentResolver().query(CONTENT_URI, null,
					"title=? and iconPackage=?",
					new String[] { title, cx.getPackageName() }, null);
			if (c != null && c.getCount() > 0) {
				result = true;

			}
		} catch (Exception e) {
		} finally {
			if (c != null) {
				c.close();
				c = null;
			}
		}
		return result;
	}

	public static String getEmailDefaultName(String emailName) {
		int index = emailName.indexOf("@");
		if (index > 0) {
			return emailName.substring(0, emailName.indexOf("@"));
		} else {
			return emailName;
		}
	}

	/**
	 * 16位的md5 默认返回是小写的
	 * 
	 * @param s
	 * @return
	 */
	public static String MD5_16Bit(String s) {
		return MD5_32Bit(s).substring(8, 24);
	}

	/**
	 * 32位的md5 默认返回是小写的
	 * 
	 * @param s
	 * @return
	 */
	public static String MD5_32Bit(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 32位的md5 默认返回是大写的
	 * 
	 * @param s
	 * @return
	 */
	public static String MD5_32Bit_Upper(String s) {
		return MD5_32Bit(s).toUpperCase();
	}

	/**
	 * 校验文件的
	 * 
	 * @param file
	 * @return
	 */
	public static String MD5_file(File file) {

		InputStream fis;
		byte[] buffer = new byte[1024];
		int numRead = 0;
		MessageDigest md5;
		try {
			fis = new FileInputStream(file);
			md5 = MessageDigest.getInstance("MD5");
			while ((numRead = fis.read(buffer)) > 0) {
				md5.update(buffer, 0, numRead);
			}
			fis.close();
			return toFileHexString(md5.digest());
		} catch (Exception e) {
			System.out.println("error");
			return null;
		}
	}

	private static String toFileHexString(byte[] b) {
		char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	/**
	 * 得到状态栏高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Activity context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0;
		int statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			Rect frame = new Rect();
			context.getWindow().getDecorView()
					.getWindowVisibleDisplayFrame(frame);
			statusBarHeight = frame.top;
		}
		return statusBarHeight;
	}

	public static int getScreenWidth(Context context) {

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);

		@SuppressWarnings("deprecation")
		int mResWidth = (int) (wm.getDefaultDisplay().getWidth());

		return mResWidth;
	}

	public static int getScreenHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);

		@SuppressWarnings("deprecation")
		int mResHeight = (int) (wm.getDefaultDisplay().getHeight());
		return mResHeight;

	}

	/**
	 * Wifi isconnected
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo != null)
			return networkInfo.isConnected();
		return false;
	}

	public static int ConnectedState(Context context) { // 返回0代表gprs连接状态，返回1代表wifi连接状态
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo != null && networkInfo.isConnected())
			return 1;

		NetworkInfo networkInfoMobile = manager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (networkInfoMobile != null && networkInfoMobile.isConnected())
			return 0;

		return -1;
	}

	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * apn isconnected
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnected(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (networkInfo != null)
			return networkInfo.isConnected();
		return false;
	}

	/**
	 * 
	 * @param context
	 * */
	public static void cleanInternalCache(Context context) {
		deleteFilesByDirectory(context.getCacheDir());
	}

	/**
	 * * 清除本应用所有数据库 (/data/data/com.xxx.xxx/databases)
	 * 
	 * @param context
	 */
	@SuppressLint("SdCardPath")
	public static void cleanDatabases(Context context) {
		deleteFilesByDirectory(new File("/data/data/"
				+ context.getPackageName() + "/databases"));
	}

	/**
	 * 清除本应用SharedPreference (/data/data/com.xxx.xxx/shared_prefs)
	 * 
	 * @param context
	 */
	public static void cleanSharedPreference(Context context) {
		deleteFilesByDirectory(new File("/data/data/"
				+ context.getPackageName() + "/shared_prefs"));
	}

	/**
	 * 按名字清除本应用数据库
	 * 
	 * @param context
	 * @param dbName
	 * */
	public static void cleanDatabaseByName(Context context, String dbName) {
		context.deleteDatabase(dbName);
	}

	/** * 清除/data/data/com.xxx.xxx/files下的内容 * * @param context */
	public static void cleanFiles(Context context) {
		deleteFilesByDirectory(context.getFilesDir());
	}


	/** * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * * @param filePath */
	public static void cleanCustomCache(String filePath) {
		deleteFilesByDirectory(new File(filePath));
	}


	/** * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory */
	private static void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				if (item.isDirectory()) {
					deleteFilesByDirectory(item);
				} else {
					item.delete();
				}
			}
		}
	}

	/**
	 * 以最省内存的方式读取本地资源的图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		opt.inJustDecodeBounds = true;

		BitmapFactory.decodeResource(context.getResources(), resId, opt);

		int imageWidth = opt.outWidth;
		int imageHeight = opt.outHeight;

		float scaleWidth = imageWidth / getScreenWidth(context);
		float scaleHeight = imageHeight / getScreenHeight(context);

		float imageScale = 1;

		if (scaleWidth > scaleHeight && scaleHeight > 1)
			imageScale = scaleWidth + 0.5f;
		if (scaleHeight > scaleWidth && scaleWidth > 1)
			imageScale = scaleHeight + 0.5f;

		opt.inJustDecodeBounds = false;
		opt.inSampleSize = (int) imageScale;

		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/*@SuppressWarnings("deprecation")
	public static SharedPreferences getSpForProcess(Context context,
			String spName) {
		context.getSharedPreferences(spName, Context.MODE_WORLD_WRITEABLE);
		SharedPreferences sp = null;
		try {
			Context otherContext = context.createPackageContext(
					context.getPackageName(), Context.CONTEXT_IGNORE_SECURITY);
			sp = otherContext.getSharedPreferences(spName,
					Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE
							+ Context.MODE_MULTI_PROCESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sp;
	}*/

	public static String padNnumberToString(int c) {
		if (c >= 10) {
			return String.valueOf(c);
		} else {
			return "0" + String.valueOf(c);
		}
	}

	/**
	 * 获取那个唯一标识多个组合的哦
	 * 
	 * @param context
	 * @return
	 */
	public static String getUniqueID(Context context) {
		String deviceId = getDeviceId(context);

		String devIDShort = "99" + Build.BOARD.length() % 10
				+ Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10
				+ Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10
				+ Build.HOST.length() % 10 + Build.ID.length() % 10
				+ Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10
				+ Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10
				+ Build.TYPE.length() % 10 + Build.USER.length() % 10;

		String androidID = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);

		WifiManager wm = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		String wlanMAC = wm.getConnectionInfo().getMacAddress();

		String uniqueID = MD5_32Bit(deviceId + devIDShort + androidID + wlanMAC);
		// .toUpperCase();
		Log.d("Mig", "uniqueID:" + uniqueID);
		return uniqueID;

	}

	private static long lastClickTime;

	/**
	 * 判断双击的 默认555ms
	 * 
	 * @return
	 */
	public static boolean isFastDoubleClick() {
		return isFastDoubleClick(555);
	}

	public static boolean isFastDoubleClick(int deltaTime) {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < deltaTime) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	private static long lastNotifyTime;

	/**
	 * 判断快速刷新的
	 * 
	 * @return
	 */
	public static boolean isFastnotify() {
		long time = System.currentTimeMillis();
		long timeD = time - lastNotifyTime;
		if (0 < timeD && timeD < 555) {
			return true;
		}
		lastNotifyTime = time;
		return false;
	}

	/**
	 * 见红的呢
	 */
	public static SpannableStringBuilder getColorSpan(String text,
			String keyword) {
		text = text == null ? "" : text;
		keyword = keyword == null ? "" : keyword;

		SpannableStringBuilder builder = new SpannableStringBuilder(text);

		if (text.contains(keyword)) {

			ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);

			int start = text.indexOf(keyword);
			if (start <= 0) {
				start = 0;
			}

			builder.setSpan(redSpan, start,
					text.indexOf(keyword) + keyword.length(),
					SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return builder;

	}

	/**
	 * 这个是看是不是4.0的
	 * 
	 * @return
	 */
	public static boolean API_14() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	}


	/**
	 * 这个是查找一个字符串有多少个的情况
	 * 
	 * @param source
	 * @param regexNew
	 * @return
	 */
	public static int finder(String source, String regexNew) {
		String regex = "[a-zA-Z]+";
		if (regexNew != null && !regexNew.equals("")) {
			regex = regexNew;
		}
		Pattern expression = Pattern.compile(regex);
		Matcher matcher = expression.matcher(source);
		int n = 0;
		while (matcher.find()) {
			n++;
		}
		return n;
	}

	/**
	 * 获取现在日期，返回几月几日
	 */
	public static String getNowDate() {
		Calendar calendar = Calendar.getInstance();
		int dayOfMounth = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH);
		String date = (month + 1) + "月" + dayOfMounth + "日";
		return date;
	}

	public static String getName(String text) {
		if (TextUtils.isEmpty(text)) {
			return "Null";
		}
		Pattern pattern = Pattern.compile(CONTACT_REGEX);
		Matcher m = pattern.matcher(text);
		if (m.find()) {
			return m.group(2).replace("\"", "");
		} else {
			return text;
		}
	}

	public static String getEmail(String text) {
		Pattern pattern = Pattern.compile(CONTACT_REGEX);
		if (TextUtils.isEmpty(text)) {
			return "";
		}
		Matcher m = pattern.matcher(text);
		if (m.find()) {
			return m.group(3).replace("\"", "");
		} else {
			return text;
		}
	}

	public static boolean isNumeric(String str) {
		if (TextUtils.isEmpty(str)) {
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 把流的内容转换为字符串返回
	 * 
	 * @param is
	 * @return
	 */

	public static String readFromStream(InputStream is) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) { // 把流里面的内容读取后放到buffer容器中

				baos.write(buffer, 0, len); // 把容器的内容从第0个开始，写出len个字节到 baos
											// 字节流里面，其实字节流里面也是封装了字节数组
			}
			is.close();
			String result = baos.toString(); // 主要利用字节数组输出流有一个把自己流里面的方法直接toString（）
			return result;
		} catch (IOException e) {
			Log.i("accountName", "异常   " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 判断SIM卡属于哪个运营商 0：无卡 1：移动 2：联通： 3：电信
	 */
	public static int getSIMCarrieroperatorMode(Context context) {

		TelephonyManager telManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String operator = telManager.getSimOperator();
		if (operator != null) {
			if (operator.equals("46000") || operator.equals("46002")
					|| operator.equals("46007")) {
				// 中国移动
				return 1;
			} else if (operator.equals("46001")) {
				// 中国联通
				return 2;
			} else if (operator.equals("46003")) {
				// 中国电信
				return 3;
			}
		}
		return 0;
	}

	/**
	 * 判断SIM卡属于哪个运营商 0：无卡 1：移动 2：联通： 3：电信
	 */
	public static int getProviders(Context context) {
		int type = 0;
		String net = getNetWork(context);
		List<String> infos = getNetWorkList(context);
		if (net == null || net.equals("WIFI")) {
			if (infos.size() > 1) {
				infos.remove("WIFI");
				net = infos.get(0);
				if (net.equals("3gwap") || net.equals("uniwap")
						|| net.equals("3gnet") || net.equals("uninet")) {
					type = 2;
				} else if (net.equals("cmnet") || net.equals("cmwap")) {
					type = 1;
				} else if (net.equals("ctnet") || net.equals("ctwap")) {
					type = 3;
				}
			} else {
				type = getProvidersName(context);
			}
		} else {
			if (net.equals("3gwap") || net.equals("uniwap")
					|| net.equals("3gnet") || net.equals("uninet")) {
				type = 2;
			} else if (net.equals("cmnet") || net.equals("cmwap")) {
				type = 1;
			} else if (net.equals("ctnet") || net.equals("ctwap")) {
				type = 3;
			}
		}
		return type;
	}

	private static int getProvidersName(Context c) {
		int ProvidersName = 0;
		try {
			TelephonyManager telephonyManager = (TelephonyManager) c
					.getSystemService(Context.TELEPHONY_SERVICE);
			String operator = telephonyManager.getSimOperator();
			if (operator == null || operator.equals("")) {
				operator = telephonyManager.getSubscriberId();
			}
			if (operator != null) {
				if (operator.startsWith("46000")
						|| operator.startsWith("46002")
						|| operator.equals("46007")) {
					ProvidersName = 1;
				} else if (operator.startsWith("46001")) {
					ProvidersName = 2;
				} else if (operator.startsWith("46003")) {
					ProvidersName = 3;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ProvidersName;
	}

	public static String getNetWork(Context context) {
		String NOWNET = null;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			if (info.getTypeName().equals("WIFI")) {
				NOWNET = info.getTypeName();
			} else {
				NOWNET = info.getExtraInfo();// cmwap/cmnet/wifi/uniwap/uninet
			}
		}
		return NOWNET;
	}

	// 获取可用的网络列表
	public static List<String> getNetWorkList(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] infos = cm.getAllNetworkInfo();
		List<String> list = new ArrayList<String>();
		if (infos != null) {
			for (int i = 0; i < infos.length; i++) {
				NetworkInfo info = infos[i];
				String name = null;
				if (info.getTypeName().equals("WIFI")) {
					name = info.getTypeName();
				} else {
					name = info.getExtraInfo();
				}
				if (name != null && list.contains(name) == false) {
					list.add(name);
				}
			}
		}
		return list;
	}

	public static void logCaller(int index) {
		StackTraceElement[] stackTraceElements = new Throwable()
				.getStackTrace();
		if (stackTraceElements != null) {
			// 最小为0，最大为方法栈的长度
			if (index <= 0) {
				index = stackTraceElements.length;
			}
			index = Math.min(index, stackTraceElements.length);
			for (int i = 0; i < index; i++) {
				Log.d("LogCaller", stackTraceElements[i].getClassName() + "."
						+ stackTraceElements[i].getMethodName());
			}
		}
	}

	public static void logCaller() {
		logCaller(4);
	}

	public static String getFormatString(String text) {
		Pattern p = Pattern
				.compile("^(Re:|re:|Fw:|FW:|FWD:|Fwd:|回复:|转发:|答复:|回复：|utf-8|UTF-8|\\*\\*\\*UNCHECKED\\*\\*\\*)+(.*)$");
		Matcher m = p.matcher(text);
		if (m.find()) {
			return m.group(2);
		}
		return text;
	}

	public static String getString(String text) {

		HashMap<String, String> digitTowordsMap = new HashMap<String, String>();
		digitTowordsMap.put("1", "一");
		digitTowordsMap.put("2", "二");
		digitTowordsMap.put("3", "三");
		digitTowordsMap.put("4", "四");
		digitTowordsMap.put("5", "五");
		digitTowordsMap.put("6", "六");
		digitTowordsMap.put("7", "七");
		digitTowordsMap.put("8", "八");
		digitTowordsMap.put("9", "九");

		StringBuilder testMsg = new StringBuilder(text);
		Pattern pattern = Pattern.compile("(1?)(\\d+)(\\.com|\\.COM|邮箱)");
		Matcher matcher = pattern.matcher(text);

		while (matcher.find()) {
			int end = matcher.end(1);
			if (matcher.group(1) != null && !"".equals(matcher.group(1).trim())) {
				testMsg.replace(end - 1, end, "妖");
			}

			String besideString = matcher.group(2);

			for (int i = 0; i < besideString.length(); i++) {
				String index = besideString.substring(i, i + 1);
				String word = digitTowordsMap.get(index);
				if (word != null) {
					testMsg.replace(end + i, end + i + 1, word);
				}

			}
		}

		return testMsg.toString();
	}


	public static long formatsSize(String size) {
		try {
			Pattern p = Pattern
					.compile("(KB|Kb|kB|kb|MB|Mb|mB|mb|GB|Gb|gB|gb|b|B)$");
			Matcher m = p.matcher(size);
			String value = null;
			String unit = null;
			if (m.find()) {
				unit = m.group(1);// 值
				if (unit == null) {
					throw new NumberFormatException("");
				}
				value = size.replace(m.group(1), "").trim();
				if (TextUtils.isEmpty(value)) {
					throw new NumberFormatException("");
				}
			}

			float v = Float.valueOf(value);
			if (unit.equalsIgnoreCase("GB")) {
				return (long) (v * 1024 * 1024 * 1024);
			} else if (unit.equalsIgnoreCase("MB")) {
				return (long) (v * 1024 * 1024);
			} else if (unit.equalsIgnoreCase("KB")) {
				return (long) (v * 1024);
			} else if (unit.equalsIgnoreCase("B")) {
				return (long) v;
			} else {
				throw new NumberFormatException("");
			}
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * 这个用于色值变成bitmap 桌面空间改颜色用最好
	 * 
	 * @param context
	 * @param color
	 * @param width
	 * @param height
	 * @return Bitmap
	 */
	public static Bitmap colorToBitamp(Context context, String color,
			int width, int height) {
		int w = width;
		int h = height;
		Bitmap bitmap;
		Drawable drawable = new ColorDrawable(Color.parseColor(color));
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		bitmap = Bitmap.createBitmap(w, h, config);
		// 注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		drawable.draw(canvas);
		return bitmap;
	}

	public static String getPerDayStr() {
		Date date = new Date();
		SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dayStr = dayFormat.format(date);
		return dayStr;
	}

	public static void del587VersionShortcut(Context context) {
		Intent shortcut = new Intent(
				"com.android.launcher.action.UNINSTALL_SHORTCUT");
		// 快捷方式名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, "139邮箱");
		Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
		shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		shortcutIntent.setClassName(context,
				"cn.cj.pe.activity.PeSplashActivity");
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		context.sendBroadcast(shortcut);
	}


	public static String getTopActivity(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

		if (runningTaskInfos != null) {
			RunningTaskInfo runningTaskInfo = runningTaskInfos.get(0);
			ComponentName topActivity = runningTaskInfo.topActivity;
			return topActivity.getClassName();
		} else {
			return "";
		}
	}

	public static void setDrawableTop(Context context, TextView textView,
			int res) {
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		Drawable drawable = context.getResources().getDrawable(res);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		textView.setCompoundDrawables(null, drawable, null, null); // 设置左图标
	}

	public static void setDrawableBottom(Context context, TextView textView,
			int res) {
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		Drawable drawable = context.getResources().getDrawable(res);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		textView.setCompoundDrawables(null, null, null, drawable); // 设置左图标
	}

	public static void setDrawableLeft(Context context, TextView textView,
			int res) {
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		Drawable drawable = context.getResources().getDrawable(res);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		textView.setCompoundDrawables(null, null, null, null); // 设置左图标
	}

	public static void setDrawableRight(Context context, TextView textView,
			int res) {
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		Drawable drawable = context.getResources().getDrawable(res);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		textView.setCompoundDrawables(null, null, drawable, null); // 设置左图标
	}


	private static final String SCHEME_FILE = "file";
	private static final String SCHEME_CONTENT = "content";

	public static File getFromMediaUri(ContentResolver resolver, Uri uri) {
		if (uri == null)
			return null;

		if (SCHEME_FILE.equals(uri.getScheme())) {
			return new File(uri.getPath());
		} else if (SCHEME_CONTENT.equals(uri.getScheme())) {
			final String[] filePathColumn = { MediaStore.MediaColumns.DATA,
					MediaStore.MediaColumns.DISPLAY_NAME };
			Cursor cursor = null;
			try {
				cursor = resolver.query(uri, filePathColumn, null, null, null);
				if (cursor != null && cursor.moveToFirst()) {
					final int columnIndex = (uri.toString()
							.startsWith("content://com.google.android.gallery3d")) ? cursor
							.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
							: cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
					// Picasa image on newer devices with Honeycomb and up
					if (columnIndex != -1) {
						String filePath = cursor.getString(columnIndex);
						if (!TextUtils.isEmpty(filePath)) {
							return new File(filePath);
						}
					}
				}
			} catch (SecurityException ignored) {
				// Nothing we can do
			} finally {
				if (cursor != null)
					cursor.close();
			}
		}
		return null;
	}

	/**
     * 程序是否在前台运行
    * 
     * @return
     */
    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();

        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
        	return false;

        for (RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                            && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
            	return true;
            }
        }

        return false;
    }
}
