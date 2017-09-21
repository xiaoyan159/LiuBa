package com.navinfo.liuba.location;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.litesuits.common.assist.Check;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Collator;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串的一些工具类，如分隔，加密
 * 
 * @author archko
 */
public class FMStringUtils {

	public static final NaturalStringComparator NSC = new NaturalStringComparator();

	public static final NaturalFileComparator NFC = new NaturalFileComparator();

	private FMStringUtils() {

	}

	/**
	 * Cleanup title. Remove from title file extension and (...), [...]
	 */
	public static String cleanupTitle(final String in) {
		String out = in;
		try {
			out = in.substring(0, in.lastIndexOf('.'));
			out = out.replaceAll("\\(.*?\\)|\\[.*?\\]", "")
					.replaceAll("_", " ").replaceAll(".fb2$", "").trim();
		} catch (final IndexOutOfBoundsException e) {
		}
		return out;
	}

	public static String md5(final String in) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			digest.reset();
			digest.update(in.getBytes());
			final byte[] a = digest.digest();
			final int len = a.length;
			final StringBuilder sb = new StringBuilder(len << 1);
			for (int i = 0; i < len; i++) {
				sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
				sb.append(Character.forDigit(a[i] & 0x0f, 16));
			}
			return sb.toString();
		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Set<String> split(final String separator, final String value) {
		final Set<String> list = new LinkedHashSet<String>();

		final String[] split = value.split(separator);
		for (final String string : split) {
			if (!Check.isEmpty(string)) {
				list.add(string);
			}
		}
		return list;
	}

	public static String merge(final String separator, final String... items) {
		final StringBuffer result = new StringBuffer();
		for (final String item : items) {
			if (!Check.isEmpty(item)) {
				if (result.length() > 0) {
					result.append(separator);
				}
				result.append(item);
			}
		}
		return result.toString();
	}

	public static String merge(final String separator,
                               final Collection<String> items) {
		final StringBuffer result = new StringBuffer();
		for (final String item : items) {
			if (!Check.isEmpty(item)) {
				if (result.length() > 0) {
					result.append(separator);
				}
				result.append(item);
			}
		}
		return result.toString();
	}

	public static final int compareNatural(String firstString,
			String secondString) {
		int firstIndex = 0;
		int secondIndex = 0;

		Collator collator = Collator.getInstance();

		while (true) {
			if (firstIndex == firstString.length()
					&& secondIndex == secondString.length()) {
				return 0;
			}
			if (firstIndex == firstString.length()) {
				return -1;
			}
			if (secondIndex == secondString.length()) {
				return 1;
			}

			if (Character.isDigit(firstString.charAt(firstIndex))
					&& Character.isDigit(secondString.charAt(secondIndex))) {
				int firstZeroCount = 0;
				while (firstString.charAt(firstIndex) == '0') {
					firstZeroCount++;
					firstIndex++;
					if (firstIndex == firstString.length()) {
						break;
					}
				}
				int secondZeroCount = 0;
				while (secondString.charAt(secondIndex) == '0') {
					secondZeroCount++;
					secondIndex++;
					if (secondIndex == secondString.length()) {
						break;
					}
				}
				if ((firstIndex == firstString.length() || !Character
						.isDigit(firstString.charAt(firstIndex)))
						&& (secondIndex == secondString.length() || !Character
								.isDigit(secondString.charAt(secondIndex)))) {
					continue;
				}
				if ((firstIndex == firstString.length() || !Character
						.isDigit(firstString.charAt(firstIndex)))
						&& !(secondIndex == secondString.length() || !Character
								.isDigit(secondString.charAt(secondIndex)))) {
					return -1;
				}
				if ((secondIndex == secondString.length() || !Character
						.isDigit(secondString.charAt(secondIndex)))) {
					return 1;
				}

				int diff = 0;
				do {
					if (diff == 0) {
						diff = firstString.charAt(firstIndex)
								- secondString.charAt(secondIndex);
					}
					firstIndex++;
					secondIndex++;
					if (firstIndex == firstString.length()
							&& secondIndex == secondString.length()) {
						return diff != 0 ? diff : firstZeroCount
								- secondZeroCount;
					}
					if (firstIndex == firstString.length()) {
						if (diff == 0) {
							return -1;
						}
						return Character.isDigit(secondString
								.charAt(secondIndex)) ? -1 : diff;
					}
					if (secondIndex == secondString.length()) {
						if (diff == 0) {
							return 1;
						}
						return Character
								.isDigit(firstString.charAt(firstIndex)) ? 1
								: diff;
					}
					if (!Character.isDigit(firstString.charAt(firstIndex))
							&& !Character.isDigit(secondString
									.charAt(secondIndex))) {
						if (diff != 0) {
							return diff;
						}
						break;
					}
					if (!Character.isDigit(firstString.charAt(firstIndex))) {
						return -1;
					}
					if (!Character.isDigit(secondString.charAt(secondIndex))) {
						return 1;
					}
				} while (true);
			} else {
				int aw = firstIndex;
				int bw = secondIndex;
				do {
					firstIndex++;
				} while (firstIndex < firstString.length()
						&& !Character.isDigit(firstString.charAt(firstIndex)));
				do {
					secondIndex++;
				} while (secondIndex < secondString.length()
						&& !Character.isDigit(secondString.charAt(secondIndex)));

				String as = firstString.substring(aw, firstIndex);
				String bs = secondString.substring(bw, secondIndex);
				int subwordResult = collator.compare(as, bs);
				if (subwordResult != 0) {
					return subwordResult;
				}
			}
		}
	}

