package com.leoch.sie.custom.sap.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.leoch.sie.custom.utils.Part;
import com.leoch.sie.custom.utils.NumberValidationUtils;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;

public class BOMInfoModel {
		
	public static String WERKS = "WERKS"; // ����(7-13ȡ�������ŵĴ���)
	
	public static String MATNR = "MATNR"; //���ϱ���
	
	public static int MATNR_L = 18; // ���ϱ��볤��
	
	public static String BMENG = "BASMN"; // ��������
	
	public static String DATUV = "DATUV"; // ��Ч����(ȡ��ǰʱ��)
	
	public static String STLAL = "STLAL"; // ��ѡ�����嵥(BOM��;)
	
	public static String STLAN = "STLAN"; //�����嵥��;(��ѡBOM���� )
	
	public static String AENNR = "AENNR"; //������
	
	private TCComponentBOMLine top; // ����BOM
	
	private List<BOMLineModel> bomlines; // BOM����Ϣ
	
	private Map<String, Object> bomInfo; // BOM����
	
	private TCComponentItemRevision rev;
	
	private AIFComponentContext[] subLines;
	
	private String ecnNo;

	private TCSession session;

	private String checkQuantityMsg;
	
	int level;
		
	/**
	 *
	 * @param top ����BOMLine
	 * @param rev ����BOM�İ汾
	 * @param encNO �����
	 * @param subLines BOM����
	 * @param level2 
	 */
	    
	public BOMInfoModel(TCComponentBOMLine top, TCComponentItemRevision rev, String encNO, AIFComponentContext[] subLines, int level) {
		this.top = top;
		this.rev = rev;
		this.ecnNo = encNO;
		this.subLines = subLines;
		this.level = level;
		session = (TCSession) AIFUtility.getDefaultSession();
	}
	
	
	/**
	 * @Title: load
	 * @Description: ���ز����BOM�ṹ��Ϣ
	 * @param @return
	 * @param @throws TCException    ����
	 * @return String   �����
	 * @throws
	 */
	    
	public String load() throws TCException {
		String msg = "";
		bomInfo = new HashMap<>();
		if (top == null) {
			return msg;
		}

		String topLineId = top.getItem().getProperty("item_id");
//		msg += Part.ckeck(top.getItemRevision(), topLineId);
		if (topLineId.length() > MATNR_L) {
			msg += topLineId + "�����ϱ��볤�Ȳ��ܳ���" + MATNR_L + "\n";
		}
		bomInfo.put(MATNR, topLineId); // ���Ϻ�
		bomInfo.put(DATUV, new Date()); // ��Ч����
		if (ecnNo != null) {
			bomInfo.put(AENNR, ecnNo);
		}
		String werks = rev.getProperty("k8_factory");
		if (werks.isEmpty()) {
			msg += topLineId + "�����Ϲ��� ����Ϊ��\n";
		}else  if(werks.indexOf(":") >0) {
			werks = werks.substring(0,werks.indexOf(":"));
		}
		bomInfo.put(WERKS, werks);	// ����
		Map<Object, Double> qs = getQuantityAndCheck(top, subLines);
		if (!checkQuantityMsg.isEmpty()) {
			msg += checkQuantityMsg;
		}
		bomInfo.put(BMENG, qs.get(top)); // ��������(��ǰĬ��Ϊ1)
//		bomInfo.put(CHOOSABLE_BOM, "1"); //��ѡ�����嵥
		bomInfo.put(STLAN, "1"); //��;
		
		bomlines = new ArrayList<>();
		if (subLines != null) {
			for (int i = 0; i < subLines.length; i++) {
				TCComponentBOMLine subLine = (TCComponentBOMLine) subLines[i].getComponent();
				BOMLineModel model = new BOMLineModel(subLine, topLineId, ecnNo, qs.get(subLines[i]));
				msg += model.load();
				bomlines.add(model);
			}
		}
		return msg;
		
	}
		
	/**
	 * @Title: getModel
	 * @Description: ��ȡBOM������Ϣ
	 * @param @return    ����
	 * @return Map<String,Object>    BOM������Ϣ
	 * @throws
	 */
	    
