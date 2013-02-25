package fatwallet;

import java.io.*;

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




public class DownloadText {
	
	public static void Download(String deal_id)throws Exception
	{
		
		Logger log = Logger.getLogger( DownloadText.class);
		BasicConfigurator.configure();
		try{
			
			File file = new File(deal_id);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			final BufferedWriter bw = new BufferedWriter(fw);
			
		 
			Document doc = Jsoup.connect("http://www.fatwallet.com/forums/textthread.php?catid=18&threadid="+deal_id).get();
			bw.write(doc.html().replaceAll("\\<.+?\\>", "").replaceAll("\n\n", "").replace("&nbsp", " ").replace(";", ""));
		
		
		
			bw.close();
		}
  	    catch(Exception e)
		{
			log.error(e);
		}
		
	}
	
	public static void main(String args[])throws Exception
	{
		
		Download("1241936");	

	}
	
	

}