	@Deprecated
	public static Comparator<? super File> getNaturalFileComparator() {
		return NFC;
	}

	public static int split(char[] str, int begin, int len, int[] outStart,
			int[] outLength, boolean lineBreaksOnly) {
		if (str == null) {
			return 0;
		}
		if (len == 0) {
			return 0;
		}
		int i = begin, start = begin;
		int index = 0;
		boolean match = false;
		while (i < begin + len) {
			if ((lineBreaksOnly && (str[i] == 0x0D || str[i] == 0x0A))
					|| (!lineBreaksOnly && (str[i] == 0x20 || str[i] == 0x0D
							|| str[i] == 0x0A || str[i] == 0x09))) {
				if (match) {
					outStart[index] = start;
					outLength[index] = i - start;
					index++;
					match = false;
				}
				start = ++i;
				continue;
			}
			match = true;
			i++;
		}
		if (match) {
			outStart[index] = start;
			outLength[index] = i - start;
			index++;
		}
		return index;
	}

	public static final class NaturalStringComparator implements
            Comparator<String> {

		@Override
		public int compare(String o1, String o2) {
			return compareNatural(o1, o2);
		}
	}

	public static final class NaturalFileComparator implements Comparator<File> {

		@Override
		public int compare(File o1, File o2) {
			if (o1 == null) {
				return -1;
			}
			if (o2 == null) {
				return 1;
			}
			return compareNatural(o1.getAbsolutePath(), o2.getAbsolutePath());
		}
	}

	public static int parseInt(char[] string, int start, int length, int radix)
			throws NumberFormatException {
		if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX) {
			throw new NumberFormatException("Invalid radix: " + radix);
		}
		if (string == null) {
			throw new NumberFormatException(new String(string, start, length));
		}
		int i = 0;
		if (length == 0) {
			throw new NumberFormatException(new String(string, start, length));
		}
		boolean negative = string[start + i] == '-';
		if (negative && ++i == length) {
			throw new NumberFormatException(new String(string, start, length));
		}

