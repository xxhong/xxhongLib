package com.xxhong.lib.uitl;

import java.security.MessageDigest;

public class TextUtil {
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
}
