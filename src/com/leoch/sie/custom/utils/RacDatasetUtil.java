package com.leoch.sie.custom.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.teamcenter.rac.kernel.NamedReferenceContext;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentDataset;
import com.teamcenter.rac.kernel.TCComponentDatasetDefinition;
import com.teamcenter.rac.kernel.TCComponentTcFile;

public class RacDatasetUtil {
	
	public static NamedReferenceContext[] getNameRefContext(TCComponentDataset dataset) throws Exception {
		
		TCComponentDatasetDefinition datasetDef = dataset.getDatasetDefinitionComponent();

	    NamedReferenceContext[] nameRefContexts = datasetDef.getNamedReferenceContexts();
	    
	    return nameRefContexts;
	}
	
	public static List<TCComponentDataset> getDatasets(TCComponent tcComponent) throws Exception{
		TCComponent[] relateds = tcComponent.getRelatedComponents();
		List<TCComponentDataset> datasets = new ArrayList<>();
		if(relateds != null && relateds.length > 0) {
			for (TCComponent related : relateds) {
				if(related instanceof TCComponentDataset) {
					datasets.add((TCComponentDataset) related);
				}
			}
		}
		return datasets;
	}
	
	/**
	 *   ����������õ����ݼ��е��������ݼ��ļ����������Զ����MyRacDatasetFile�࣬������download��������ֱ���������ݼ�
	 * @param dataset
	 * @return
	 * @throws Exception
	 */
	public static List<MyRacDatasetFile> getRacDatasetFile(TCComponentDataset dataset, TCComponent parent) throws Exception {
		
		TCComponent[] refList = dataset.getReferenceListProperty("ref_list");
		List<MyRacDatasetFile> racDatasetFiles = new ArrayList<>();
		if(refList != null && refList.length > 0) {
			for (TCComponent ref : refList) {
				if(ref instanceof TCComponentTcFile) {
					TCComponentTcFile tcFile = (TCComponentTcFile) ref;
					racDatasetFiles.add(new MyRacDatasetFile(tcFile, parent));
				}
			}
		}
		
		return racDatasetFiles;
	}
	
	public static HashMap<String, File> getTCFile(TCComponentDataset dataset, String folderName) throws Exception{
		
	      String datasetName = dataset.getProperty("object_name");
//	      SmbFile remoteFile = new SmbFile(remoteUrl);
	      HashMap<String, File> filesMap = new HashMap<String, File>();
	      
	      TCComponentDatasetDefinition datasetDef = dataset.getDatasetDefinitionComponent();

	      NamedReferenceContext[] nameRefContexts = datasetDef.getNamedReferenceContexts();
	      if ((nameRefContexts == null) || (nameRefContexts.length == 0)) return null;

	      NamedReferenceContext nf = nameRefContexts[0];
	      String namedRef = nf.getNamedReference();

	      String[] fileNames = dataset.getFileNames(namedRef);
	      if ((fileNames == null) || (fileNames.length == 0)) return null;

	      for (String fileName : fileNames) {
	        File ret = dataset.getFile(namedRef, fileName, folderName);

	        if (ret == null) {
	        	System.out.println("�����ļ��쳣��" + folderName + "/" + fileName);
	        }
	        filesMap.put(fileName, ret);
	        String info = fileName + " = " + datasetName;
	        System.out.println(info);
	      }
	      return filesMap;
	  }
}