		return parse(string, start, length, i, radix, negative);
	}

	private static int parse(char[] string, int start, int length, int offset,
			int radix, boolean negative) throws NumberFormatException {
		int max = Integer.MIN_VALUE / radix;
		int result = 0;
		while (offset < length) {
			int digit = Character.digit(string[start + (offset++)], radix);
			if (digit == -1) {
				throw new NumberFormatException(new String(string, start,
						length));
			}
			if (max > result) {
				throw new NumberFormatException(new String(string, start,
						length));
			}
			int next = result * radix - digit;
			if (next > result) {
				throw new NumberFormatException(new String(string, start,
						length));
			}
			result = next;
		}
		if (!negative) {
			result = -result;
			if (result < 0) {
				throw new NumberFormatException(new String(string, start,
						length));
			}
		}
		return result;
	}

	public static boolean isEmpty(String str) {
		if (FMTextUtils.isEmpty(str)
				|| "NULL".equals(str)) {
			return true;
		}
		// 过滤空格
		if (str.equals(" ") || str.trim().equals("")) {
			return true;
		}
		return false;
	}

	// 专门过滤空格
	public static String trim(String str) {
		if (FMTextUtils.isEmpty(str)
				|| "NULL".equals(str)) {
			return null;
		}
		// 过滤空格
		if (str.equals(" ") || str.trim().equals("")) {

			str.trim();

		}
		return str;
	}

	public static boolean isEquals(String str1, String str2) {
		if (!isEmpty(str1) && !isEmpty(str2) && str1.equals(str2)) {
			return true;
		}
		return false;
	}

	/**
	 * MD5 加密
	 * 
	 * @param str
	 * @return
	 */
	public static String getMD5Str(String str) {
		if (!FMStringUtils.isEmpty(str)) {
			MessageDigest messageDigest = null;
			try {
				messageDigest = MessageDigest.getInstance("MD5");
				messageDigest.reset();
				messageDigest.update(str.getBytes("UTF-8"));
			} catch (NoSuchAlgorithmException e) {
				
					e.printStackTrace();
				return null;
			} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				return null;
			}

			final byte[] byteArray = messageDigest.digest();

			final StringBuffer md5StrBuff = new StringBuffer();

			for (int i = 0; i < byteArray.length; i++) {
				if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
					md5StrBuff.append("0").append(
							Integer.toHexString(0xFF & byteArray[i]));
				} else {
					md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
				}
			}
			// 16位加密，从第9位到25位
			return md5StrBuff.substring(8, 24).toString().toUpperCase();
		}
		return "";
	}

	/**
	 * 替换字符串中 全部的,为空格.
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceAllComma2Spacing(String str) {
		if (!FMStringUtils.isEmpty(str)) {
			return str.replaceAll(",", " ");
		}
		return "";
	}

	/**
	 * 计算字符串中的字符长度 汉字长度为2 字母数字长度为1
	 * 
	 * @param value
	 * @return
	 */
	public static int String_length(String value) {
		int valueLength = 0;
		String chinese = "[\u4e00-\u9fa5]";
		for (int i = 0; i < value.length(); i++) {
			String temp = value.substring(i, i + 1);
			if (temp.matches(chinese)) {
				valueLength += 2;
			} else {
				valueLength += 1;
			}
		}
		return valueLength;
	}
	 /**
     * 大写首字母
     * 
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     * 
     * @param str
     * @return
     */
    public static String capitalizeFirstLetter(String str) {
        if (isEmpty(str)) {
            return str;
        }

        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str : new StringBuilder(str.length())
                .append(Character.toUpperCase(c)).append(str.substring(1)).toString();
    }
    /**
     * encoded in utf-8
     * 
     * <pre>
     * utf8Encode(null)        =   null
     * utf8Encode("")          =   "";
     * utf8Encode("aa")        =   "aa";
     * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
     * </pre>
     * 
     * @param str
     * @return
     * @throws UnsupportedEncodingException if an error occurs
     */
    public static String utf8Encode(String str) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }
    /**
     * encoded in utf-8, if exception, return defultReturn
     * 
     * @param str
     * @param defultReturn
     * @return
     */
    public static String utf8Encode(String str, String defultReturn) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defultReturn;
            }
        }
        return str;
    }

    /**
     * get innerHtml from href
     * 
     * <pre>
     * getHrefInnerHtml(null)                                  = ""
     * getHrefInnerHtml("")                                    = ""
     * getHrefInnerHtml("mp3")                                 = "mp3";
     * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
     * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
     * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
     * </pre>
     * 
     * @param href
     * @return <ul>
     *         <li>if href is null, return ""</li>
     *         <li>if not match regx, return source</li>
     *         <li>return the last string that match regx</li>
     *         </ul>
     */
    public static String getHrefInnerHtml(String href) {
        if (isEmpty(href)) {
            return "";
        }

        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }

