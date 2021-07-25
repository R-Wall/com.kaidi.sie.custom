package cocom.leoch.sie.custom.oa.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.leoch.sie.custom.sap.models.PartModel;
import com.leoch.sie.custom.utils.MyPerference;
import com.teamcenter.rac.kernel.TCException;

public class PartSyncToOAAction {
	
//	private static String  url_address = "http://192.168.1.145:88/services/CreateWorkflowService";  
	private static String  url_address = null;
	private String processNum = ""; 
	@SuppressWarnings("unused")
	private HttpURLConnection getHTTPConnection() throws IOException {
		URL url = new URL(url_address);  
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();  
        connection.setRequestMethod("POST");  
        connection.setRequestProperty("content-type", "text/xml;charset=utf-8");  
        connection.setDoInput(true);  
        connection.setDoOutput(true); 
		return connection;
		
	}
	
	
	public  String sent(List<PartModel> models) throws IOException, TCException {
		url_address = MyPerference.getOAAddress();
		url_address = url_address+"/services/CreateWorkflowService";
		String msg = "";
		if (models == null || models.size() == 0) {
			return msg += "û����Ҫ���͵�OA�����ϡ�";
		}
		String json = "";
		for (int i = 0; i < models.size(); i++) {
			PartModel model = models.get(i);
			Map<String,Object> value = model.getModel();
		    json += getJSON(value);	
		    if( i+1  < models.size()) {
				json += ",";
			}
		}
	   //��֯SOAP���ݣ���������  
	   String soapXML = getXML(json);
	   System.out.println(soapXML);
	   HttpURLConnection connection = getHTTPConnection();
       OutputStream os = connection.getOutputStream();  
       os.write(soapXML.getBytes("UTF-8"));  
        
        //���շ������Ӧ
        int responseCode = connection.getResponseCode();  
        if(200 == responseCode){
            InputStream is = connection.getInputStream();  
            InputStreamReader isr = new InputStreamReader(is,"UTF-8");  
            BufferedReader br = new BufferedReader(isr);  
              
            StringBuilder sb = new StringBuilder();  
            String temp = null;  
            while(null != (temp = br.readLine())){  
                sb.append(temp);  
            }  
            
            //�������ֵ
            String returnMSG = getReturn(sb.toString());
            if(returnMSG != null ){
            	msg += returnMSG;
            }
            is.close();  
            isr.close();  
            br.close();
        }else {
        	msg += "���Ϸ���OAʧ�ܣ�û�л�ȡ��OA���������ӣ�.";
        }  
        os.close();  
		return msg;
	}
	 
	 public String getProcessNum() {
		return processNum;
	}


	public void setProcessNum(String processNum) {
		this.processNum = processNum;
	}


	private String getReturn(String str) {
	   try {
		    String isSuccess = str.substring(str.indexOf("{\"isSuccess\": \"")+15, str.indexOf("\",\"message\":"));
			if(isSuccess != null && isSuccess.equals("S")) {
				processNum = str.substring(str.indexOf("\"requestid\": \"")+14, str.indexOf("\"}</ns1:out>"));
				return null;
			}else {
				return str.substring(str.indexOf("\",\"message\": \"")+14, str.indexOf("\"}</ns1:out>"));
			}
	   }catch (Exception e) {
		   return "���Ϸ���OAʧ�ܣ�OA�ķ��ؽ�����ԣ�.";
	   }
	}
	

	public String getJSON(Map<String,Object> value){  
		 String info = "{"
				  +"\"MARC-WERKS\": \""+value.get(PartModel.WERKS)+"\"," 	          //<!--���� ����--> <!--PLM�ӿ��ĵ����Ǳ�����-->
				  +"\"MARA-MTART\": \""+value.get(PartModel.MTART)+"\"," 	          //<!--�������� ����-->
				  +"\"MARA-MATNR\": \""+value.get(PartModel.MATNR)+"\"," 	          //<!--SAP���ϱ��� -->
				  +"\"MAKT-MAKTX\": \""+value.get(PartModel.MAKTX)+"\"," 	          //<!--�������� ����-->
				  +"\"MARA_ZEINR\": \""+value.get(PartModel.ZEINR)+"\"," 	          //<!--ͼ��-->
				  +"\"MARA-MEINS\": \""+value.get(PartModel.MEINS)+"\"," 	          //<!--����������λ ����-->
				  +"\"MARA-MATKL\": \""+value.get(PartModel.MATKL)+"\"," 	          //<!--������ ����-->
				  +"\"MARA-BISMT\": \""+value.get(PartModel.BISMT)+"\"," 	          //<!--�����Ϻ�-->
				  +"\"MARA-GROES\": \""+value.get(PartModel.GROES)+"\"," 	          //<!--��С����-->
				  +"\"MARA-BRGEW\": \""+value.get(PartModel.BRGEW)+"\"," 	          //<!--ë��-->
				  +"\"MARA-NTGEW\": \""+value.get(PartModel.NTGEW)+"\"," 	          //<!--����-->
				  +"\"MARA-GEWEI\": \""+value.get(PartModel.GEWEI)+"\"," 	          //<!--������λ-->
				  +"\"MARA_NORMT\": \""+value.get(PartModel.NORMT)+"\"," 	          //<!--�ڲ�����-->
				  +"\"MARA_FERTH\": \""+value.get(PartModel.FERTH)+"\"," 	          //<!--ģѨ-->
				  +"\"MARA-MSBOOKPARTNO\": \""+value.get(PartModel.MSBOOKPARTNO)+"\"," 	 //<!--��Ʒϵ��--> <!--PLM�ӿ��ĵ�δ�ṩ-->
				  +"\"GRUN\": \""+value.get(PartModel.GRUN)+"\"," 	                //<!--���ϳ�����-->
				  +"\"MARC-BESKZ\": \""+value.get(PartModel.BESKZ)+"\"," 	          //<!--�ɹ����� ����-->
				  +"\"MARC-SOBSL\": \""+value.get(PartModel.SOBSL)+"\"," 	        //<!--����ɹ���-->
				  +"\"MARC-RGEKZ\": \""+value.get(PartModel.RGEKZ)+"\""+	          //<!--����-->
				"}";

		 return info;	
	    }  
	 
	    public String getXML(String xml){  
	    	String soapXML = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"webservices.createworkflow.weaver.com.cn\">" 
	    			+"   <soapenv:Header/>" 
	    	        +"   <soapenv:Body>" 
	    			+"      <web:createWl>"  
	    	            +"    <web:in0>"  
	    	            	+" {"
	    	            		+ "\"detailtable\": ["
	    	            			+xml
	    	            		+ "]"
	    	            	+"}"
	    	            +"    </web:in0>"  
	    	        +"      </web:createWl>"  
	    	        +"   </soapenv:Body>" 
	    	        +"</soap:Envelope>";  
	        return soapXML;  
	    }  
}

