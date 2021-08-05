package com.leoch.sie.custom.sap.models;

import java.util.HashMap;
import java.util.Map;

import com.leoch.sie.custom.utils.NumberValidationUtils;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;

public class PartModel {
	
	public static String PartSentSAPFlag = "k8_ERP"; // �����Ƿ���SAP����������

//	public static String BS01     = "BS01";  //�½�/�޸ı�ʶ    CHAR 10
//	public static String MANDT   = "MANDT";  //����            CLNT 3
	public static String SPRAS   = "SPRAS";  //���� LANG 1
	public static String MTART   = "MTART";  //�������� CHAR 4
	public static String MBRSH   = "MBRSH";  //��ҵ���� CHAR 1
	public static String MATNR   = "MATNR";  //���ϱ��� CHAR 40
	public static String MAKTX   = "MAKTX";  //�������� CHAR 40
	public static String MEINS   = "MEINS";  //����������λ UNIT 3
	public static String MATKL   = "MATKL";  //������ CHAR 9
	public static String SPART   = "SPART";  //��Ʒ�� CHAR 2
	public static String BISMT   = "BISMT";  //�����Ϻ� CHAR 40
	public static String MSTAE   = "MSTAE";  //�繤������״̬ CHAR 2
	public static String BRGEW   = "BRGEW";  //ë�� QUAN 13��3
	public static String NTGEW   = "NTGEW";  //���� QUAN 13��3
	public static String GEWEI   = "GEWEI";  //������λ UNIT 3
	public static String GROES   = "GROES";  //��С���� CHAR 32
	public static String ZEINR   = "ZEINR";  //ͼ�� CHAR 22
	public static String NORMT   = "NORMT";  //�ڲ����� CHAR 18
	public static String FERTH   = "FERTH";  //ģѨ CHAR 18
	public static String GRUN     = "LTEXT";  //���ϳ����� CHAR 300
//	public static String ZVALUE1 = "ZVALUE1";  //Ԥ���ֶ�1 CHAR 20
//	public static String ZVALUE2 = "ZVALUE2";  //Ԥ���ֶ�2 CHAR 20
//	public static String ZVALUE3 = "ZVALUE3";  //Ԥ���ֶ�3 CHAR 20
//	public static String ZVALUE4 = "ZVALUE4";  //Ԥ���ֶ�4 CHAR 20
//	public static String ZVALUE5 = "ZVALUE5";  //Ԥ���ֶ�5 CHAR 20
	public static String WERKS   = "WERKS"; //��������SAP���๤����':'������ CHAR 4
	public static String BESKZ   = "BESKZ"; //�ɹ����� CHAR 1
	public static String SOBSL   = "SOBSL"; //����ɹ��� CHAR 2
	public static String RGEKZ   = "RGEKZ"; //���� CHAR 1
	public static String MSBOOKPARTNO = "MSBOOKPARTNO"; //��Ʒϵ��
	
	public static int BS01_L  = 10;  //�½�/�޸ı�ʶ    CHAR 10
	public static int MANDT_L = 3;  //����            CLNT 3
	public static int SPRAS_L = 1;  //���� LANG 1
	public static int MTART_L = 4;  //�������� CHAR 4
	public static int MBRSH_L = 1;  //��ҵ���� CHAR 1
	public static int MATNR_L = 40;  //���ϱ��� CHAR 40
	public static int MAKTX_L = 40;  //�������� CHAR 40
	public static int MEINS_L = 3;  //����������λ UNIT 3
	public static int MATKL_L = 9;  //������ CHAR 9
	public static int SPART_L = 2;  //��Ʒ�� CHAR 2
	public static int BISMT_L = 40;  //�����Ϻ� CHAR 40
	public static int MSTAE_L = 2;  //�繤������״̬ CHAR 2
	public static int BRGEW_L = 13;  //ë�� QUAN 13��3
	public static int NTGEW_L = 13;  //���� QUAN 13��3
	public static int GEWEI_L = 3;  //������λ UNIT 3
	public static int GROES_L = 32;  //��С���� CHAR 32
	public static int ZEINR_L = 32;  //ͼ�� CHAR 22
	public static int NORMT_L = 18;  //�ڲ����� CHAR 18
	public static int FERTH_L = 18;  //ģѨ CHAR 18
	public static int GRUN_L  = 300;  //���ϳ����� CHAR 300
	public static int ZVALUE1_L = 20;  //Ԥ���ֶ�1 CHAR 20
	public static int ZVALUE2_L = 20;  //Ԥ���ֶ�2 CHAR 20
	public static int ZVALUE3_L = 20;  //Ԥ���ֶ�3 CHAR 20
	public static int ZVALUE4_L = 20;  //Ԥ���ֶ�4 CHAR 20
	public static int ZVALUE5_L = 20;  //Ԥ���ֶ�5 CHAR 20
	public static int WERKS_L   = 4; //��������SAP���๤����':'������ CHAR 4
	public static int BESKZ_L   = 1; //�ɹ����� CHAR 1
	public static int SOBSL_L   = 2; //����ɹ��� CHAR 2
	public static int RGEKZ_L   = 1; //���� CHAR 1
	
