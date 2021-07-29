package com.leoch.sie.custom.sap.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.leoch.sie.custom.sap.logs.NewBOMLog;
import com.leoch.sie.custom.sap.models.BOMInfoModel;
import com.leoch.sie.custom.sap.models.BOMLineModel;
import com.leoch.sie.custom.sap.models.BOMStruct;
import com.leoch.sie.custom.sap.models.PartModel;
import com.leoch.sie.custom.utils.SAPConn;
import com.leoch.sie.custom.utils.Sort;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.util.MessageBox;

import cocom.leoch.sie.custom.oa.action.PartSyncToOAAction;

public class BOMSentToSAPAction {
	
	List<TCComponentItemRevision> revs;
	TCSession session;
	
	public static String functionName = "ZFUNC_005";
	
	public static String input_BOM_HDR = "I_INPUT";
	
	public static String input_BOM_ITEM = "T_TAB";
	
	public static String export_Table = "E_OUTPUT";
	
	private Logger log = NewBOMLog.logger;
	

	/**
	 * 	     
	 * @param revs ����Ŀ���·���SAP�����ϰ汾
	 */
		    
	public BOMSentToSAPAction(List<TCComponentItemRevision> revs) {
		this.revs = revs;
	}
	
	/**
	 * @Title: excute
	 * @Description: ִ�з���SAP�߼�
	 * @param     ����
	 * @return void    ��������
	 * @throws
	 */
	    
	public void excute() {
		session = (TCSession) AIFUtility.getDefaultSession();
		if (session == null) {
			MessageBox.post(new TCException("��ȡTC�Ựʧ��,�޷���ȡϵͳ���ݣ�"));
			return;
		}
		
		try {
			String msg = "";
			List<PartModel> partModels = new ArrayList<>();
			List<String> ids = new ArrayList<>();
			for (int i = 0; i < revs.size(); i++) {
				TCComponentItemRevision part = revs.get(i);
				String revsionId = part.getProperty("item_revision_id");
				String  sentToSAP = part.getProperty(PartModel.PartSentSAPFlag);
				if (!sentToSAP.contains(revsionId)) {
					PartModel model = new PartModel(part);
					msg += model.load();
					partModels.add(model);
					ids.add(part.getProperty("item_id"));
				}
			}
			
			if (!msg.isEmpty()) {
				MessageBox.post(msg, "��ʾ", MessageBox.INFORMATION);
				return;
			}
			//��������
			if (partModels != null && partModels.size() != 0) {
				PartSyncToSapAction action = new PartSyncToSapAction(log);
				msg = action.sent(partModels,ids);
				if (!msg.isEmpty()) {
					MessageBox.post(msg, "��ʾ", MessageBox.INFORMATION);
					return;
				}
			}
			//����OA
			String  oaMsg  = "";
			if (partModels != null && partModels.size() != 0) {
				PartSyncToOAAction synOA = new PartSyncToOAAction();
				msg = synOA.sent(partModels);
				if (!msg.isEmpty()) {
					MessageBox.post(msg, "����", MessageBox.ERROR);
					return;
				}
				oaMsg = ",�����½�����SAP��OA�ɹ���OA�����̺��ǣ�"+synOA.getProcessNum();
			}
			
			//��BOM
			BOMStruct struct = new BOMStruct(revs, session); // ����ecnNO����ʱ��ΪBOM����
			msg = struct.load();
			struct.close();
			if (!msg.isEmpty()) {
				log.error(msg);
				MessageBox.post(msg, "��ʾ", MessageBox.INFORMATION);
				return;
			}
			
			Map<String, BOMInfoModel> models = struct.getBOMInfo();
			if (models.size() == 0) {
				MessageBox.post("����Ŀ����û����Ҫͬ��SAP��BOM��", "��ʾ", MessageBox.INFORMATION);
				return;
			}
			msg = sent(models);
			if (msg != null && !msg.isEmpty()) {
				MessageBox.post(msg, "����", MessageBox.ERROR);
				return;
			}
			
			MessageBox.post("BOM����SAP�ɹ�"+oaMsg, "��ʾ", MessageBox.INFORMATION);
		} catch (JCoException | TCException | IOException e) {
			e.printStackTrace();
			log.error(e);
			MessageBox.post(e);
		}
		
	}
	
	/**
	 * @Title: sent
	 * @Description: ����SAP
	 * @param @param models ��Ҫ����SAP��BOM��Ϣ
	 * @param @return
	 * @param @throws JCoException
	 * @param @throws TCException
	 * @param @throws IOException    ����
	 * @return String    ���ش�����Ϣ
	 * @throws
	 */
		    
	public String sent(Map<String, BOMInfoModel> models) throws JCoException, TCException, IOException{
		String msg = "";
		JCoDestination destination = SAPConn.connect();
		JCoRepository repository = destination.getRepository();
//		JCoFunction function = repository.getFunction(functionName);
		Collection<BOMInfoModel> list = models.values();
		List<BOMInfoModel> bomInfos = new ArrayList<>();
		bomInfos.addAll(list);
		if (bomInfos.size() > 1) {
			Collections.sort(bomInfos, new Sort());
		}
		for (BOMInfoModel bomInfo : bomInfos) {
			JCoFunction function = repository.getFunction(functionName);
			Map<String, Object> values = bomInfo.getModel();
			JCoStructure headTable = function.getImportParameterList().getStructure(input_BOM_HDR);
			Set<String> keys = values.keySet();
			String topID = values.get("MATNR") + ":";
			String info = "";
			for (String key : keys) {
				info += key + "=" + values.get(key) + "\n";
				headTable.setValue(key, values.get(key));
			}
			System.out.println("TopBOM:" + topID + "\n" + info);
			List<BOMLineModel>  bomlineInfos = bomInfo.getBOMLinModel();
			JCoTable bomlineTable = function.getTableParameterList().getTable(input_BOM_ITEM);
			for (int i = 0; i < bomlineInfos.size(); i++) {
				BOMLineModel bomlineInfo = bomlineInfos.get(i);
				bomlineTable.insertRow(i);
				values = bomlineInfo.getModel();
				keys = values.keySet();
				String childID = values.get("IDNRK") + "";
				info = "";
				for (String key : keys) {
					info += key + "=" + values.get(key) + "\n";
					bomlineTable.setValue(key, values.get(key));
				}
				System.out.println("SubLine:" + childID + ":\n" + info);
			}
			function.execute(destination);
			JCoStructure table = function.getExportParameterList().getStructure(export_Table);

			String type = 	table.getString("STA");
			String message = table.getString("MESSAGE");			
			if (!"S".equals(type)) {
				message = "SAP ERROR:"+ topID+ message;
				log.error(message);
				msg += message + "\n";
			} else {
				bomInfo.setSentSAPFlag();
				log.info(message);
			}
			if (msg != null && !msg.isEmpty()) {
				return msg;
			}
			headTable.clear();
			bomlineTable.clear();
			function.clone();
		}
		return msg;
	}
}
