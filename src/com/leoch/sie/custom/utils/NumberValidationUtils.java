package com.leoch.sie.custom.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberValidationUtils {

	private static boolean isMatch(String regex, String orginal) {
		if (orginal == null || orginal.trim().equals("")) {
			return false;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher isNum = pattern.matcher(orginal);
		return isNum.matches();
	}

	// ������
	public static boolean isPositiveInteger(String orginal) {
		return isMatch("^\\+{0,1}[1-9]\\d*", orginal);
	}

	// ������
	public static boolean isNegativeInteger(String orginal) {
		return isMatch("^-[1-9]\\d*", orginal);
	}

	// ����
	public static boolean isWholeNumber(String orginal) {
		return isMatch("[+-]{0,1}0", orginal) || isPositiveInteger(orginal) || isNegativeInteger(orginal);
	}

	// ��С��
	public static boolean isPositiveDecimal(String orginal) {
		return isMatch("\\+{0,1}[0]\\.[1-9]*|\\+{0,1}[1-9]\\d*\\.\\d*", orginal);
	}

	// ��С��
	public static boolean isNegativeDecimal(String orginal) {
		return isMatch("^-[0]\\.[1-9]*|^-[1-9]\\d*\\.\\d*", orginal);
	}

	// С��
	public static boolean isDecimal(String orginal) {
		return isMatch("[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+", orginal);
	}

	// ʵ��
	public static boolean isRealNumber(String orginal) {
		return isWholeNumber(orginal) || isDecimal(orginal);
	}
	
	// С����ǰ1-10λ��С�����1-3λ����С��
	public static boolean isQuantityNumber(String orginal) {
		return isMatch("^[+]{0,1}[0-9]{1,10}?(.[0-9]{1,3})?$", orginal);
	}
	
	// С����ǰ1-10λ��С�����1-3λ��С��
	public static boolean isQuantityNumber1(String orginal) {
		return isMatch("^[-+]{0,1}[0-9]{1,10}?(.[0-9]{1,3})?$", orginal);
	}
	
	// С����ǰ1-6λ��С�����1-7λ��С��
	public static boolean isQuantityNumber2(String orginal) {
		return isMatch("^[-+]{0,1}[0-9]{1,6}?(.[0-9]{1,7})?$", orginal);
	}

}