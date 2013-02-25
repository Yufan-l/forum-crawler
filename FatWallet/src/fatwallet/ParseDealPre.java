package fatwallet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;




public class ParseDealPre {
	
	public static void SaveDeal(String URL)throws Exception
	{
		
		Connection connect = null;
		Statement statement = null;
		
		Logger log = Logger.getLogger( ParseDealPre.class);
		BasicConfigurator.configure();
		try{
			
		Class.forName("com.mysql.jdbc.Driver");
	    connect = DriverManager.getConnection("jdbc:mysql://localhost/FatWallet1?user=yliu0&password=yl1234");
	    statement = connect.createStatement();
	     
		 
		Document doc = Jsoup.connect(URL).get();
	
		Elements dateLinks= doc.select("div[class=post_date]");
		System.out.println(dateLinks.get(0).html());
		String content[]=dateLinks.get(0).html().split(" ");
		
		int len=content.length;

		String content2[]=URL.split("\\/");
		String deal_id=content2[content2.length-1];
			
		//String deal_id=content2[];
		Elements msgLinks= doc.select("meta[name=description]");
		
				
		Elements nameLinks = doc.select("li[class=user_name]");

		
	    Elements replyLinks = doc.select("div[class=userMsg]");

		ArrayList<String> post_dateList=new ArrayList<String>();
	    for(Element E1:dateLinks)
	    {
	    	if(E1.html().contains("rel")||E1.html().contains("updated:"))
	    		continue;
	    	post_dateList.add(E1.html());
	    	
	    }
	  
	    
		for(int i=0;i<post_dateList.size();i++)
		{
			if(i==0||i==1)
				continue;
			String name=nameLinks.get(i).html().replaceAll("\\<.+?\\>", "");
			String content3[]=post_dateList.get(i).split(" "); 
			len=content3.length;
			String reply_date=content3[len-5]+" "+content3[len-4]+" "+content3[len-3]+" "+content3[len-2]+" "+content3[len-1];
			String reply_msg=replyLinks.get(i).html().replaceAll("\\<.+?\\>", "").replaceAll("\n", "").replace("&nbsp", " ");
			/*if(reply_msg.length()>3000)
			{
				System.out.println(reply_msg.length());
			}*/
			
			PreparedStatement pstmt2 = connect.prepareStatement("INSERT INTO deal_replies (deal_id,description,reply_user,reply_date) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE reply_date=?");
			pstmt2.setString(1, deal_id);
			pstmt2.setString(2, reply_msg);
			pstmt2.setString(3, name);
			pstmt2.setString(4, reply_date);
			pstmt2.setString(5, reply_date);
			pstmt2.executeUpdate();
		
		}
			
		
		Elements ratingLinks = doc.select("a[name*=m]");
		String ratingID=ratingLinks.get(0).attr("name").replace("m", "");
		String ratingURL="http://www.fatwallet.com/forums/viewvotes.php?messageid="+ratingID;
		
		
		ParseRating.PR(ratingURL, deal_id);
		
  	    statement.close();
  	    connect.close();
		}
  	    catch(Exception e)
		{
			log.error(e);
		}
		
	}
	
	public static void main(String args[])throws Exception
	{
		
		SaveDeal("http://www.fatwallet.com/forums/hot-deals/1253212/");	
		//SaveDeal("http://www.fatwallet.com/forums/hot-deals/1254570/");	
	
		
		
	}
	
	

}