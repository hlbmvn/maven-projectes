package com.hlb.utils.security;


public class UUID {
	public static String getUUID(){
		java.util.UUID uuid = java.util.UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
	}
}
