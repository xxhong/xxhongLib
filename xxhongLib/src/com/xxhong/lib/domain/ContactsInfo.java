package com.xxhong.lib.domain;

public class ContactsInfo {
	private String name;
	private String phonenumber;
	private String email;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public ContactsInfo(String name, String phonenumber, String email) {
		this.name = name;
		this.phonenumber = phonenumber;
		this.email = email;
	}
	public ContactsInfo() {
	}
	
}
