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




public class ParseDeal {
	
	public static void SaveDeal(String URL)throws Exception
	{
		
		Connection connect = null;
		Statement statement = null;
		
		Logger log = Logger.getLogger( ParseDeal.class);
		BasicConfigurator.configure();
		try{
			
		Class.forName("com.mysql.jdbc.Driver");
	    connect = DriverManager.getConnection("jdbc:mysql://localhost/FatWallet1?user=yliu0&password=yl1234");
	    statement = connect.createStatement();
	     
		 
		Document doc = Jsoup.connect(URL).get();
		
		Elements dateLinks= doc.select("div[class=post_date]");
		Elements dealLinks= doc.select("title");
		
		String deal_name=dealLinks.get(0).html();
		String content[]=dateLinks.get(0).html().split(" ");
		int len=content.length;
		String date=content[len-5]+" "+content[len-4]+" "+content[len-3]+" "+content[len-2]+" "+content[len-1];

		String content2[]=URL.split("\\/");
		String deal_id=content2[content2.length-1];
			
		//String deal_id=content2[];
		Elements msgLinks= doc.select("meta[name=description]");
		String msg=msgLinks.get(0).attr("content");
	
		Elements nameLinks = doc.select("li[class=user_name]");
		
		String publisher=nameLinks.get(0).html().replaceAll("\\<.+?\\>", "");
		
		Elements cateLinks= doc.select("a");
		String other_category="";
		String deal_category="";
		ArrayList <String> categories=new ArrayList<String>();
		for(Element E1:cateLinks)
		{
			String linkHref = E1.text();
			
			if(linkHref.contains("Deals"))
			{
				if(!linkHref.contains("Product")&&!linkHref.contains("/")&&!linkHref.contains("Hot")&&!linkHref.contains("Expired"))
				{
					categories.add(linkHref.replace(" Deals", ""));
				}
			}
		}
		//System.out.println(other_category);
		if(categories.size()>0)
		{
			deal_category=categories.get(0);
			categories.remove(0);
			for(String s1:categories)
				other_category+=s1+";";
		}
		
		PreparedStatement pstmt = connect.prepareStatement("INSERT INTO deals (deal_id,publisher_name,deal_name,deal_category,deal_other_category,post_date,description) VALUES (?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE description=?");
		pstmt.setString(1, deal_id);
		pstmt.setString(2, publisher);
		pstmt.setString(3, deal_name);
		pstmt.setString(4, deal_category);
		pstmt.setString(5, other_category);
		pstmt.setString(6, date);
		pstmt.setString(7, msg);
		pstmt.setString(8, msg);
		pstmt.executeUpdate();
		
		
		
		
		
		
		
		
		Elements titleLinks = doc.select("li[class=user_title]");
		Elements initLinks = doc.select("img[title*=Posts]");

		for(int i=0;i<nameLinks.size();i++)
		{
			if(i==1)
				continue;
			String name=nameLinks.get(i).html().replaceAll("\\<.+?\\>", "");
			String title=titleLinks.get(i).html().replaceAll("\\<.+?\\>", "");
			String content1[]=initLinks.get(i*2).attr("title").split(" ");
			String reg_date=content1[4]+content1[5];
			//System.out.println(name+"*"+title+"*"+date);
			
			PreparedStatement pstmt1 = connect.prepareStatement("INSERT INTO users (user_name,user_title,registered_date) VALUES (?,?,?) ON DUPLICATE KEY UPDATE user_title=?");
			pstmt1.setString(1, name);
			pstmt1.setString(2, title);
			pstmt1.setString(3, reg_date);
			pstmt1.setString(4, title);
			pstmt1.executeUpdate();
			//int result = statement.executeUpdate("INSERT INTO users(user_name,user_title,registered_date)"+ "VALUES('Butcherboy','Never Pay Retail','Jan2003')ON DUPLICATE KEY UPDATE user_title='mytitle' ");
			
		}
		
		
		
		
	    Elements replyLinks = doc.select("div[class=userMsg]");
		int count=0;
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
			e.printStackTrace();
		}
		
	}
	
	public static void main(String args[])throws Exception
	{
		
		SaveDeal("http://www.fatwallet.com/forums/hot-deals/1234564/");	
	}
	
	

}