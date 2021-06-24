package com.leoch.sie.custom.sap.logs;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class NewBOMLog {
	
	public static Logger logger = Logger.getLogger("newBOMLog");
	public static String configPath = "configuration" + File.separator + "newBOMLog.properties";
	
	static {
		initLogConfig();
	}
		
	public static void initLogConfig() {
		String url = null;
		try {
			File file = new File(".");
			String fullPath = file.getCanonicalPath();
			if (!fullPath.endsWith(File.separator)) {
				fullPath += File.separator;
			}
			url = fullPath + configPath;
			System.out.println("��־�����ļ�Ĭ��·��Ϊ��" + url);
			file = new File(url);
			if (!file.exists()) {
				URL url4j = NewPartLog.class.getResource("newBOMLog.properties");
				System.setProperty("log4j.configuration", url4j.toString());
				PropertyConfigurator.configure(url4j);
			} else {
				System.out.println("��ȡlog4j�����ļ��ɹ�,�����ļ�·��Ϊ: " + url);
				System.setProperty("log4j.configuration", url);
				PropertyConfigurator.configure(new URL("file:/" + url));
			}
		} catch (IOException e) {
			try {
				PropertyConfigurator.configure(new URL(url));
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

}
