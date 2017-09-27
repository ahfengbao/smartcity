package org.hfzy.smartcity.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {
	private static final String PREF_NAME = "config";

	public static void setBoolean(Context context, String key, boolean value) {
		context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
				.putBoolean(key, value).commit();
	}

	public static boolean getBoolean(Context context, String key,
			boolean defValue) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
	}

	public static void setInt(Context context, String key, int value) {
		context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
				.putInt(key, value).commit();
	}

	public static int getInt(Context context, String key, int defValue) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getInt(key, defValue);
	}

	public static void setString(Context context, String key, String value) {
		context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
				.putString(key, value).commit();
	}

	public static String getString(Context context, String key, String defValue) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}

	public static void remove(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().remove(key).commit();
	}
}