	public Map<String, Object> getModel(){
		return bomInfo;
	}
		
	/**
	 * @Title: getBOMLinModel
	 * @Description: ��ȡBOMLine�ṹ
	 * @param @return    ����
	 * @return List<BOMLineModel>    BOMLine�ṹ
	 * @throws
	 */
	    
	public List<BOMLineModel> getBOMLinModel(){
		return bomlines;
	}
		
	/**
	 * @Title: getRevision
	 * @Description: ��ȡBOMLine�����ϰ汾
	 * @param @return    ����
	 * @return TCComponentItemRevision   ���ϰ汾
	 * @throws
	 */
	    
	public TCComponentItemRevision getRevision() {
		return rev;
	}
	
	
	/**
	 * @Title: setSentSAPFlag
	 * @Description: ����BOM�ѷ���SAP�ɹ�
	 * @param @throws TCException    ����
	 * @return void    ��������
	 * @throws
	 */
	    
	@SuppressWarnings("deprecation")
	public void setSentSAPFlag() throws TCException {
//		session.getUserService().call("avicit_call_bypass", new Object[] { 1 });
		rev.setLogicalProperty(BOMStruct.BOMSentSAPFlag, true); // ����BOM�ѷ���SAP
//		session.getUserService().call("avicit_call_bypass", new Object[] { 0 });
	}
		
	/**
	 * @Title: getQuantityAndCheck
	 * @Description: ת��BOM�ж�Ӧ���������
	 * @param @param top
	 * @param @param subLines
	 * @param @return
	 * @param @throws TCException    ����
	 * @return Map<Object,Double>    BOM�ж�Ӧ������
	 * @throws
	 */
	    
	public Map<Object, Double> getQuantityAndCheck(TCComponentBOMLine top, AIFComponentContext[] subLines) throws TCException {
		double topQuantity = 10000;
		checkQuantityMsg = "";
		if (subLines != null) {
			String topID = top.getItemRevision().getProperty("item_id");
			for (int i = 0; i < subLines.length; i++) {
				TCComponentBOMLine subLine = (TCComponentBOMLine) subLines[i].getComponent();
				String subID = subLine.getItemRevision().getProperty("item_id");
				String quantity = subLine.getProperty("bl_quantity").trim();
				if (quantity.contains("/")) {
					String[] nums = quantity.split("/");
					if (nums.length == 2) {
						boolean b1 = NumberValidationUtils.isPositiveInteger(nums[0]);
						boolean b2 = NumberValidationUtils.isPositiveInteger(nums[1]);
						if (b1 && b2) {
							int q = Integer.parseInt(nums[1]);
							if (topQuantity % q != 0) {
								topQuantity = topQuantity * q;
							}
							continue;
						}
					} 
					checkQuantityMsg += topID + "������(" + subID + ")�������Ƿ�����ʽ\n";
				} else {
					quantity =  (Double.parseDouble(quantity) * topQuantity)+"";
					boolean b = NumberValidationUtils.isQuantityNumber1(quantity);
					if (b && Double.parseDouble(quantity) == 0) {
						b = false;
					}
					if (b) {
						continue;
					}
					checkQuantityMsg += topID + "������(" + subID+ ")��������С����ǰ1-6λ��С�����1-7λ��С��\n";
				}
			}
		}
		
		Map<Object, Double> vs = new HashMap<>();
		if (!checkQuantityMsg.isEmpty()) {
			return vs;
		}
		
		vs.put(top, topQuantity);
		if (subLines != null) {
			for (int i = 0; i < subLines.length; i++) {
				TCComponentBOMLine subLine = (TCComponentBOMLine) subLines[i].getComponent();
				String quantity = subLine.getProperty("bl_quantity").trim();
				if (quantity.contains("/")) {
					String[] nums = quantity.split("/");
					int q1 = Integer.parseInt(nums[0]);
					int q2 = Integer.parseInt(nums[1]);
					vs.put(subLines[i], topQuantity / q2 * q1);
				} else {
					double q = Double.parseDouble(quantity);
					vs.put(subLines[i], topQuantity * q);
				}
			}
		}
		return vs;		
	}	
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
}
