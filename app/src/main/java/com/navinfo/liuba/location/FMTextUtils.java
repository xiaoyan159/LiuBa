package com.navinfo.liuba.location;

import android.text.TextUtils;

import java.util.UUID;

/**
 * FMTextUitls类
 * @author qiji
 *
 */
public class FMTextUtils {

	/**
	 * CharSequence isEmpty
	 * 
	 * @param str
	 * 
	 * @return return true if charSequence is empty, false otherwise
	 */
	public static boolean isEmpty(CharSequence str){
		
		if(TextUtils.isEmpty(str)|| TextUtils.equals(str, "null")){
			return true;
		}
		
		return false;
	}

	/**
	 * CharSequence equals
	 *
	 * @param arg1
	 * @param arg2
	 * 
	 * @return return true if charSequence is equals, false otherwise
	 */
	public static boolean equals(String arg1, String arg2){
		
		if(!isEmpty(arg1)&&!isEmpty(arg2)&&arg1.equals(arg2))
		{
			return true;
		}
		
		return false;
	}

	/**
	 * @param str 需要被处理的String
	 * @return 如果当前数据是null或者"null",则返回为默认的null，否则原值返回
	 */
	public static String parserNull(String str) {
		if (isEmpty(str)) {
			return null;
		} else {
			return str;
		}
	}

	public static String getUuid(boolean isUpperCase){
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		if(isUpperCase)
			uuid = uuid.toUpperCase();

		return uuid;
	}
}
