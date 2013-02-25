package fatwallet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import java.util.*;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.sql.*;


public class ParseFirstPage {
	
	 public static void Loading(String URL, ArrayList<String> previous) throws Exception {
		Connection connect = null;
		Statement statement = null;
		
		Logger log = Logger.getLogger( ParseFirstPage.class);
		BasicConfigurator.configure();
		try{
		System.out.println("parsing "+URL+" ...");
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection("jdbc:mysql://localhost/FatWallet1?user=yliu0&password=yl1234");
	    statement = connect.createStatement();
	     
		 
		 
		 /*File file1 = new File("C:/output.html");
		 
 		if (!file1.exists()) {
 			file1.createNewFile();
 		}
 		FileWriter fw1 = new FileWriter(file1.getAbsoluteFile());
 		final BufferedWriter bw1 = new BufferedWriter(fw1);*/

	    
	    
		 Document doc = Jsoup.connect(URL).get();
		 //String str=doc.html();	 
		 
		 
		 Elements links = doc.select("td[class=topicInfo]");
		 Elements links2 = doc.select("span[class=rating]");
		 Elements links3 = doc.select("title");
		
		 int count=0;
		 
		 
		 for(int i=0;i<links.size();i+=3) 
		 {
			 String content1[]=links.get(i).html().split(" ");
			 String content2[]=links.get(i+2).html().split("\\/");
			 int replies=Integer.parseInt(links.get(i+1).html());
			 String ID=content2[3];
			 int days=0;
			 
			 
			 if(content1[0].equals("New")&&!previous.contains(ID))
			 {
				 days=0; 
			 //else
			 //	 days=Integer.parseInt(content1[0]);
				 int order=1;
				 int rating=Integer.parseInt(links2.get(count).text().replace("+", ""));
				 int views=Integer.parseInt(content1[content1.length-2]);
				 //System.out.println(ID+"*"+rating+"*"+replies+"*"+views+"*"+days);
				 int result = statement.executeUpdate("INSERT INTO deal_change VALUES("+ID+","+rating+","+views+","+replies+","+days+","+order+", NOW());");
				
				 result = statement.executeUpdate("INSERT INTO deals_queue VALUES("+ID+","+days+","+order+") ON DUPLICATE KEY UPDATE deal_id="+ID+";");
				 
				 
				 ParseDeal.SaveDeal("http://www.fatwallet.com/forums/hot-deals/"+ID);
			 
				 try {
				    Thread.sleep(2000);
				 } catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				 }
			 }
			 count++;
		 }
		 
	
		 
		
		// bw1.close();
		 statement.close();
   	  	 connect.close();
   	     System.out.println("parse successed");
		}
  	    catch(Exception e)
		{
			log.error(e);
		}
		
		
		 
	    }
	 
	 
	   
	 
	 public static void main(String args[])throws Exception
	 {
		 ArrayList<String> test=new ArrayList<String> ();
		 Loading("http://www.fatwallet.com/forums/hot-deals/?start=20",test);	 
	 }

}
