package com.leoch.sie.custom.sap.models;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.leoch.sie.custom.utils.Part;
import com.leoch.sie.custom.utils.RevisionRule;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCComponentBOMWindow;
import com.teamcenter.rac.kernel.TCComponentBOMWindowType;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentRevisionRule;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;

public class BOMStruct {
	
	private List<TCComponentItemRevision> revs;
	
	private TCSession session;	
	
	private TCComponentBOMWindow window;
	
	Map<String, BOMInfoModel> info;
	
	Map<String,BOPInfoModel> BOPinfo;
	
	String msg;
	
	private String ecnNo = null;
	
	public static String BOMSentSAPFlag = "k8_BOM_SendSAP";
	
	public BOMStruct(List<TCComponentItemRevision> revs, TCSession session) {
		this.revs = revs;
		this.session = session;
	}
		
	/**
	 * BOM�ṹ
	 *
	 * @param revs ������汾
	 * @param session TC�Ự
	 * @param ecnNo �����
	 */
	    
	public BOMStruct(List<TCComponentItemRevision> revs, TCSession session, String ecnNo) {
		this.revs = revs;
		this.session = session;
		this.ecnNo = ecnNo;
	}
	
	/**
	 * @Title: load
	 * @Description: ���ز����BOM��Ϣ
	 * @param @return
	 * @param @throws TCException    ����
	 * @return String    BOM�����
	 * @throws
	 */
	    
	public String load() throws TCException {
		TCComponentRevisionRule rule = RevisionRule.RULE;
		if (rule == null) {
			throw new TCException("��ȡ�汾����(Latest Rev Any Status)ʧ��,�޷�����BOM��");
		}
		TCComponentBOMWindowType type = (TCComponentBOMWindowType) session.getTypeComponent("BOMWindow");
		window = type.create(rule);
		msg = "";
		info = new LinkedHashMap<>();
		for (int i = 0; i < revs.size(); i++) {
			TCComponentItemRevision rev = revs.get(i);
			boolean isSentSap = rev.getLogicalProperty(BOMSentSAPFlag);
			if (ecnNo == null  && isSentSap) {
				continue;
			}
			TCComponentItem item = rev.getItem();
			TCComponentBOMLine topLine = window.setWindowTopLine(item, rev, null, null);
			if (topLine == null) {
				throw new TCException(rev.toString() + " ���ͽṹ������ʧ��");
			}
			loadModel(topLine, 0);
		}		
		return msg;		
	}
	
	public String loadBOP() throws Exception{
		
		TCComponentRevisionRule rule = RevisionRule.RULE;
		if (rule == null) {
			throw new TCException("��ȡ�汾����(Latest Rev Any Status)ʧ��,�޷�����BOM��");
		}
		TCComponentBOMWindowType type = (TCComponentBOMWindowType) session.getTypeComponent("BOMWindow");
		window = type.create(rule);
		msg = "";
		BOPinfo = new LinkedHashMap<String, BOPInfoModel>();
		for (int i = 0; i < revs.size(); i++) {
			TCComponentItemRevision rev = revs.get(i);
			boolean isSentSap = rev.getLogicalProperty(BOMSentSAPFlag);
			if (ecnNo == null  && isSentSap) {
				continue;
			}
			TCComponentItem item = rev.getItem();
			TCComponentBOMLine topLine = window.setWindowTopLine(item, rev, null, null);
			if (topLine == null) {
				throw new TCException(rev.toString() + " ���ͽṹ������ʧ��");
			}
			loadBOPModel(topLine, 0);
		}		
		return msg;
	}
	
	/**�������й��ձ���Ϣ
	 * @param topLine
	 * @param level
	 * @throws Exception 
	 */
	public void loadBOPModel(TCComponentBOMLine topLine, int level) throws Exception {
		AIFComponentContext[] subLines = unpackBOMLine(topLine);
		TCComponentItemRevision rev = topLine.getItemRevision();
		boolean isSentSap = rev.getLogicalProperty(BOMSentSAPFlag);
		if(isSentSap) {}
		String id = rev.getProperty("item_id");
		if (subLines != null && subLines.length > 0) {
//			BOPInfoModel model = new BOPInfoModel();
			if (ecnNo != null && !isSentSap) { //�Ƿ���
				BOPInfoModel model = new BOPInfoModel(topLine, rev, ecnNo, subLines);
				msg += model.load();
				BOPinfo.put(id, model);
			}else if(ecnNo == null && !isSentSap){
				BOPInfoModel model = new BOPInfoModel(topLine, rev, ecnNo, subLines);
				msg += model.load();
				BOPinfo.put(id, model);
			}else {
				BOPInfoModel model = new BOPInfoModel(topLine, rev, ecnNo, subLines);
				msg += model.load();
				BOPinfo.put(id, model);
			}
		}
	}
		
