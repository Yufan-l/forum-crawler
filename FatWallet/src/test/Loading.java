package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class Loading {
	
	public static void LoadPage(String url) throws Exception
	{
		
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		String strDate = sdfDate.format(now);
	
		HttpClient httpclient = new DefaultHttpClient();
	    	try {
	    		File file1 = new File("C:/output.html");
	    		if (!file1.exists()) {
	    			file1.createNewFile();
	    		}
	    		FileWriter fw1 = new FileWriter(file1.getAbsoluteFile());
	    		final BufferedWriter bw1 = new BufferedWriter(fw1);
	    		
	            HttpGet httpget = new HttpGet(url);
	            System.out.println("executing request " + httpget.getURI());
	            HttpResponse response = httpclient.execute(httpget);
	            HttpEntity entity = response.getEntity();

	            System.out.println("----------------------------------------");
	            System.out.println(response.getStatusLine());
	            if (entity != null) {
	                System.out.println("Response content length: " + entity.getContentLength());
	                Scanner SC=new Scanner(entity.getContent());

	                while(SC.hasNext())
	                
	                {
	                	bw1.write(SC.nextLine());
	                	bw1.newLine();
	                }
	    
	                SC.close();
	                bw1.close();
	            }
	            System.out.println("----------------------------------------");

	            // Do not feel like reading the response body
	            // Call abort on the request object
	            httpget.abort();
	        } finally {
	            // When HttpClient instance is no longer needed,
	            // shut down the connection manager to ensure
	            // immediate deallocation of all system resources
	            httpclient.getConnectionManager().shutdown();
	        }
	}
	
	
	
	
	
	
	
	public final static void main(String[] args) throws Exception {
		
		LoadPage("http://www.fatwallet.com/forums/hot-deals/electronic/?start=20");
       
    }
}
