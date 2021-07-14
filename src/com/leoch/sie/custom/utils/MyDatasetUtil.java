package com.leoch.sie.custom.utils;

import java.io.File;

import com.teamcenter.rac.kernel.NamedReferenceContext;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentDataset;
import com.teamcenter.rac.kernel.TCComponentDatasetDefinition;
import com.teamcenter.rac.kernel.TCComponentDatasetDefinitionType;
import com.teamcenter.rac.kernel.TCComponentDatasetType;
import com.teamcenter.rac.kernel.TCComponentGroup;
import com.teamcenter.rac.kernel.TCComponentUser;
import com.teamcenter.rac.kernel.TCSession;

public class MyDatasetUtil {

	
	public static void replaceDatesetRelationByName(TCComponent tcc, String name, File file, String relation)
		throws Exception{
		if (relation!= null) {
			TCComponent[] coms = tcc.getRelatedComponents(relation);
			boolean flag = true;
			for (TCComponent com : coms) {
				if (com instanceof TCComponentDataset) {
					if (name.equals(com.getProperty("object_name"))) {
						flag = false;
						break;
					}
				}
			}
			if (flag) {
				TCComponentDataset dataset = createDateset(name, file, tcc.getSession());
				tcc.add(relation, dataset);
				dataset.changeOwner((TCComponentUser)tcc.getReferenceProperty("owning_user"), (TCComponentGroup)tcc.getReferenceProperty("owning_group"));
			}
		}
	}
		
	/**
	 * ͨ����ѡ�DRAG_AND_DROP_default_dataset_type����ȡ�ļ���Ӧ�����ݼ�����
	 * @param file_name �ļ�����/·��
	 * @param session
	 * @return ���ݼ�����
	 * @throws Exception
	 */
	public static String getDatasetType(String file_name, TCSession session) throws Exception {
		if (!file_name.contains(".")) {
			return "Text";
		}
		String type = null;
		String[] values = session.getPreferenceService().getStringValues("DRAG_AND_DROP_default_dataset_type");
		String suffix = file_name.substring(file_name.lastIndexOf("."), file_name.length());
		if (values != null) {
			for (String value : values) {
				String[] infos = value.split(":");
				if (infos != null && infos.length > 1) {
					if (suffix.equalsIgnoreCase("." + infos[0])) {
						type = infos[1];
						break;
					}
				}
			}
		}
		if (type == null || type.isEmpty()) {
			throw new Exception("[" + suffix + "]���ļ�����δ������ѡ��[DRAG_AND_DROP_default_dataset_type]");
		}
		return type;
	}
	
	/**
	 * ͨ�����ݼ����ͻ�ȡ���ݼ���������
	 * @param file_name �ļ�����/·��
	 * @param datasetType ���ݼ�����
	 * @param session
	 * @return ���ݼ����͵���������
	 * @throws Exception
	 */
	public static String getDatasetRefType(String file_name,String datasetType, TCSession session) throws Exception {
		if (!file_name.contains(".")) {
			return "Text";
		}
		String ref_type = "";
		TCComponentDatasetDefinitionType definitionType = (TCComponentDatasetDefinitionType) session.getTypeComponent("DatasetType");
		TCComponentDatasetDefinition def = definitionType.find(datasetType);
		NamedReferenceContext[] nameRefContexts = def.getNamedReferenceContexts();
		String suffix = file_name.substring(file_name.lastIndexOf("."), file_name.length());
		for (NamedReferenceContext nameRefContext : nameRefContexts) {
			String file_format = nameRefContext.getFileTemplate();
			if (file_format.equals("*") || file_format.equalsIgnoreCase("*" + suffix)) {
				ref_type = nameRefContext.getNamedReference();
				break;
			}
		}
		if (ref_type == null || ref_type.isEmpty()) {
			throw new Exception("[" + datasetType + "]���ݼ���֧�ִ�[*"+suffix+"]�ļ�����");
		}
		return ref_type;
	}
	
	/**
	 * ͨ�����ݼ���ȡ���ݼ���������
	 * @param dataset ���ݼ�
	 * @return ���ݼ����͵���������
	 * @throws Exception
	 */
	public static String getDatasetRefType(TCComponentDataset dataset) throws Exception {
		String ref_type = "";
		TCComponentDatasetDefinition def = dataset.getDatasetDefinitionComponent();
		NamedReferenceContext[] nameRefContexts = def.getNamedReferenceContexts();
		for (NamedReferenceContext nameRefContext : nameRefContexts) {
			ref_type = nameRefContext.getNamedReference();
			break;
		}
		return ref_type;
	}
	
	/**
	 * ͨ�����ݼ���ȡ���ݼ��ļ�����
	 * @param dataset ���ݼ�
	 * @return ���ݼ����͵��ļ�����
	 * @throws Exception
	 */
	public static String getDatasetFileType(TCComponentDataset dataset) throws Exception {
		String file_format = "";
		TCComponentDatasetDefinition def = dataset.getDatasetDefinitionComponent();
		NamedReferenceContext[] nameRefContexts = def.getNamedReferenceContexts();
		for (NamedReferenceContext nameRefContext : nameRefContexts) {
			file_format = nameRefContext.getFileTemplate();
			break;
		}
		return file_format;
	}
	
	public static TCComponentDataset createDateset(String name, File file, TCSession session)
		throws Exception{
		String file_name= file.getName();
		String datasetType = getDatasetType(file_name,  session);
		String ref = getDatasetRefType(file_name, datasetType, session);
		TCComponentDatasetType type = (TCComponentDatasetType) session.getTypeComponent("Dataset");
		TCComponentDataset dataset = type.create(name, "", datasetType);
		String[] refs = new String[] { ref };
		String[] files = new String[] { file.getAbsolutePath() };
		dataset.setFiles(files, refs);
		return dataset;
		
	}

}
