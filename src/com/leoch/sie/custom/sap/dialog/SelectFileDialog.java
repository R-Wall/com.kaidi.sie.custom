package com.leoch.sie.custom.sap.dialog;

import java.awt.Frame;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

public class SelectFileDialog {

	Frame frame = null;
	String path = null;
	File file = null;
	
	public SelectFileDialog(Frame frame){
	
		this.frame = frame;
	}
	
	public void showSelectFileDialog(){
		int result = 0;
		
		JFileChooser fileChooser = new JFileChooser();
		
		FileSystemView fsv = FileSystemView.getFileSystemView(); //ע���ˣ�������Ҫ��һ��
		
		System.out.println("����·��"+fsv.getHomeDirectory()); //�õ�����·��
		
		fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
		
		fileChooser.setDialogTitle("��ѡ������·��...");
		fileChooser.setApproveButtonText("ȷ��");
		
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		result = fileChooser.showOpenDialog(frame);
		
		if (JFileChooser.APPROVE_OPTION == result) {
			path=fileChooser.getSelectedFile().getPath();
			//System.out.println("path: "+path);
		}
	}
	
	public String getSelectFilePath(){
		return path;
	}
}
