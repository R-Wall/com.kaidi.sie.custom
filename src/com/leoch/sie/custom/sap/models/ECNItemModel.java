package com.leoch.sie.custom.sap.models;

import java.util.HashMap;
import java.util.Map;

import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCException;

public class ECNItemModel {

	public static String MATNR = "MATNR"; // ���ϱ��
	public static int MATNR_L = 12; // ���ϱ��볤��
	
	public static String OITXT = "OITXT"; // ����
	public static int OITXT_L = 40; // ��������
	
	private TCComponentItemRevision rev;
	
	private String desc;
	
	Map<String, Object> infos;	
	
	/**
	 *
	 * @param rev ��������µ����ϰ汾
	 * @param desc ����������Ϊ�㲿��������
	 */
	    
	public ECNItemModel (TCComponentItemRevision rev, String desc) {
		this.rev =rev;
		this.desc = desc;
	}
		
	/**
	 * @Title: load
	 * @Description: ���ز����ECN���������Ϣ
	 * @param @return
	 * @param @throws TCException    ����
	 * @return String    ������Ϣ
	 * @throws
	 */
	    
	public String load() throws TCException {
		String msg = "";
		if (rev == null) {
			return msg;
		}
		infos = new HashMap<>();
		String id = rev.getProperty("item_id");
		if (id.length() > MATNR_L) {
			msg += "���������������ϱ��� "+ id + "���Ȳ��ܳ���" + MATNR_L + "\n";
		}
		if (desc.length() > OITXT_L) {
			msg += "�������������������� "+ desc + "���Ȳ��ܳ���" + OITXT_L + "\n";
		}
		infos.put(MATNR, id); // ���Ϻ�
		infos.put(OITXT, desc); // ����
		return msg;
	}
		
	/**
	 * @Title: getModel
	 * @Description: ��ȡ������������Ϣ
	 * @param @return    ����
	 * @return Map<String,Object>   ������������Ϣ
	 * @throws
	 */
	    
	public Map<String, Object> getModel(){
		return infos;
	}
}
