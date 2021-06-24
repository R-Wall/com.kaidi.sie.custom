package com.leoch.sie.custom.utils;

import java.util.HashMap;

public class ReleaseStatusMap {
	public static HashMap<String, String> map = null;
	
	static {
		map = new HashMap<>();
//		map.put("�ѷ���", "TCM Released");
//		map.put("TCM �ѷ���", "TCM Released");
		
	    map.put("����", "TCM Released");
	    map.put("ʧЧ", "L8_Invalid");
	    map.put("ԭ������", "L8_Prototype");
	    map.put("��������", "L8_Engineering");
	    map.put("����", "L8_Restrict");
	    map.put("����", "L8_Batch");
	    map.put("��ͣ", "L8_Suspensive");
	}
	
	public static String getReleaseStatusName(String displayName) {
		String ret = map.get(displayName);
		if (ret == null || ret.length() == 0) {
			if (map.values().contains(displayName)) {
				return displayName;
			}
		}
		return ret;
	}
}
