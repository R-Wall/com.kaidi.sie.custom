package com.leoch.sie.custom.sap.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;

public class ECNModel {

	public static final String ECNSentSAPFlag = "k8_ecn_sendSAP";
	public static String AENNR = "AENNR"; // ���ı��
	public static int AENNR_L = 12; // //�����ų���
	
	public static String AETXT = "AETXA"; // ���ı������
	public static int AETXT_L = 40; // ���ı����������	
	
	public static String DATUV = "DATUA"; // ��ʼ��Ч����
	
	public static String AEGRU = "AEGRA"; // ����ԭ��
	public static int AEGRU_L = 40; // ����ԭ�򳤶�
	
	public static String AENST = "AENSA"; // ���ĺŵ�״̬
	public static int AENST_L = 2; // ���ĺ�״̬�ĳ���
	
	BOMStruct struct;
	
	List<ECNItemModel> itemModels;
	
	TCComponentItem ecn;
	
	TCSession session;
	
	Map<String, Object> infos;
	
	List<TCComponentItemRevision> solu;
	
	/**
	 * ECN����SAP������ģ��
	 *
	 * @param ecn ECN����
	 * @param session TC�Ự
	 * @param solu ECN�Ľ�������е�����
	 */
	    
	public ECNModel(TCComponentItem ecn, TCSession session, List<TCComponentItemRevision> solu) {
		this.ecn = ecn;
		this.session = session;
		this.solu = solu;
	}
		
	/**
	 * @Title: load
	 * @Description: ���ز��������BOM��Ϣ��Ϣ
	 * @param @return
	 * @param @throws TCException    ����
	 * @return String    �����
	 * @throws
	 */
	    
	public String load() throws TCException {
		String msg = "";
		infos = new HashMap<>();		
		String ecnNo = ecn.getProperty("item_id"); 
		String name = ecn.getProperty("object_name");
		String reason = ecn.getProperty("k8_reason"); 	
		if (ecnNo.length() > AENNR_L) {
			msg += "ECN����" + ecnNo + "�ĳ��Ȳ��ܳ���" + AENNR_L + "\n";
		}
		if (name.length() > AETXT_L) {
			msg += "��������" + name + "�ĳ��Ȳ��ܳ���" + AETXT_L + "\n";
		}
		if (reason.length() > AEGRU_L) {
			reason = reason.substring(0, 39);
//			msg += "����ԭ��" + reason + "�ĳ��Ȳ��ܳ���" + AEGRU_L + "\n";
		}
		infos.put(AENNR, ecnNo); // �����
		infos.put(AETXT, name); // ��������
		infos.put(DATUV, new Date()); // ��Ч����
		infos.put(AEGRU, reason); // ���ԭ��
		infos.put(AENST, "01"); // ���ĺ�״̬
		itemModels = new ArrayList<>();
		for (int i = 0; i < solu.size(); i++) {
			ECNItemModel itemModel = new ECNItemModel(solu.get(i), name);
			msg += itemModel.load();
			itemModels.add(itemModel);
		}
		
		struct = new BOMStruct(solu, session, ecnNo);
		msg += struct.load();
		struct.close();
		return msg;
	}
	
	/**
	 * @Title: getModel
	 * @Description: ��ȡECN������SAP��Ϣ
	 * @param @return    ����
	 * @return Map<String,Object>   ECN����SAP��Ϣ
	 * @throws
	 */
	    
	public Map<String, Object> getModel() {
		return infos;
	}
		
	/**
	 * @Title: getItemModels
	 * @Description: ��ȡECN�����������SAP����Ϣ
	 * @param @return    ����
	 * @return List<ECNItemModel>    ECN�����������SAP����Ϣ
	 * @throws
	 */
	    
	public List<ECNItemModel> getItemModels(){
		return itemModels;
	}
	
	/**
	 * @Title: getBOMStruct
	 * @Description: ��ȡECN�µ�����BOM�ṹ��Ϣ
	 * @param @return    ����
	 * @return BOMStruct    ECN�µ�����BOM�ṹ��Ϣ
	 * @throws
	 */
	    
	public BOMStruct getBOMStruct() {
		return struct;
	}
	
	/**
	 * @Title: setSentSAPFlag
	 * @Description: ����ECN�ѷ���SAP
	 * @param @throws TCException    ����
	 * @return void    ��������
	 * @throws
	 */
	    
	@SuppressWarnings("deprecation")
	public void setSentSAPFlag() throws TCException {
//		session.getUserService().call("avicit_call_bypass", new Object[] { 1 });
		ecn.setLogicalProperty(ECNSentSAPFlag, true);
//		session.getUserService().call("avicit_call_bypass", new Object[] { 0 });
	}
}
