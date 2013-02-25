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


public class ParsePreviousDeals {
	
	 public static ArrayList<String> Loading() throws Exception {
		Connection connect = null;
		Statement statement = null;
		
		Logger log = Logger.getLogger( ParsePreviousDeals.class);
		BasicConfigurator.configure();
		ArrayList<String> deal_list=new ArrayList<String>();
 		HashMap<String,Integer> order_list=new HashMap<String, Integer>();
 		
		try{
		System.out.println("parsing previous deals...");
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection("jdbc:mysql://localhost/FatWallet1?user=yliu0&password=yl1234");
	    statement = connect.createStatement();

	    
	    ResultSet rs = statement.executeQuery("select deal_id, days,record_order from deals_queue where record_order<42 ");
 		
 		
	    while(rs.next())
	    {
	    	String deal_id=rs.getString("deal_id");
	    	int record_order=rs.getInt("record_order");
	    
	    	ParseDealPre.SaveDeal("http://www.fatwallet.com/forums/hot-deals/"+deal_id);
	    	
	    	if(record_order==41)
	    		DownloadText.Download(deal_id);
	    		
	    	deal_list.add(deal_id);
	    	order_list.put(deal_id,record_order);
	   	 	try {
			    Thread.sleep(2000);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
	   	 	
	   	 	
	   	 	
	    	
	    }
	    System.out.println(deal_list.size()+" previous deals need to be parsed");
	    
	    
	    for(int i=0;i<120;i++)
	    {
	    	if (deal_list.isEmpty())
	    		break;
	    	int page=i*20;
	    	Document doc = Jsoup.connect("http://www.fatwallet.com/forums/hot-deals/?start="+Integer.toString(page)).get();
	    	 Elements links = doc.select("td[class=topicInfo]");
			 Elements links2 = doc.select("span[class=rating]");
	

			 int count=0;

			 for(int j=0;j<links.size();j+=3) 
			 {
				 
				 String content2[]=links.get(j+2).html().split("\\/");
				 
				 String ID=content2[3];
				
				 
					 //System.out.println(ID+"*"+rating+"*"+replies+"*"+views+"*"+days);
					 
				 if(deal_list.contains(ID))	
				 {
					 String content1[]=links.get(j).html().split(" ");
					 int replies=Integer.parseInt(links.get(j+1).html());
					 int days=0;
					 if(content1[0].equals("New"))
						 days=0; 
					 else
					 	 days=Integer.parseInt(content1[0]);
					 
					 int rating=Integer.parseInt(links2.get(count).text().replace("+", ""));
					 int views=Integer.parseInt(content1[content1.length-2]);
					 int record_order=order_list.get(ID)+1;
					 
					 int result = statement.executeUpdate("INSERT INTO deal_change VALUES("+ID+","+rating+","+views+","+replies+","+days+","+record_order+",NOW());");
					 
					 result = statement.executeUpdate("UPDATE deals_queue SET record_order="+record_order+", days="+days+" WHERE deal_id="+ID+";");
					 
					 deal_list.remove(ID);
				 }
				 count++;
			 }
			 try {
				    Thread.sleep(2000);
				 } catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				 }
	    	
			
	    }
		
	    
		 
		 
	
		 
		 rs.close();
		 statement.close();
   	  	 connect.close();
   	     System.out.println("parse successed");
   	    
   
		}
  	    catch(Exception e)
		{
			log.error(e);
		}
		return deal_list;
		
}
	 
	 
	   
	 
	 public static void main(String args[])throws Exception
	 {
		 Loading();	 
	 }

}