	private TCComponentItemRevision part;	
	
	Map<String,Object> model;
	
	TCSession session;
	
	/**
	 * ���Ϸ���SAP���ݽṹ��װ
	 * 
	 * @param part �㲿��
	 */
	    
	public PartModel(TCComponentItemRevision part) {
		this.part = part;
		session = (TCSession) AIFUtility.getDefaultSession();
	}

	/**
	 * @Title: getModel
	 * @Description: ��ȡ���Ϸ���SAP����Ϣ
	 * @param @return
	 * @param @throws TCException    ����
	 * @return Map<String,Object>    ��������
	 * @throws
	 */
	    
	public Map<String, Object> getModel() throws TCException{		
		return model;
	}
		
	/**
	 * @Title: load
	 * @Description: ���ز�������Ϸ���SAP����Ϣ
	 * @param @return
	 * @param @throws TCException    ����
	 * @return String    ���ϵĴ�����Ϣ
	 * @throws
	 */
	public String load() throws TCException {
			String msg = "";
			model = new HashMap<>();
			part.refresh();
			
//			if(!(part.getTCProperty("k8_ERP").equals("true"))){
//				 model.put(BS01, "ADD"); // �½�/�޸ı�ʶ   
//			}else{
//			   model.put(BS01, "UPDATE"); // �½�/�޸ı�ʶ  
//			}
//			
			String id = part.getProperty("item_id");
			if (id.isEmpty()) {
				msg += "���벻��Ϊ��\n";
			}
			if (id.length() > MATNR_L) {
				msg += id + "�ı��볤�Ȳ��ܳ���" + MATNR_L + "\n";
			}
			model.put(MATNR, id); // ���Ϻ�  
			
//			String mandt = part.getProperty("k8_group");
//			if (mandt.isEmpty()) {
//				msg += "���Ų���Ϊ��\n";
//				mandt = "800";
//			}
//			if (mandt.length() > MANDT_L) {
//				msg += id + "�ļ��ų��Ȳ��ܳ���" + MANDT_L + "\n";
//			}
//			model.put(MANDT, mandt); //���� 
			
			String spras = part.getProperty("k8_language");
			if (spras.isEmpty()) {
//				msg += "���Բ���Ϊ��\n";
				spras="1";
			}
			if (spras.length() > SPRAS_L) {
				msg += id + "�����Գ��Ȳ��ܳ���" + SPRAS_L + "\n";
			}
			model.put(SPRAS, spras); // ����
			
			String mtart = part.getProperty("k8_material_type");
			if (mtart.isEmpty()) {
				msg += "�������Ͳ���Ϊ��\n";
			}
			if (mtart.length() > MTART_L) {
				msg += id + "���������ͳ��Ȳ��ܳ���" + MTART_L + "\n";
			}
			model.put(MTART, mtart); // ��������
			
		  String mbrsh = part.getProperty("k8_industry_field");
			if (mbrsh.isEmpty()) {
//				msg += "��ҵ������Ϊ��\n";
				mbrsh="M";
			}
			if (mbrsh.length() > MBRSH_L) {
				msg += id + "����ҵ���򳤶Ȳ��ܳ���" + MBRSH_L + "\n";
			}
			model.put(MBRSH, mbrsh); // ��ҵ����
		
			String desc = part.getProperty("k8_description1"); 
			if (desc.isEmpty()) {
				msg += id + "����������Ϊ��\n";
			}
			if (desc.length() > MAKTX_L) {
				msg += id + "�������������Ȳ��ܳ���" + MAKTX_L + "\n";
			}
			model.put(MAKTX, desc); // ��������
			
			String unit = part.getProperty("k8_uom2");
			if (unit.isEmpty()) {
				msg += id + "�Ļ�����λ����Ϊ��\n";
			}
			if (unit.length() > MEINS_L) {
				msg += id + "�Ļ�����λ���Ȳ��ܳ���" + MEINS_L + "\n";
			}
			model.put(MEINS, unit); // ������λ
			
			String group = part.getProperty("k8_material_group");
			if (group.isEmpty()) {
				msg += id + "�������鲻��Ϊ��\n";
			}
			if (group.length() > MATKL_L) {
				msg += id + "�������鳤�Ȳ��ܳ���" + MATKL_L + "\n";
			}
			model.put(MATKL, group); // ������
			
			String spart = part.getProperty("k8_product_team");
//			if (spart.isEmpty()) {
//				msg += id + "�Ĳ�Ʒ�鲻��Ϊ��\n";
//			}
			if (spart.length() > SPART_L) {
				msg += id + "�Ĳ�Ʒ�鳤�Ȳ��ܳ���" + SPART_L + "\n";
			}
			model.put(SPART, spart); // ��Ʒ��
			
			String bismt = part.getProperty("k8_old_material_num");
			if (bismt.length() > BISMT_L) {
				msg += id + "�ľ����Ϻų��Ȳ��ܳ���" + BISMT_L + "\n";
			}
		    model.put(BISMT,bismt); // �����Ϻ�
		  
		  String status = part.getProperty("k8_life_state");
			if (status.isEmpty()) {
				msg += id + " ������״̬����Ϊ��\n";
			}else if(status.indexOf(":") >0)  {
				status = status.substring(0,status.indexOf(":"));
			}else if(status.contains("��Ч"))  {
				status = " ";
			}else {
				status = "99";
			}
			if (status.length() > MSTAE_L) {
				msg += id + " ������״̬���Ȳ��ܳ���" + MSTAE_L + "\n";
			}
			model.put(MSTAE, status); // ״̬
			
//			String brgew = part.getProperty("k8_rough_weight");
			String brgew = part.getDoubleProperty("k8_rough_weight")+"";
			if (brgew.equals("") && !brgew.isEmpty()) {
				if (!NumberValidationUtils.isQuantityNumber(brgew)) {
					msg += id + " ������ë������ֵ������λС����ʵ��\n";
				} else if (Double.parseDouble(brgew) == 0){
					msg += id + " ������ë������ֵ����Ϊ0\n";
				}
			}
			if (brgew.length() > BRGEW_L) {
				msg += id + " ������ë�س��Ȳ��ܳ���" + BRGEW_L + "\n";
			}
			model.put(BRGEW, brgew); // ë��
			
//			String ntgew = part.getProperty("k8_net_weight");
			String ntgew = part.getDoubleProperty("k8_net_weight")+"";
			if (ntgew.equals("") && !ntgew.isEmpty()) {
				if (!NumberValidationUtils.isQuantityNumber(ntgew)) {
					msg += id + " �����Ͼ�������ֵ������λС����ʵ��\n";
				} else if (Double.parseDouble(ntgew) == 0){
					msg += id + " �����Ͼ�������ֵ����Ϊ0\n";
				}
			}
			if (ntgew.length() > NTGEW_L) {
				msg += id + " �����Ͼ��س��Ȳ��ܳ���" + NTGEW_L + "\n";
			}
			model.put(NTGEW, ntgew); // ����
					
			String unit_weight = part.getProperty("k8_weight_unit"); 
			if(brgew.equals("0") || ntgew.equals("0")){
				if (unit_weight.isEmpty()) {
					msg += id + "��������λ����Ϊ��\n";
					unit_weight = "KG"; 
				}
				}
			if (unit_weight.length() > GEWEI_L) {
				msg += id + "��������λ���Ȳ��ܳ���" + GEWEI_L + "\n";
			}
			model.put(GEWEI, unit_weight); // ������λ 
			
			String groes = part.getProperty("k8_dimension"); 
//			if (groes.isEmpty()) {
//				msg += id + "�Ĵ�С���� ����Ϊ��\n";
//			}
			if (groes.length() > GROES_L) {
				msg += id + "�Ĵ�С���ٳ��Ȳ��ܳ���" + GROES_L + "\n";
			}
			model.put(GROES, groes); // ��С���� 
			
			String zeinr = part.getProperty("k8_drawing_num");//ͼ��
			if(zeinr == null || zeinr.isEmpty()) {
				zeinr = part.getProperty("k8_drawing_No");//��ʷͼ��
			}
			if (zeinr.length() > ZEINR_L) {
				msg += id + "��ͼ�Ż�����ʷͼ�ų��Ȳ��ܳ���" + (ZEINR_L) + "\n";
			}
			model.put(ZEINR, zeinr); // ͼ��
			
		    
			
			String type = part.getProperty("k8_internal_order");
			if (type.length() > NORMT_L) {
				msg += id + " �������ڲ������ų��Ȳ��ܳ���" + NORMT_L + "\n";
			}
			model.put(NORMT, type);	// �ڲ�����		
			
//			String ferth = part.getProperty("k8_mold_cavity");
//			if (ferth.length() > FERTH_L) {
//				msg += id + " ������ģѨ���Ȳ��ܳ���" + FERTH_L + "\n";
//			}
//			model.put(FERTH, ferth);	// ģѨ		
			
			String grun = part.getProperty("k8_description2");
			if (grun.isEmpty()) {
				msg += id + "�����ϳ����� ����Ϊ��\n";
			}
			if (grun.length() > GRUN_L) {
				msg += id + " ������ģѨ���Ȳ��ܳ���" + GRUN_L + "\n";
			}
			model.put(GRUN, grun);	// ���ϳ�����	
			
			String werks = part.getProperty("k8_factory");
			if (werks.isEmpty()) {
				msg += id + "�����Ϲ��� ����Ϊ��\n";
			}else  if(werks.indexOf(":") >0) {
				werks = werks.substring(0,werks.indexOf(":"));
			}
			if (werks.length() > WERKS_L) {
				msg += id + " �����Ϲ������Ȳ��ܳ���" + WERKS_L + "\n";
			}
			model.put(WERKS, werks);	// ��������SAP���๤����':'������	
			
			String beskz = part.getProperty("k8_procurement");
			int beskzle = beskz.indexOf(":");
			String sobsl = part.getProperty("k8_special_procurement");
			
			if(sobsl.indexOf(":") >0) {
				sobsl = sobsl.substring(0,sobsl.indexOf(":"));
			}else if(beskzle >2) {
				sobsl =  beskz.substring(1,beskz.indexOf(":"));
			}
			if (sobsl.length() > SOBSL_L) {
				msg += id + " ����������ɹ��೤�Ȳ��ܳ���" + SOBSL_L + "\n";
			}
			model.put(SOBSL, sobsl);	// ����ɹ���	
			
//			if (beskz.isEmpty()) {
//				msg += id + "�����ϲɹ����� ����Ϊ��\n";
//			}else if(beskz.indexOf(":") >0) {
//				beskz = beskz.substring(0,beskz.indexOf(":"));
//			}
			
			if (beskz.isEmpty()) {
				msg += id + "�����ϲɹ����� ����Ϊ��\n";
			}else if(beskzle >0) {
				beskz = beskz.substring(0,1);
			}
			
			if (beskz.length() > BESKZ_L) {
				msg += id + " �����ϲɹ����ͳ��Ȳ��ܳ���" + BESKZ_L + "\n";
			}
			model.put(BESKZ,beskz);	// �ɹ�����	
			
			
			
			
			String rgekz = part.getProperty("k8_recoil");
			if(rgekz.indexOf(":") >0) {
				rgekz = rgekz.substring(0,rgekz.indexOf(":"));
			}else {
				rgekz = " ";
			}
			if (rgekz.length() > RGEKZ_L) {
				msg += id + " �����Ϸ��峤�Ȳ��ܳ���" + RGEKZ_L + "\n";
			}
			model.put(RGEKZ, rgekz);	// ����	
			
			return msg;
	}
		
	/**
	 * @Title: setSentSAPFlag
	 * @Description: ���������ѷ���SAP
	 * @param @throws TCException    ����
	 * @return void    ��������
	 * @throws
	 */
	    
	@SuppressWarnings("deprecation")
	public void setSentSAPFlag() throws TCException {
//		session.getUserService().call("avicit_call_bypass", new Object[] { 1 });
		String revsionId = part.getProperty("item_revision_id");
		String flag = part.getProperty(PartSentSAPFlag);
		part.setProperty(PartSentSAPFlag, flag+revsionId);
//		session.getUserService().call("avicit_call_bypass", new Object[] { 0 });
	}
}