	/**
	 * @Title: loadModel
	 * @Description: ��������BOM�㼶����Ϣ
	 * @param @param topLine ����BOMLine
	 * @param @param level BOM�㼶
	 * @param @throws TCException    ����
	 * @return void    ��������
	 * @throws
	 */
	    
	public void loadModel(TCComponentBOMLine topLine, int level) throws TCException {
		AIFComponentContext[] subLines = unpackBOMLine(topLine);
		TCComponentItemRevision rev = topLine.getItemRevision();
		boolean isSentSap = rev.getLogicalProperty(BOMSentSAPFlag);
		String id = rev.getProperty("item_id");
		if (subLines != null && subLines.length > 0) {
			if (info.get(id) == null) { // �ظ�BOM�ṹ������
				if (ecnNo != null ||  !isSentSap) { // �Ѵ���SAP��BOM���ظ�����
					BOMInfoModel model = new BOMInfoModel(topLine, rev, ecnNo, subLines, level);
					msg += model.load();
					info.put(id, model);
				}
			} else {
				BOMInfoModel model = info.get(id);
				int l = model.getLevel();
				if (level > l) {
					model.setLevel(l);
				}
			}
			if(ecnNo == null) {
				for (int i = 0; i < subLines.length; i++) {
					TCComponentBOMLine subLine = (TCComponentBOMLine) subLines[i].getComponent();
					loadModel(subLine, level + 1);
				}
			}
		} else {
			boolean flag = Part.isBOM(rev);
			if (ecnNo != null && flag && level == 0) {
				// ���ͬ��SAPʱ,Ŀ���µ�BOM�Ӽ�Ϊ�յ�BOM��ͼʱ,���Ϳ�BOM�ṹ��SAP
				if (info.get(id) == null) { 
					if (!isSentSap) { 
						BOMInfoModel model = new BOMInfoModel(topLine, rev, ecnNo, subLines, level);
						msg += model.load();
						info.put(id, model);
					}
				}
			} else if (ecnNo == null && level == 0) {
			//  BOM����SAPʱ��Ŀ���µ�BOMû������ʱ������BOMͬ��SAP��ʶ
				if (info.get(id) == null) { 
					if (!isSentSap) { 
						BOMInfoModel model = new BOMInfoModel(topLine, rev, ecnNo, subLines, level);
//						msg += model.load();
//						info.put(id, model);
						model.setSentSAPFlag();
					}
				}
			}
		}		
	}
	
	/**
	 * @Title: unpackBOMLine
	 * @Description: ���BOMLine
	 * @param @param topLine ����BOMLine
	 * @param @return
	 * @param @throws TCException    ����
	 * @return AIFComponentContext[]   ���״̬�µ�BOMLine����
	 * @throws
	 */
	    
	public static AIFComponentContext[] unpackBOMLine(TCComponentBOMLine topLine) throws TCException {
		if (topLine == null) {
			return null;
		}
		AIFComponentContext[] subLines = topLine.getChildren();
		if (subLines == null) {
			return null;
		}
		for (int i = 0; i < subLines.length; i++) {
			TCComponentBOMLine bomLines = (TCComponentBOMLine) subLines[i].getComponent();
			if (bomLines.isPacked()) {
				bomLines.unpack();
			}
		}
		return topLine.getChildren();
	}
		
	/**
	 * @Title: getBOMInfo
	 * @Description: ��ȡBOM����SAP����Ϣ
	 * @param @return    ����
	 * @return Map<String,BOMInfoModel>    ��������
	 * @throws
	 */
	    
	public Map<String, BOMInfoModel> getBOMInfo(){
		return info;
	}
	
	public Map<String, BOPInfoModel> getBOPInfo(){
		return BOPinfo;
	}
		
	/**
	 * @Title: close
	 * @Description: �ر�BOM���ڣ��ͷ���Դ
	 * @param @throws TCException    ����
	 * @return void    ��������
	 * @throws
	 */
	    
	public void close() throws TCException {
		if (window != null) {
			window.close();
		}
	}
	
}
