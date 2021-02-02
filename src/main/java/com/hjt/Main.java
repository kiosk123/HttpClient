import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.hjt.util.XMLUtil;


public class Main {
	public static void main(String[] args) {

		File file = null;
		Map<String, String> params = new HashMap<String, String>();
		
		try {
			file = new File("xml/test.xml").getCanonicalFile();
			XMLUtil.createXMLFile(file, createXMLData());
			
			//http://localhost:8081/FSYSEAI00008_ONL-initial/XMLClient , http://localhost:8081/FSYSEAI00008_ONL-initial/HTTPSRC
			HttpClient httpClient = new HttpClient("http://localhost:8083/inbound",5000,10000);
			params.put("Content-Type","text/xml;charset=EUC-KR");			
			httpClient.setRequestMethod("POST");					
			httpClient.setRequestProperties(params);
			
			String xmlString = XMLUtil.convertXMLFileToString(file);			
			httpClient.writeBody(xmlString.getBytes());
			
			int responseCode = httpClient.getResponseCode();
			System.out.println("RESPONSE_CODE : "+responseCode);
			
			//서버에서 전송받은 결과 출력
			System.out.println("------------------------- result -----------------------------");
			String responseBody = httpClient.getResponseBody();
			//System.out.println(responseBody);
			
			//XML 파싱하여 결과 출력
			//XMLUtil.parseXMLStringByDOM(responseBody);
			XMLUtil.parseXMLStringByXpath(responseBody);
			System.out.println(responseBody);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}			
	}
	
	private static Map<String, String> createXMLData(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("globalid",UUID.randomUUID().toString().replace("-",""));
		map.put("interfaceid","000000000005"); //통합서비스아이디
		map.put("res_code", "200");
		map.put("res_message", "내데이터");
		map.put("dataAttrName", "내데이터");
		map.put("item", "SHIRT");
		map.put("color", "RED");
		map.put("length", "95");
		return map;
	}		
}
