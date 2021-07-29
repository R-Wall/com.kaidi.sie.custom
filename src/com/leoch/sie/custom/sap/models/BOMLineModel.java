package com.leoch.sie.custom.sap.models;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.leoch.sie.custom.utils.Part;
import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCException;

public class BOMLineModel {

	TCComponentBOMLine bomLine = null;
	
	public static String POSTP = "POSTP";//  ��Ŀ���(L) 
	
	public static String POSNR = "POSNR"; // BOM��Ŀ��(�Ǳ���)
	public static int POSNR_L = 4;

	public static String IDNRK = "IDNRK";// BOM���(�Ӽ����ϱ���)
	public static int IDNRK_L = 18; // BOM�������
	
	public static String MENGE = "MENGE"; // �������
		
	public static String MEINS = "MEINS"; // ���������λ(����)
	public static int MEINS_L = 3; // ���������λ����
	
	public static String AUSCH = "AUSCH"; // ������Ʒ��(�Ǳ���)(�Ӽ������)
	public static int AUSCH_L = 5; 
	
	public static String SANKA = "SANKA"; // ��ɱ����(Ĭ��ΪX�������/�͹��ϲ���ɹ���ʦѡ�Z004����)
	public static int SANKA_L = 1; 
	
	public static String LGORT = "LGORT"; // �Ǳ����������һ������һ��ֵ��
	public static int LGORT_L = 4; 
	
	public static String FMENG = "FMENG"; // �����̶�(�Ǳ���)
	
	public static String POTX1 = "POTX1"; // BOM ��Ŀ�ı�(�Ǳ���)
	public static int POTX1_L = 40; // BOM ��Ŀ�ı�����
	
	public static String ALPGR = "ALPGR"; // �����Ŀ����(�Ǳ���)
	
	public static String ALPRF = "ALPRF"; // �����Ŀ�����ȶ���(�Ǳ���)
	
	public static String ALPST = "ALPST"; // �����Ŀ������(�Ǳ���)
	
	public static String EWAHR = "EWAHR"; // ʹ�ÿ����԰� % (BTCI)(�Ǳ���)
	
//	public static String NUMBER = "NUMBER"; // ���ұ��
	
	private Map<String, Object> info = null;
	
	String topLineId = null;
	
	String ecnNo = null;
	
	Double quantity = null;
			
	/**
	 *
	 * @param bomLine BOMLine����
	 * @param topLineId ����ID
	 * @param ecnNO �����
	 * @param quantity ����
	 */
	    
	public BOMLineModel(TCComponentBOMLine bomLine, String topLineId, String ecnNO, Double quantity) {
		this.bomLine = bomLine;
		this.topLineId = topLineId;
		this.ecnNo = ecnNO;
		this.quantity = quantity;
	}
		
	/**
	 * @Title: load
	 * @Description: ���ز����BOMLine��Ϣ
	 * @param @return
	 * @param @throws TCException    ����
	 * @return String    ������Ϣ
	 * @throws
	 */
	    
	public String load() throws TCException {
		String msg = "";
		if (bomLine == null) {
			return msg;
		}
		info = new HashMap<>();
		TCComponentItemRevision rev = bomLine.getItemRevision();
		String bomLineId = rev.getProperty("item_id");
		
		msg += Part.ckeckBySAP(rev, bomLineId);
		
//		String isSentSAP = rev.getProperty(PartModel.PartSentSAPFlag);
//		if (!isSentSAP.equals("true")) {
//			msg += bomLineId + "����δͬ��SAP\n";
//		}
		info.put(POSTP, "L"); // ��Ŀ������ϵ���,Ĭ��ֵΪL
		if (bomLineId.length() > IDNRK_L) {
			msg += bomLineId + "���ϱ��볤�Ȳ��ܳ���" + IDNRK_L + "\n";
		}
		info.put(IDNRK, bomLineId); // ���Ϻ�
		String symbol = bomLine.getProperty("K8_Symbol");
		if(symbol.isEmpty() || symbol.equals("+")) {
			info.put(MENGE, quantity); // �������
		}else {
			info.put(MENGE, "-"+quantity); // �������
		}
		String posnr = bomLine.getProperty("bl_sequence_no"); // BOM�к�
		if (posnr.length() > POSNR_L) {
			msg += bomLineId + "BOM�кų��Ȳ��ܳ���" + POSNR_L + "\n";
		}
		info.put(POSNR, posnr);
		
		String unit = bomLine.getProperty("bl_K8_PartRevision_k8_uom2"); // BOM���̵�λ
		if (unit.isEmpty()) {
			msg += bomLineId + "���̵�λ����Ϊ��\n";
		} else if (unit.length() > MEINS_L) {
			msg += bomLineId + "���̵�λ���Ȳ��ܳ���" + MEINS_L + "\n";
		}
		info.put(MEINS, unit);
		
		String ausch = bomLine.getProperty("bl_occ_k8_Sub_component"); // �Ӽ������
		if (ausch.length() > AUSCH_L) {
			msg += bomLineId + "�Ӽ�����ʳ��Ȳ��ܳ���" + AUSCH_L + "\n";
		}
		info.put(AUSCH, ausch);
		
		//�жϲ��ұ���Ƿ���ȷ
		String number = bomLine.getProperty("bl_sequence_no"); // ���ұ��
		String numberlen = number.substring(number.length() -1,number.length());
		if (!numberlen.equals("0")) {
			System.out.println(numberlen);
			msg += bomLineId + ":�еĲ��ұ���������һλ��Ϊ��0" + "\n";
		}

		String sanka = bomLine.getProperty("bl_occ_k8_Sanka"); // ��ɱ����
//		if(sanka.isEmpty() || sanka.trim().equals("")) {
//			sanka = "X";
//		}
		if (sanka.length() > SANKA_L) {
			msg += bomLineId + "��ɱ���س��Ȳ��ܳ���" + SANKA_L + "\n";
		}
		info.put(SANKA, sanka);
	
		String lgort = bomLine.getProperty("bl_occ_k8_Lgort"); // Ͷ�Ͽ��ص�
		String lgort_v= null;
		Pattern pattern = Pattern.compile("[0-9]*");
		if (!lgort.isEmpty()) {
			lgort_v = lgort.substring(0, 4);
			if (!pattern.matcher(lgort_v).matches()) {
				msg += bomLineId + "Ͷ�Ͽ��ص���д������ɾ�����������ֵ" + "\n";
			}
			if (lgort_v.length() > LGORT_L) {
				msg += bomLineId + "Ͷ�Ͽ��ص㳤�Ȳ��ܳ���" + LGORT_L + "\n";
			}
			info.put(LGORT, lgort_v);
		}else {
			info.put(LGORT, lgort);
		}
		
//		String note = bomLine.getProperty("L8_note"); // BOM��ע
//		if (note.length() > POTX1_L) {
//			msg += bomLineId + "BOM��ע���Ȳ��ܳ���" + POTX1_L + "\n";
//		}
//		info.put(POTX1, note);
		return msg;
	}
		
	/**
	 * @Title: getModel
	 * @Description: ��ȡBOMLine��Ϣ
	 * @param @return    ����
	 * @return Map<String,Object>    BOMLine��Ϣ
	 * @throws
	 */
	    
	public Map<String, Object> getModel() {
		return info;
	}
}
