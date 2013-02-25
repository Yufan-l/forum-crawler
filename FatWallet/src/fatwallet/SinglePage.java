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




public class SinglePage {
	
	public static void SaveDeal(String URL)throws Exception
	{
		
		Connection connect = null;
		Statement statement = null;
		
		Logger log = Logger.getLogger( SinglePage.class);
		BasicConfigurator.configure();
		try{
			
		Class.forName("com.mysql.jdbc.Driver");
	    connect = DriverManager.getConnection("jdbc:mysql://localhost/FatWallet1?user=yliu0&password=yl1234");
	    statement = connect.createStatement();
	     
		 
		Document doc = Jsoup.connect(URL).get();
	
		Elements cateLinks= doc.select("a");
		
		for(Element E1:cateLinks)
		{
			String linkHref = E1.text();
			
			if(linkHref.contains("Deals"))
			{
				if(!linkHref.contains("Product")&&!linkHref.contains("/")&&!linkHref.contains("Hot")&&!linkHref.contains("Expired"))
				{
					System.out.println(linkHref);
				}
			}
		}
		
				
		


		
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
		
		SaveDeal("http://www.fatwallet.com/forums/hot-deals/1234564/");	
	}
	
	

}