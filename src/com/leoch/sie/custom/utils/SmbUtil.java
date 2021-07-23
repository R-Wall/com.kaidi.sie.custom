package com.leoch.sie.custom.utils;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.Date;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;
 
public class SmbUtil {
	// 1. ��������
	private String url = "smb://userName:password@192.168.2.153/mars/";
	private SmbFile smbFile = null;
	private SmbFileOutputStream smbOut = null;
	private static SmbUtil smbUtil = null; // �����ļ�Э��
	
	private SmbUtil(String url) {
		this.url = url;
		this.init();
	}
	// 2. �õ�SmbUtil�����ӵķ���
	public static synchronized SmbUtil getInstance(String url) {
		if (smbUtil == null)
			return new SmbUtil(url);
		return smbUtil;
	}
 
	
	// 3.smbFile����
	public void init() {
		try {
			System.out.println("��ʼ����...url��" + this.url);
			smbFile = new SmbFile(this.url);
			smbFile.connect();
			System.out.println("���ӳɹ�...url��" + this.url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.print(e);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.print(e);
		}
	}
	
	// 4.�ϴ��ļ���������
	public int uploadFile(File file) {
		int flag = -1;
		BufferedInputStream bf = null;
		try {
			SmbFile folder = new SmbFile(this.url);
			if(!folder.exists()){
				folder.mkdir();
			}
			this.smbOut = new SmbFileOutputStream(this.url + "/"
					+ file.getName(), false);
			bf = new BufferedInputStream(new FileInputStream(file));
			byte[] bt = new byte[8192];
			int n = bf.read(bt);
			while (n != -1) {
				this.smbOut.write(bt, 0, n);
				this.smbOut.flush();
				n = bf.read(bt);
			}
			flag = 0;
			System.out.println("�ļ��������...");
		} catch (SmbException e) {
			e.printStackTrace();
			System.out.println(e);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println(e);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println("�Ҳ�������...url��" + this.url);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		} finally {
			try {
				if (null != this.smbOut)
					this.smbOut.close();
				if (null != bf)
					bf.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
 
		return flag;
	}
	
	public static File downloadFile(String smbMachine,String localPath){
		File localfile = null;
		InputStream is = null;
		OutputStream os = null;
		try{
			//Զ�̶�ȡ�ļ�
			SmbFile rmiFile = new SmbFile(smbMachine);
			String filename = rmiFile.getName(); //��ȡ�ļ���
			is = new BufferedInputStream(new SmbFileInputStream(rmiFile)); //���ļ����ж�ȡ			
			//��Զ���ļ�д������
			localfile = new File(localPath + File.separator + filename);  //��Զ�̿������ļ���ָ��������ı��صľ���·��
			System.out.println("lcoalfile:" + localfile);
			os = new BufferedOutputStream(new FileOutputStream(localfile)); 
			int length = rmiFile.getContentLength();  //��ȡ�ļ������ݴ�С
			System.out.println("length:" + length);
			byte[] buffer = new byte[length];
			is.read(buffer); 
			os.write(buffer);  //��ʼд
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				if(is!=null){
					is.close();
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		return localfile;
	}
 
	// 5. ��main�����������
	public static void main(String[] args) {
		System.out.println(new Date());
		// ��������ַ ��ʽ�� smb://�����û���:��������@����IP��ַ/IP������ļ���
		String remoteUrl = "smb://aaa:123456@192.168.1.145/fenfa";
		String localFile = "D:\\Share\\334.pdf"; // ����Ҫ�ϴ����ļ�
		String localPath = "D:\\Share";
//		smb://aaa:123456@192.168.1.145/2021-07-22/1.1���̸����������̣�����ECR��-����-2021-05-18_offline_html_83336.zip
		String smbMachine = "smb://aaa:123456@192.168.1.145/share/123R.xlsx";
		File file = new File(localFile);
		SmbUtil smb = SmbUtil.getInstance(remoteUrl);
		downloadFile(smbMachine,localPath);
//		smb.uploadFile(file);// �ϴ��ļ�
//		System.out.println(new Date());
	}
}
