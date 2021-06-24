package com.leoch.sie.custom.sap.action;

import java.io.IOException;

import javax.swing.SwingUtilities;

import com.leoch.sie.custom.sap.dialog.ViewPartStockDialog;
import com.leoch.sie.custom.utils.SAPConn;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoTable;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.util.MessageBox;

public class ViewPartStockAction {

	TCComponentItemRevision part;
	
	public static String functionName = "ZMM_MMBE_QY";
	public static String tableName = "ITEM_OUT";
	
	/**
	 *
	 * @param part �鿴��������
	 */
		    
	public ViewPartStockAction(TCComponentItemRevision part) {
		this.part = part;
	}
		
	/**
	 * @Title: excute
	 * @Description: ִ�в鿴����߼�
	 * @param     ����
	 * @return void    ��������
	 * @throws
	 */
	    
	public void excute() {
		try {
			String id = part.getProperty("item_id");
			String[][] values = getStockBySap(id);
			SwingUtilities.invokeLater(new ViewPartStockDialog(part, values));
		} catch (Exception e) {
			MessageBox.post(e);
			e.printStackTrace();
		}
	}
		
	/**
	 * @Title: getStockBySap
	 * @Description: ��ȡSAP�Ŀ����Ϣ
	 * @param @param id ���Ϻ�
	 * @param @return
	 * @param @throws JCoException
	 * @param @throws IOException    ����
	 * @return String[][]    �����Ϣ
	 * @throws
	 */
	    
	public String[][] getStockBySap(String id) throws JCoException, IOException{
		JCoDestination destination = SAPConn.connect();
		JCoRepository repository = destination.getRepository();
		JCoFunction function = repository.getFunction(functionName);
		JCoParameterList input = function.getImportParameterList();
		input.setValue("I_MATNR_L", id);
		function.execute(destination);
		JCoParameterList tableParams = function.getTableParameterList();
		JCoTable table = tableParams.getTable(tableName);
		int rows = table.getNumRows();
		String[][] values = new String[rows][5];
		table.firstRow();
		for (int i = 0; i < rows; i++) {
			values[i][0]= (i + 1) + "";
			values[i][1] = table.getString("WERKS");
			values[i][2] = table.getString("LGORT");
			values[i][3] = table.getString("LABST");
			values[i][4] = table.getString("SOBKZ");
			table.nextRow();
		}
		return values;
	}
	
}
