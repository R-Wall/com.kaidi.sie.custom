package com.leoch.sie.custom.sap.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCProperty;
import com.teamcenter.rac.stylesheet.PropertyNameLabel;
import com.teamcenter.rac.stylesheet.PropertyTextField;
import com.teamcenter.rac.util.PropertyLayout;

public class ViewPartStockDialog extends AbstractAIFDialog{
	    
	private static final long serialVersionUID = 2421230560256304581L;
		
	/**
	 * �鿴������
	 *
	 * @param part ѡ�е�����
	 * @param values SAP�����Ϣ
	 * @throws Exception
	 */
	    
	public ViewPartStockDialog(TCComponentItemRevision part,String[][] values) throws Exception {
		super(AIFUtility.getActiveDesktop());
		setTitle("�鿴���Ͳɹ��۸�");		
		JPanel panel = new JPanel();
		setLayout(new BorderLayout());
		getContentPane().add(panel,BorderLayout.CENTER);
		panel.setLayout(new BorderLayout());
		JPanel partInfo = new JPanel();
		partInfo.setBackground(Color.white);
		panel.add(partInfo,BorderLayout.NORTH);
		partInfo.setBorder(BorderFactory.createTitledBorder("�����Ϣ"));
		partInfo.setLayout(new PropertyLayout());
		int row = 1;
		TCProperty tcp = part.getTCProperty("item_id");
		PropertyNameLabel label = new PropertyNameLabel();
		label.load(tcp);
		PropertyTextField textField = new PropertyTextField();
		textField.load(tcp);
		textField.setColumns(15);
		textField.setEditable(false);
		partInfo.add(row + ".1.right.center", label);
		partInfo.add(row++ + ".2.right.center.resizable", textField);
		
		tcp = part.getTCProperty("object_name");
		label = new PropertyNameLabel();
		label.load(tcp);
		textField = new PropertyTextField();
		textField.load(tcp);
		textField.setColumns(15);
		textField.setEditable(false);
		partInfo.add(row + ".1.right.center", label);
		partInfo.add(row++ + ".2.right.center.resizable", textField);
		
		tcp = part.getTCProperty("item_revision_id");
		label = new PropertyNameLabel();
		label.load(tcp);
		textField = new PropertyTextField();
		textField.load(tcp);
		textField.setColumns(15);
		textField.setEditable(false);
		partInfo.add(row + ".1.right.center", label);
		partInfo.add(row++ + ".2.right.center.resizable", textField);
		
		tcp = part.getTCProperty("object_desc");
		label = new PropertyNameLabel();
		label.load(tcp);
		textField = new PropertyTextField();
		textField.load(tcp);
		textField.setColumns(15);
		textField.setEditable(false);
		partInfo.add(row + ".1.right.center", label);
		partInfo.add(row++ + ".2.right.center.resizable", textField);
		
		tcp = part.getTCProperty("release_status_list");
		label = new PropertyNameLabel();
		label.load(tcp);
		textField = new PropertyTextField();
		textField.load(tcp);
		textField.setColumns(15);
		textField.setEditable(false);
		partInfo.add(row + ".1.right.center", label);
		partInfo.add(row++ + ".2.right.center.resizable", textField);
				
		JPanel sapInfo = new JPanel();
		sapInfo.setBackground(Color.white);
		sapInfo.setBorder(BorderFactory.createTitledBorder("SAP���ͼ۸���Ϣ"));
		panel.add(sapInfo,BorderLayout.CENTER);
		sapInfo.setLayout(new BorderLayout());
		String[] summaryTitles =  new String[] { "���", "��������", "���ص�", "�������", "�������ʶ"};
		
		DefaultTableModel model = new DefaultTableModel(values, summaryTitles);
		JTable table = new JTable(model) {
			// ��д�������ǵ�Ԫ���ܱ༭
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};		
		table.getTableHeader().setReorderingAllowed(false);  
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(table);
		sapInfo.setPreferredSize(new Dimension(708,240));
		table.getColumnModel().getColumn(0).setPreferredWidth(48);
		table.getColumnModel().getColumn(1).setPreferredWidth(165);
		table.getColumnModel().getColumn(2).setPreferredWidth(165);
		table.getColumnModel().getColumn(3).setPreferredWidth(165);
		table.getColumnModel().getColumn(4).setPreferredWidth(165);
		sapInfo.add(scrollPane,BorderLayout.CENTER);
		centerToScreen();
		pack();
	}

}
