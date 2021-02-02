package client;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;


public class HttpClient {
	
	private HttpURLConnection httpURLConnection;
		
	public HttpClient(String httpAddress)throws IOException{
		httpURLConnection = (HttpURLConnection)(new URL(httpAddress).openConnection());
	}
	
	public HttpClient(String httpAddress, int connTimeOut, int readTimeOut)throws IOException{
		
		httpURLConnection = (HttpURLConnection)(new URL(httpAddress).openConnection());
		setConnectTimeout(connTimeOut);
		setReadTimeout(readTimeOut);
	}

	public void setConnectTimeout(int timeout){
		httpURLConnection.setConnectTimeout(timeout);
	}
	
	public void setReadTimeout(int timeout){
		httpURLConnection.setReadTimeout(timeout);
	}
	
	public void setRequestMethod(String method)throws IOException{
		httpURLConnection.setRequestMethod(method);
		if(method.equalsIgnoreCase("post")){
			httpURLConnection.setDoOutput(true);
		}
	}
	
	public void setRequestProperties(Map<String,String> params){	
		Set<String> keySet = params.keySet();
		for(String key : keySet){
			httpURLConnection.setRequestProperty(key, params.get(key));
		}
	}
	
	public void writeBody(byte[] body)throws IOException{
		DataOutputStream out = null;
		try {
			out = new DataOutputStream(httpURLConnection.getOutputStream());
			out.write(body);
			out.flush();
		} catch (IOException ioe) {
			throw ioe;
		}finally{
			if(out != null){try{ out.close(); }catch(IOException ioe){}}
		}
				
	}
	
	public String getResponseBody()throws IOException{
		
		String responseBody = null;
		BufferedInputStream in = null;
		ByteArrayOutputStream baos= null;
		try {
			in = new BufferedInputStream(httpURLConnection.getInputStream());
			baos = new ByteArrayOutputStream();
	        byte[] buffer = new byte[1024];
	        while(true) {
	            int len = in.read(buffer);
	            if(len < 0) {
	                break;
	            }
	            baos.write(buffer, 0, len);
	        }
	        responseBody = new String(baos.toByteArray());
		} catch (IOException ioe) {
			throw ioe;
		}finally{
			if(baos != null){try{ baos.close(); }catch(IOException ioe){}}
			if(in != null){try{ in.close(); }catch(IOException ioe){}}
		}
		return responseBody;
	}
	
	public int getResponseCode()throws IOException{
		return httpURLConnection.getResponseCode();
	}
}
