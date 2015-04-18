package com.xxhong.lib.uitl;

import java.security.MessageDigest;

import android.text.TextUtils;

public class XXTextUtils {
	public static String diagestPassword(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] result = digest.digest(password.getBytes());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < result.length; i++) {
				String res = Integer.toHexString(result[i] & 0xFF);
				if (res.length() == 1) {
					sb.append("0" + res); // 0~F
				} else {
					sb.append(res);
				}
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 判断是否是手机号
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean isPhoneNum(String phone) {
		String telRegex = "[1][345789]\\d{9}";
		if (TextUtils.isEmpty(phone)) {
			return false;
		} else {
			return phone.matches(telRegex);
		}
	}
}
