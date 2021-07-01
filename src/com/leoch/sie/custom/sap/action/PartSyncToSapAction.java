package com.leoch.sie.custom.sap.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.leoch.sie.custom.sap.models.PartModel;
import com.leoch.sie.custom.utils.SAPConn;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoStructure;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.util.MessageBox;

import cocom.leoch.sie.custom.oa.action.PartSyncToOAAction;

public class PartSyncToSapAction {
	
	public static String functionName = "ZFUNC_002";
	
	public static String tableName = "TAB_LOG";

	private TCComponent[] targets;
	
	private String type;
	
	private Logger log;
	
	private boolean isNew;

	JCoRepository repository;
	
	/**
	 *
	 * @param log ��־(�����Ϸ���SAP������״̬�������SAP����־��ͬ)
	 */
			
	public PartSyncToSapAction(Logger log) {
		this.log = log;
	}
	
	/**
	 *
	 * @param targets ����Ŀ���µĶ���
	 * @param isNew �Ƿ�Ϊ�����Ϸ���SAP
	 * @param log ��־(�����Ϸ���SAP������״̬�������SAP����־��ͬ)
	 */
	    
	public PartSyncToSapAction(TCComponent[] targets, boolean isNew, Logger log) {
		this.targets = targets;
		this.isNew = isNew;
		this.log = log;
	}
	
	/**
	 * @Title: excute
	 * @Description: ִ������ͬ��SAP�߼�
	 * @param @throws TCException
	 * @param @throws JCoException
	 * @param @throws IOException    ����
	 * @return void    ��������
	 * @throws
	 */
		    
	public void excute() throws TCException, JCoException, IOException  {
		List<PartModel> models = new ArrayList<>();
		String msg = "";
		List<String> ids = new ArrayList<>();
		for (int i = 0; i < targets.length; i++) {
			TCComponent target = targets[i];
			if (target instanceof TCComponentItemRevision) {
				TCComponentItemRevision part = (TCComponentItemRevision) target;
				String part_type = part.getType();
				String revsionId = part.getProperty("item_revision_id");
				String sentToSAP = part.getProperty(PartModel.PartSentSAPFlag);
				if (part_type.endsWith("PartRevision")) {
					if( isNew  && sentToSAP.contains(revsionId)) {continue;}
					PartModel model = new PartModel(part);
					msg += model.load();
					models.add(model);
					ids.add(part.getProperty("item_id"));
				}
			}
		}
		if (!msg.isEmpty()) {
			MessageBox.post(msg, "��ʾ", MessageBox.INFORMATION);
			return;
		}
		
		if (models.size() == 0) {
			MessageBox.post("����Ŀ����û����Ҫͬ��SAP��OA�����ϣ�", "��ʾ", MessageBox.INFORMATION);
			return;
		}
		
		msg = sent(models, ids);
		if (!msg.isEmpty()) {
			MessageBox.post(msg, "����", MessageBox.ERROR);
			return;
		}
		PartSyncToOAAction synOA = new PartSyncToOAAction();
		msg = synOA.sent(models);
		if (!msg.isEmpty()) {
			MessageBox.post(msg, "����", MessageBox.ERROR);
			return;
		}
		if (isNew) {
			MessageBox.post("�����½�����SAP��OA�ɹ���OA�����̺��ǣ�"+synOA.getProcessNum(), "��ʾ", MessageBox.INFORMATION);	
		} else {
			MessageBox.post("���ϸ��·���SAP��OA�ɹ���OA�����̺��ǣ�"+synOA.getProcessNum(), "��ʾ", MessageBox.INFORMATION);	
		}
	
	}
	
	/**
	 * @Title: sent
	 * @Description: ����ͬ��SAP
	 * @param @param models ����SAP��������Ϣ
	 * @param @return
	 * @param @throws TCException
	 * @param @throws JCoException
	 * @param @throws IOException    ����
	 * @return String    ������Ϣ
	 * @throws
	 */
	    
	public String sent(List<PartModel> models, List<String> ids) throws TCException, JCoException, IOException {
		String msg = "";
		JCoDestination destination = SAPConn.connect();
		JCoRepository repository = destination.getRepository();
//		if (isNew) {
//			if (ids != null && ids.size() != 0) {
//				JCoFunction function = repository.getFunction("ZPLM_MATERIAL_CHECK");
//				JCoTable inputTable = function.getTableParameterList().getTable("I_MATERIAL_IN");
//				for (int i = 0; i < ids.size(); i++) {
//					String id = ids.get(i);
//					inputTable.insertRow(i);
//					inputTable.setValue("MATNR", id);
//				}
//				function.execute(destination);
//				JCoParameterList tableParams = function.getTableParameterList();
//				JCoTable outputTable = tableParams.getTable("I_MATERIAL_OUT");
//				int rows = outputTable.getNumRows();
//				if (rows > 0) {
//					outputTable.firstRow();
//					for (int i = 0; i < rows; i++) {
//						String existId = outputTable.getString("MATNR");
//						msg += existId + "\n";
//						outputTable.nextRow();
//					}
//					
//				}
//			}
//			if (!msg.isEmpty()) {
//				return msg + "���Ϻ��Ѵ���SAP��ȡ������ͬ��SAP����!";
//			}
//		}
		if (models == null || models.size() == 0) {
			return msg;
		}
		JCoFunction function = repository.getFunction(functionName);
		JCoParameterList input = function.getImportParameterList();
		JCoStructure structure=function.getImportParameterList().getStructure("IS_INPUT");
		
		for (int i = 0; i < models.size(); i++) {
			PartModel model = models.get(i);
			Map<String,Object> value = model.getModel();
			Set<String> keys = value.keySet();
			String info = "";
			for (String key : keys) {
				info += key + "=" + value.get(key) + "\n";
//				input.setValue(key, value.get(key));
				structure.setValue(key, value.get(key));
			}
			String id = value.get("MATNR") + ":";
			System.out.println("ID:" + id + "\n" + info);
			input.setValue("IS_INPUT", structure);
			function.execute(destination);
			structure.clear();
			JCoStructure js = function.getExportParameterList().getStructure("ES_RETURN");//���ýӿڷ���״̬
//			JCoParameterList tableParams = function.getTableParameterList();
//			JCoTable table = tableParams.getTable(tableName);
//			table.firstRow();
//			type = table.getString("TYPE");
//			String message = table.getString("MESSAGE");
			type = 	js.getString("TYPE");
			String message = js.getString("MSG");			
			if (!"S".equals(type)) {
				message = "SAP ERROR:"+ id + message;
				log.error(message);
				msg += message + "\n";
			} else {
				model.setSentSAPFlag();
				log.info(message);
			}
		}
		return msg;
	}	
		
}
