package com.xxhong.lib.uitl;

import java.util.ArrayList;
import java.util.List;

import com.xxhong.lib.domain.ContactsInfo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.Contacts.Data;
import android.text.TextUtils;
import android.util.Log;

public class ContactsSearchFromLocal {

	// 读取通讯录的全部的联系人
	// 需要先在raw_contact表中遍历id，并根据id到data表中获取数据
	public static List<ContactsInfo> searchContacts(Context ct, String str) {
		List<ContactsInfo> allContactsInfo = new ArrayList<ContactsInfo>();
		Uri uri = Uri.parse("content://com.android.contacts/contacts"); // 访问raw_contacts表
		ContentResolver resolver = ct.getContentResolver();
		String searchKey = "";

//		if (PinyinConv.isChineseCharacters(str)) {// 是汉字
//			searchKey = "%"+str+"%";
//		} else {
			searchKey = getSearchStr(str);
//		}
		Cursor cursor = resolver.query(uri, new String[] { Data._ID },
				"sort_key like ?", new String[] { searchKey }, null); // 获得_id属性
		while (cursor!=null&&cursor.moveToNext()) {
			StringBuilder buf = new StringBuilder();
			int id = cursor.getInt(0);// 获得id并且在data中寻找数据
			buf.append("#id=" + id);
			uri = Uri.parse("content://com.android.contacts/contacts/" + id
					+ "/data"); // 如果要获得data表中某个id对应的数据，则URI为content://com.android.contacts/contacts/#/data
			Cursor cursor2 = resolver.query(uri, new String[] { Data.DATA1,
					Data.MIMETYPE }, null, null, null); // data1存储各个记录的总数据，mimetype存放记录的类型，如电话、email等
			ContactsInfo info = new ContactsInfo();
			while (cursor2!=null&&cursor2.moveToNext()) {

				String data = cursor2
						.getString(cursor2.getColumnIndex("data1"));
				if (cursor2.getString(cursor2.getColumnIndex("mimetype"))
						.equals("vnd.android.cursor.item/name")) { // 如果是名字
					if (TextUtils.isEmpty(info.getName())) {
						info.setName(data);
					}
					buf.append(",name=" + data);
				}
				if (cursor2.getString(cursor2.getColumnIndex("mimetype"))
						.equals("vnd.android.cursor.item/phone_v2")) { // 如果是电话
					if (TextUtils.isEmpty(info.getPhonenumber())) {
						info.setPhonenumber(data);
					}
					buf.append(",phone=" + data);
				}
				if (cursor2.getString(cursor2.getColumnIndex("mimetype"))
						.equals("vnd.android.cursor.item/email_v2")) { // 如果是email
					if (TextUtils.isEmpty(info.getEmail())) {
						info.setEmail(data);
					}
					buf.append(",email=" + data);
				}

			}
			allContactsInfo.add(info);
			Log.i("xxhong", buf.toString());
		}
		return allContactsInfo;
	}

	public static String getSearchStr(String str) {
		StringBuffer sb = new StringBuffer();
		int len = str.length();
		for (int i = 0; i < len; i++) {
			sb.append("%");
			sb.append(str.substring(i, i + 1));
			if (i == len - 1) {
				sb.append("%");
			}
		}
		return sb.toString();
	}
}
