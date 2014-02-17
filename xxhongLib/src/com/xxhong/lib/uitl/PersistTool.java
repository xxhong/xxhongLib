package com.xxhong.lib.uitl;

import java.security.MessageDigest;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

/**
 * SharedPreferences工具
 */
public class PersistTool {
	private static boolean isEncrypt = false;
	private static final String PREFERENCES_NAME = "xxhongLib";
	private static SharedPreferences mPreference = null;
	private static boolean isInited = false;
	static RSACoder mRsa = new RSACoder();
	static byte[] publicKey, privateKey;

	public synchronized static void init(Context context) {
		if (mPreference == null) {
			mPreference = context.getSharedPreferences(PREFERENCES_NAME,
					Context.MODE_PRIVATE);
		}
		isInited = true;
		try {
			Map<String, Object> keyMap = mRsa.initKey();
			// 公钥
			publicKey = mRsa.getPublicKey(keyMap);
			// byte[] publicKey = b;
			// 私钥
			privateKey = RSACoder.getPrivateKey(keyMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean IsInited() {
		return isInited;
	}

	public static void saveBoolean(String name, boolean value) {
		saveString(name, String.valueOf(value));
	}

	public static boolean getBoolean(String name) {
		String bolStr = getString(name, "false");
		return Boolean.valueOf(bolStr);
	}

	public static void saveLong(String name, long value) {
		// SharedPreferences.Editor editor = mPreference.edit();
		// editor.putLong(name, value);
		// editor.commit();
		saveString(name, value + "");
	}

	public static long getLong(String name) {
		String logStr = getString(name, "0");
		return Long.parseLong(logStr);
	}

	public static void saveInt(String name, int value) {
		saveString(name, value + "");

	}

	public static int getInt(String name) {
		String intstr = getString(name, "0");
		return Integer.parseInt(intstr);
	}

	public static void saveFloat(String name, float value) {
		saveString(name, value + "");
	}

	public static float getFloat(String name) {
		String longstr = getString(name, "0");
		return Float.parseFloat(longstr);
	}

	public static boolean saveString(String name, String value) {
		boolean flag = false;
		SharedPreferences.Editor editor = mPreference.edit();
		editor.putString(md5(name), enCryByPublicKey(value));
		flag = editor.commit();
		return flag;
	}

	public static String getString(String name, String defaultValue) {
		return deCryByPrivateKey(mPreference.getString(md5(name), defaultValue));
	}

	public static boolean clearAll() {
		return mPreference.edit().clear().commit();
	}

	@SuppressLint("NewApi")
	public static String enCryByPublicKey(String key) {
		String enData;
		if (isEncrypt) {
			if (!TextUtils.isEmpty(key)) {
				try {
					byte[] code1 = RSACoder.encryptByPublicKey(key.getBytes(),
							publicKey);
					enData = Base64.encodeToString(code1, Base64.DEFAULT);
				} catch (Exception e) {
					enData = key;
					e.printStackTrace();
				}
			} else {
				enData = key;
			}
		} else {
			enData = key;
		}

		return enData;
	}

	@SuppressLint("NewApi")
	public static String deCryByPrivateKey(String enData) {
		String deData;
		if (isEncrypt) {
			if (!TextUtils.isEmpty(enData)) {
				byte[] decryptByPrivateKey;
				try {
					byte[] decode = Base64.decode(enData, Base64.DEFAULT);
					decryptByPrivateKey = RSACoder.decryptByPrivateKey(decode,
							privateKey);
					deData = new String(decryptByPrivateKey);
				} catch (Exception e) {
					deData = enData;
					e.printStackTrace();
				}
			} else {
				deData = enData;
			}
		} else {
			deData = enData;
		}
		return deData;
	}

	/**
	 * 
	 * @param paramString
	 * @return
	 */
	private static String md5(String paramString) {
		if (isEncrypt) {
			String returnStr;
			try {
				MessageDigest localMessageDigest = MessageDigest
						.getInstance("MD5");
				localMessageDigest.update(paramString.getBytes());
				returnStr = byteToHexString(localMessageDigest.digest());
				return returnStr;
			} catch (Exception e) {
				return paramString;
			}
		} else {
			return paramString;
		}

	}

	/**
	 * 将指定byte数组转换成16进制字符串
	 * 
	 * @param b
	 * @return
	 */
	private static String byteToHexString(byte[] b) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexString.append(hex.toUpperCase());
		}
		return hexString.toString();
	}

}
