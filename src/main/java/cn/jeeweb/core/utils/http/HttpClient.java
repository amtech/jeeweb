package cn.jeeweb.core.utils.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;


public class HttpClient {
	
	private final static Logger logger = Logger.getLogger(HttpClient.class);

	 /**
	 * @Title: doPost
	 * @Description: TODO  一般的http、https(无证书或者全证书)的请求
	 * @param url
	 * @param map
	 * @param charset
	 * @return String
	 * @author wmm
	 * @date 2017年7月20日下午5:38:00
	 */
	public static String doPost(String url,Map<String,Object> map,String charset){  
	        String result = "";  
	        CloseableHttpClient httpClient = HttpClients.createDefault();
	        CloseableHttpResponse httpResponse = null;
	        try{
	        	HttpPost httpPost = new HttpPost(url);
	 	        httpPost.addHeader("User-Agent", charset);
	 	        if (map != null) {
	                 List<NameValuePair> paramList = new ArrayList<NameValuePair>();
	                 for (Map.Entry<String, Object> param : map.entrySet()) {
	                     NameValuePair pair = new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue()));
	                     paramList.add(pair);
	                 }
	                 httpPost.setEntity(new UrlEncodedFormEntity(paramList, charset));
	             }
	 	        httpResponse = httpClient.execute(httpPost);
	 	        HttpEntity entity = httpResponse.getEntity();
	 	        if (entity != null) {
	 	        	result = EntityUtils.toString(entity);
	             }
	        }catch (Exception e) {
	        	logger.info("doPost 方法出现异常");
	        	e.printStackTrace();
			}finally {
				try {
					httpResponse.close();
					httpClient.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	        return result;  
	    }  
	/**
	 * @Title: doPostSSL
	 * @Description: TODO 加证书的请求
	 * @param url
	 * @param map
	 * @param charset
	 * @return String
	 * @author wmm
	 * @date 2017年7月20日下午5:39:12
	 */
	public static String doPostSSL(String url,String filePath,Map<String,Object> map,String charset){  
		
		
        String result = "";  
        //CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try{
        	 KeyStore trustStore  = KeyStore.getInstance(KeyStore.getDefaultType());
             //加载证书文件
             FileInputStream instream = new FileInputStream(new File(filePath));
             try {
                 trustStore.load(instream, "mypassword".toCharArray());
             } finally {
                 instream.close();
             }
             SSLContext sslcontext = SSLContexts.custom()
                     .loadTrustMaterial(trustStore)
                     .build();

             SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,
                     SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
             httpClient = HttpClients.custom()
                     .setSSLSocketFactory(sslsf)
                     .build();
             
        	HttpPost httpPost = new HttpPost(url);
 	        httpPost.addHeader("User-Agent", charset);
 	        if (map != null) {
                 List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                 for (Map.Entry<String, Object> param : map.entrySet()) {
                     NameValuePair pair = new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue()));
                     paramList.add(pair);
                 }
                 httpPost.setEntity(new UrlEncodedFormEntity(paramList, charset));
             }
 	        httpResponse = httpClient.execute(httpPost);
 	        HttpEntity entity = httpResponse.getEntity();
 	        if (entity != null) {
 	        	result = EntityUtils.toString(entity);
             }
        }catch (Exception e) {
        	logger.info("doPost 方法出现异常");
        	e.printStackTrace();
		}finally {
			try {
				httpResponse.close();
				httpClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        return result;  
    } 
	
	
	 /**
	 * @Title: doGet
	 * @Description: TODO  get请求
	 * @param url
	 * @param charset
	 * @return String
	 * @author wmm
	 * @date 2017年7月20日下午5:38:47
	 */
	public static String doGet(String url,String charset){
		 String result = "";  
         CloseableHttpClient httpClient = HttpClients.createDefault();
         CloseableHttpResponse httpResponse = null;
	     try{
	    	 HttpGet httpGet = new HttpGet(url);
	    	 httpGet.addHeader("User-Agent", charset);
	         httpResponse = httpClient.execute(httpGet);
	         HttpEntity entity = httpResponse.getEntity();
	         if (entity != null) {
 	        	result = EntityUtils.toString(entity);
             }
	     }catch (Exception e) {
	    	 logger.info("doGet 方法出现异常");
	    	 e.printStackTrace();
		 }finally {
			 try {
				httpResponse.close();
				httpClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		 return result; 
	 }
	 
	 public static void main(String[] args) {
		HttpClient hc = new HttpClient();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("tel", "15233837943");
		String s = "";
		//s = hc.doPost("https://tcc.taobao.com/cc/json/mobile_tel_segment.htm", map, "UTF-8");
		s = hc.doGet("https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=15233837943", "UTF-8");
		System.err.println("测试结果： " + s);
	}
}