/**
     * process special char in html
     * 
     * <pre>
     * htmlEscapeCharsToString(null) = null;
     * htmlEscapeCharsToString("") = "";
     * htmlEscapeCharsToString("mp3") = "mp3";
     * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
     * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
     * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
     * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
     * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
     * </pre>
     * 
     * @param source
     * @return
     */
    public static String htmlEscapeCharsToString(String source) {
        return FMStringUtils.isEmpty(source) ? source : source.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
    }

    /**
     * transform half width char to full width char
     * 
     * <pre>
     * fullWidthToHalfWidth(null) = null;
     * fullWidthToHalfWidth("") = "";
     * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
     * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
     * </pre>
     * 
     * @param s
     * @return
     */
    public static String fullWidthToHalfWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == 12288) {
                source[i] = ' ';
                // } else if (source[i] == 12290) {
                // source[i] = '.';
            } else if (source[i] >= 65281 && source[i] <= 65374) {
                source[i] = (char)(source[i] - 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * transform full width char to half width char
     * 
     * <pre>
     * halfWidthToFullWidth(null) = null;
     * halfWidthToFullWidth("") = "";
     * halfWidthToFullWidth(" ") = new String(new char[] {12288});
     * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
     * </pre>
     * 
     * @param s
     * @return
     */
    public static String halfWidthToFullWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == ' ') {
                source[i] = (char)12288;
                // } else if (source[i] == '.') {
                // source[i] = (char)12290;
            } else if (source[i] >= 33 && source[i] <= 126) {
                source[i] = (char)(source[i] + 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }
 // 去除字符串尾部空格
 	public static String trimEnd(String str) {
 		return str.replaceAll("[\\s]*$", "");
 	}

 	// 将字符串中所有的非标准字符（双字节字符）替换成两个标准字符（**，或其他的也可以）
 	public static String convert(String str) {
 		return str.replaceAll("[^\\x00-\\xff]", "**");
 	}
 	/**
 	 * 网络路径获取
 	 * @param url
 	 * @return
 	 */
 	public static String getUrlName(String url) {
		if (FMStringUtils.isEmpty(url))
			return "";

		int index = url.indexOf("?");
		if (index > 0)
			url = url.substring(0, index);

		index = url.lastIndexOf("/");
		if (index < 0)
			index = url.indexOf("\\");

		if (index < 0)
			return url;

		return url.substring(index + 1);
	}
 	/**
 	 * 获得时间
 	 * @param tm 秒
 	 * @return
 	 */
 	public static String getTime(int tm) {
		int h = tm / 3600;
		int m = tm / 60;
		int s = tm % 60;

		if (h > 0)
			return String.format("%02d:%02d:%02d", h, m, s);

		else if (m > 0)
			return String.format("%02d:%02d", m, s);

		return String.format("%02d", s);
	}
 	/**
 	 * String 转为 double
 	 * @param value 
 	 * @param default_value 默认
 	 * @return
 	 */
 	public static double parseDouble(String value, double default_value) {
		try {
			return Double.parseDouble(value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return default_value;
	}
 	/**
 	 * String 转为 double
 	 * @param value
 	 * @param default_value 默认
 	 * @return
 	 */
 	public static int parseInt(String value, int default_value) {
		try {
			return Integer.parseInt(value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return default_value;
	}
 	/**
 	 * String 转为 int
 	 * @param value
 	 * @return
 	 */
 	public static int parseInt(String value) {
		if (isEmpty(value)) 
			return 0;
		
		try {
			return Integer.parseInt(value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return 0;
	}
 	/**
 	 * String 转为 long
 	 * @param value
 	 * @param default_value
 	 * @return
 	 */
 	public static long parseLong(String value, long default_value) {
		try {
			return Long.parseLong(value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return default_value;
	}
 	/**
 	 * 0至128之间
 	 * @param str
 	 * @return
 	 */
 	public static boolean isASCII(String str) {
		if (isEmpty(str))
			return false;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) >= 128 || str.charAt(i) < 0) {
				return false;
			}
		}
		return true;
	}
 	
 	/**
 	 * 获取UUID
 	 * @return
 	 */
 	public static String getUUID(){

 		 return FMTextUtils.getUuid(true);
 	}
 	
 	/**
 	 * 新增
 	 * @return
 	 */
 	public static String newADD  = "02";

 	/**
	 * 小数点后四舍五入 精确到第5位
	 * 
	 * @param d
	 * @return
	 */
	public static double formatDouble5(double d) {
		 BigDecimal bg = new BigDecimal(d);
		 double f1 = bg.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1;
	}

 	/**
	 * 获取异常堆栈信息
	 * 
	 * @return
	 */
	public static String getExceptionInfo(Exception e) {
		String errorInfo = "";
		if (e != null && e.getStackTrace().length > 0) {
			for (int i = 0; i < e.getStackTrace().length; i++) {
				errorInfo += e.getStackTrace()[i];
			}
		}
		return errorInfo;
	}

	public static String getDeviceId32(Context context){
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String tmDevice, tmSerial, tmPhone, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String uniqueId = deviceUuid.toString();
		uniqueId = uniqueId.replaceAll("-","").toUpperCase();
		return uniqueId;
	}

//	public static boolean equalsJson(String json1, String json2){
//		if(json1 == null && null == json2){
//			return true;
//		}
//		if(!FMTextUtils.isEmpty(json1)&&!FMTextUtils.isEmpty(json2)&&json1.equals(json2)){
//			return true;
//		}
//		try {
//			JsonParser parser = new JsonParser();
//			JsonObject obj = (JsonObject) parser.parse(json1);
//			JsonParser parser1 = new JsonParser();
//			JsonObject obj1 = (JsonObject) parser1.parse(json2);
//			FMLog.writeLogtoFile("差分","Tips", "新对象:"+json1+";旧对象:"+json2);
//			return obj.equals(obj1);
//
//		}catch (Exception e){
//			e.printStackTrace();
//			return TextUtils.equals(json1, json2);
//		}
//	}
}
