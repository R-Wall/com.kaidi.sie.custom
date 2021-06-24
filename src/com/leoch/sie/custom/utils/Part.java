package com.leoch.sie.custom.utils;

import com.leoch.sie.custom.sap.models.PartModel;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCException;

public class Part {

	
	/**
	 * @Title: ckeck
	 * @Description: TODO(���BOM�ṹ�����Ϸ���״̬)
	 * @param @param tcc
	 * @param @param id
	 * @param @return 
	 * @param @throws TCException    ����
	 * @return String    �����
	 * @throws TCException
	 */
	    
	public static String ckeck(TCComponentItemRevision tcc, String id) throws TCException {		
		String status = tcc.getProperty("release_status_list");
		if (status == null || status.isEmpty()) {
			return id + "��״̬����Ϊ��\n";
		}
//		if (!id.startsWith("201") && !id.startsWith("202") && ("ʧЧ".equals(status) || "����".equals(status))) {
//			return id + "��״̬����Ϊ:" + status + "\n";
//		}
		return "";
	}
	
	public static String ckeckBySAP(TCComponentItemRevision tcc, String id) throws TCException {		
		
		String status = tcc.getProperty(PartModel.PartSentSAPFlag);
		if (!status.equals("true")) {
			return id + "������û�з��͹�SAP��\n";
		}
		return "";
	}
	
	
	public static String[] getStatus(String id) {
		if (id.startsWith("201") || id.startsWith("202")) {
			return new String[] {"ԭ������","��������","����","����","ʧЧ"};
		} else {
			return new String[] {"ԭ������","��������","����"};
		}
	}
	
	public static boolean isBOM(TCComponentItemRevision rev) throws TCException {
		TCComponent[] bomviews = rev.getRelatedComponents("structure_revisions");
		if (bomviews == null || bomviews.length == 0) {
			return false;
		}
		return true;
	}
}
