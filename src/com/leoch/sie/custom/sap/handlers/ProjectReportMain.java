package com.leoch.sie.custom.sap.handlers;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.leoch.sie.custom.utils.SAPConn;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentProject;
import com.teamcenter.rac.kernel.TCComponentSchedule;
import com.teamcenter.rac.kernel.TCComponentScheduleTask;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;

public class ProjectReportMain {
	
	
	private TCComponent[] projects;
	private List<LinkedHashMap<String, String>> projectValueList;
	private LinkedHashMap<String, String> projectValueMap;
	private int count = 0;
	private String path = null;
	private String[] propetys = new String[] {"�󻭷���","��Ʒ������","��ͼ","��������","������","��֤����","��֤ȡ��","����"};
	
	public  ProjectReportMain(Date date1,Date date2,String path) throws Exception {
		this.path = path;
		TCSession session = (TCSession) AIFUtility.getDefaultSession();	
		session.setStatus("��̨���ڵ���...");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm");
		String aftertime = sdf.format(date1);
		String beforetime = sdf.format(date2);
		projects = session.search("Search Project", new String[] { "Before" ,"After"}, new String[] {beforetime,aftertime});
	}
	
	public String excute() throws Exception {
		count = 0;
		projectValueList = new ArrayList<LinkedHashMap<String, String>>(); 
		if(projects.length>0) {
			for (int i = 0; i < projects.length; i++) {
				TCComponent tcComponent = projects[i];
				getProjectValue(tcComponent);				
			}
			wirteExcel();
			return null;
		}else {
			return "��ʱ���û�в�ѯ����Ŀ��";
		}
				
	}
	
	private void wirteExcel() throws IOException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
		String datehm = sdf.format(new Date());
		
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		InputStream is = this.getClass().getResourceAsStream("/resources/������Ŀ����ģ��.xlsx");
		if(is==null) {
			throw new IOException("�Ҳ���ģ���ļ����޷�����");
		}
		XSSFWorkbook wb = new XSSFWorkbook(is);	
		XSSFSheet sheet = wb.getSheetAt(0);
		String name = sheet.getSheetName();
		System.out.println(name);
		for (int i = 0; i < projectValueList.size(); i++) {
			valueMap = projectValueList.get(i);
			XSSFRow row = sheet.getRow(i+4);
			if(row==null) {
				row = sheet.createRow(i+4);
			}
			int column = 0;
			for(String value:valueMap.values()) {
				XSSFCell cell = row.getCell(column);
				if (cell == null) {
					cell = row.createCell(column);
				}
				cell.setCellValue(value);
				column = column+1;
			}
		}
		setAutoWidth(sheet,27);
		sheet.setColumnWidth(0, 1000);
//		String path = System.getProperty("user.home");
		FileOutputStream fileOut = new FileOutputStream(path+"\\"+datehm+"��Ŀ����.xlsx");
		wb.write(fileOut);
		fileOut.close();
		File file = new File(path+"\\"+datehm+"��Ŀ����.xlsx");
		Desktop.getDesktop().open(file);

	}

	public void getProjectValue(TCComponent project) throws Exception {
		count = count+1;

		projectValueMap = new LinkedHashMap<String, String>();
		
		projectValueMap.put("���", count+"");
		
		String name  = project.getProperty("object_name");
		projectValueMap.put("��Ŀ����", name);
		
		String project_id = project.getProperty("project_id");
		projectValueMap.put("��ĿID", project_id);
		
		String project_desc = project.getProperty("project_desc");
		projectValueMap.put("��Ŀ����", project_desc);
		
		String production_num = project.getProperty("p8_production_num");
		projectValueMap.put("������", production_num);
		
		String development_level = project.getProperty("p8_development_level");
		projectValueMap.put("�����ȼ�", development_level);
		
		String market = project.getProperty("p8_market");
		projectValueMap.put("�г�", market);
		
		String plan_quantity = project.getProperty("p8_plan_quantity");
		projectValueMap.put("��̨��", plan_quantity);
		
		String plan_user = project.getProperty("p8_plan_user");
		projectValueMap.put("�󻭵���", plan_user);
		
		String design_user = project.getProperty("p8_design_user");
		projectValueMap.put("��Ƶ���", design_user);

		String production_base = project.getProperty("p8_production_base");
		projectValueMap.put("�����ݵ�", production_base);

		TCComponent schedule = getSchedule(project);
		if(schedule!=null) {
			getScheduleValue(schedule);
		}		
		projectValueList.add(projectValueMap);
		
	}
	
	public TCComponent getSchedule(TCComponent project) throws TCException {
		
		 project.refresh();
		 TCComponentProject project1 = (TCComponentProject) project;
		 TCComponent[] comps = project1.getRelatedComponents();	
		 TCComponent scheduleTask = null;
		 for (int i = 0; i < comps.length; i++) {
			 if(comps[i] instanceof TCComponentSchedule) {
				String baseschedulename = comps[i].getProperty("based_on");
				if(baseschedulename.contains("��Ʒ")) {
					TCComponentSchedule schedule= (TCComponentSchedule)comps[i];
					scheduleTask = schedule.getRelatedComponent("fnd0SummaryTask");		
				}
			 }else if(comps[i] instanceof TCComponentScheduleTask){
				 TCComponent temp = comps[i].getRelatedComponent("schedule_tag");
				 String baseschedulename = temp.getProperty("based_on");
				 if(baseschedulename.contains("��Ʒ")) {
					scheduleTask = comps[i];
				}
			 }
		}
		return scheduleTask;
	}
	
	public void getScheduleValue(TCComponent scheduleTask) throws Exception {
		
		String temp = null;
		String start_date = null;
		String finish_date = null;
		AIFComponentContext[] childs = scheduleTask.getChildren();
		TCComponent comp = null;
		List<String> resultList= new ArrayList<>(Arrays.asList(propetys));	
		HashMap<String, TCComponent> childsMap = new HashMap<String, TCComponent>();
		for (int i = 0; i< childs.length; i++) {
			comp = (TCComponent) childs[i].getComponent();
			temp =comp.getProperty("object_name");
			if(resultList.contains(temp)) {
				childsMap.put(temp, comp);
			}
		}
		if(childsMap.size()>0) {
			for (int i = 0; i < resultList.size(); i++) {
				comp = childsMap.get(resultList.get(i));
				if(comp!=null) {
					start_date = comp.getProperty("start_date");
					finish_date = comp.getProperty("actual_finish_date");
					projectValueMap.put(resultList.get(i)+"��ʼʱ��",start_date);
					projectValueMap.put(resultList.get(i)+"����ʱ��",finish_date);
				}else {
					projectValueMap.put(resultList.get(i)+"��ʼʱ��","");
					projectValueMap.put(resultList.get(i)+"����ʱ��","");
				}

			}
		}
	}
	
	/**�Զ������п�
	 * @param sheet
	 * @param columnNum
	 */
	public void setAutoWidth(XSSFSheet sheet,int columnNum) {
		
		//���п����ŵ������г��Զ���Ӧ
        for (int colNum = 0; colNum < columnNum; colNum++) {
            int columnWidth = sheet.getColumnWidth(colNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                XSSFRow currentRow;
                //��ǰ��δ��ʹ�ù�
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }
                if (currentRow.getCell(colNum) != null) {
                    XSSFCell currentCell = currentRow.getCell(colNum);
                    if (currentCell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            sheet.setColumnWidth(colNum, (columnWidth+4) * 256);         
        }
	}

}
