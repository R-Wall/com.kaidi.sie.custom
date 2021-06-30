package com.leoch.sie.custom.sap.models;

import java.util.HashMap;
import java.util.Map;

import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCException;

public class BOPLineModel {

	public static String STEUS = "STEUS";//  �������
	
	public static String VORNR = "VORNR";//  ������
	
	public static String ARBPL = "ARBPL"; // ��������
	
	public static String KTSCH = "KTSCH"; // ��׼�ı���
	
	public static String LTXA1 = "LTXA1"; // �������ƣ�object_name��
	
	public static String BMSCH = "BMSCH"; // ��������
		
	public static String VORME = "VORME";// ������λ
			
	public static String VGW01 = "VGW01"; // ֱ���˹�
		
	public static String VGE01 = "VGE01"; // ֱ���˹���λ
		
	public static String VGW02 = "VGW02"; // ����˹�
	
	public static String VGE02 = "VGE02"; // ����˹���λ
	
	public static String VGW03 = "VGW03"; // �۾���̯��
	
	public static String VGE03 = "VGE03"; // �۾���̯����λ

	public static String VGW04 = "VGE04"; // ��Դ(ˮ������
	
	public static String VGE04 = "VGE04"; // ��Դ(ˮ��������λ
	
	public static String VGW05 = "VGW05"; // ά���Ĳ�
	
	public static String VGE05 = "VGE05"; // ά���Ĳĵ�λ
	
	public static String VGW06 = "VGW06"; // ����
	
	public static String VGE06 = "VGE06"; // ������λ	
	
	public static String CKSELKZ = "CKSELKZ"; // �ɱ�������ر�ʶ
	
	public static String ANZMA = "ANZMA"; // ��Ա��
	
	public static String INFNR = "INFNR"; // �ɹ���Ϣ��¼
	
	public static String EKORG = "EKORG"; // �ɹ���֯
	
	public static String MATKL = "MATKL"; // ������
	
	public static String EKGRP = "EKGRP"; // �ɹ���
	
	public static String SAKTO = "SAKTO"; // �ɱ�Ҫ��
	
	private Map<String, Object> info = null;
	
	TCComponentBOMLine bomLine = null;
	
	String topLineId = null;
	
	String ecnNo = null;
	
	Double quantity = null;
//	public static String CHANGE = "CHANGE"; // �������
	
	public BOPLineModel(TCComponentBOMLine bomLine, String topLineId, String ecnNO) {
		this.bomLine = bomLine;
		this.topLineId = topLineId;
		this.ecnNo = ecnNO;
//		this.quantity = quantity;
	}
	
	public String load() throws TCException {
		String msg = "";
		if (bomLine == null) {
			return msg;
		}
		info = new HashMap<>();
		
		String k8_STEUS = bomLine.getProperty("bl_occ_k8_STEUS");
		info.put(STEUS, k8_STEUS);
		
		//������
		String k8_VORNR = bomLine.getProperty("bl_sequence_no");
		info.put("VORNR", k8_VORNR);
		
		String k8_ARBPL = bomLine.getProperty("bl_occ_k8_ARBPL");
		info.put("ARBPL", k8_ARBPL);
		
		String k8_KTSCH = bomLine.getProperty("bl_occ_k8_KTSCH");
		info.put("KTSCH",k8_KTSCH);
		
		String object_name = bomLine.getItemRevision().getProperty("object_name");
		info.put(LTXA1, object_name);
				
		String k8_BMSCH = bomLine.getProperty("bl_occ_k8_BMSCH");
		info.put(BMSCH, k8_BMSCH);
		
		String k8_VORME = bomLine.getProperty("bl_occ_k8_VORME");
		info.put(VORME, k8_VORME);
		
		String k8_VGW01 = bomLine.getProperty("bl_occ_k8_VGW1");
		info.put(VGW01, k8_VGW01);
		
		String k8_VGE01 = bomLine.getProperty("bl_occ_k8_VGE01");
		info.put(VGE01, k8_VGE01);
		
		String k8_VGW02 = bomLine.getProperty("bl_occ_k8_VGW2");
		info.put(VGW02, k8_VGW02);
		
		String k8_VGE02 = bomLine.getProperty("bl_occ_k8_VGE02");
		info.put(VGE02, k8_VGE02);
		
		String k8_VGW03 = bomLine.getProperty("bl_occ_k8_VGW3");
		info.put(VGW03, k8_VGW03);
		
		String k8_VGE03 = bomLine.getProperty("bl_occ_k8_VGE03");
		info.put(VGE03, k8_VGE03);
		
		String k8_VGW04 = bomLine.getProperty("bl_occ_k8_VGW4");
		info.put(VGW04, k8_VGW04);
		
		String k8_VGE04 = bomLine.getProperty("bl_occ_k8_VGE04");
		info.put(VGE04, k8_VGE04);
		
		String k8_VGW05 = bomLine.getProperty("bl_occ_k8_VGW5");
		info.put(VGW05, k8_VGW05);		
		
		String k8_VGE05 = bomLine.getProperty("bl_occ_k8_VGE05");
		info.put(VGE05, k8_VGE05);
		
		String k8_VGW06 = bomLine.getProperty("bl_occ_k8_VGW6");
		info.put(VGW06, k8_VGW06);
		
		String k8_VGE06 = bomLine.getProperty("bl_occ_k8_VGE06");
		info.put(VGE06, k8_VGE06);
				
		String k8_CKSELKZ = bomLine.getProperty("bl_occ_k8_CKSELKZ");
		info.put(CKSELKZ, k8_CKSELKZ);
		
		String k8_ANZMA = bomLine.getProperty("bl_occ_k8_ANZMA");
		info.put(ANZMA, k8_ANZMA);
		
		String k8_INFNR = bomLine.getProperty("bl_occ_k8_INFNR");
		info.put(INFNR, k8_INFNR);
		
		String k8_EKORG = bomLine.getProperty("bl_occ_k8_EKORG");
		info.put(INFNR, k8_EKORG);
		
		String k8_MATKL = bomLine.getProperty("bl_occ_k8_MATKL");
		info.put(MATKL, k8_MATKL);
		
		String k8_EKGRP = bomLine.getProperty("bl_occ_k8_EKGRP");
		info.put(EKGRP, k8_EKGRP);
		
		String k8_SAKTO = bomLine.getProperty("bl_occ_k8_SAKTO");
		info.put(SAKTO, k8_SAKTO);
		
//		info = bomLine.getProperty("");
		return msg;
		
	}
	
	
	
	public Map<String, Object> getModel() {
		return info;
	}
}
