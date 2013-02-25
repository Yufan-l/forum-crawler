package fatwallet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;


public class ParseRating {
	
	public static void PR(String URL,String deal_id)throws Exception
	{
		Connection connect = null;
		Statement statement = null;
		
		Logger log = Logger.getLogger( ParseRating.class);
		BasicConfigurator.configure();
		try{
		
		Class.forName("com.mysql.jdbc.Driver");
	    connect = DriverManager.getConnection("jdbc:mysql://localhost/FatWallet1?user=yliu0&password=yl1234");
	    statement = connect.createStatement();
	     
		String urlContent[]=URL.split("\\?");
		String msgID=urlContent[1].replace("messageid=", "");
		Document doc = Jsoup.connect(URL).get();
		Elements ratingLinks = doc.select("td[class=normal]");
		for(int i=0;i<ratingLinks.size();i++)
		{
			if(i<4)
				continue;
			if(ratingLinks.get(i).html().length()==0)
				continue;
				
			String content[]=ratingLinks.get(i).html().split(" ");
			String reg_date=content[5]+content[6].replace("\">", "");
			String name=content[14].replace("<br", "");
			//System.out.println(name+"*"+reg_date);
			PreparedStatement pstmt1 = connect.prepareStatement("INSERT INTO users (user_name,registered_date) VALUES (?,?) ON DUPLICATE KEY UPDATE registered_date=?");
			pstmt1.setString(1, name);
			pstmt1.setString(2, reg_date);
			pstmt1.setString(3, reg_date);
			pstmt1.executeUpdate();
			if(i%2==0)
			{
				PreparedStatement pstmt2 = connect.prepareStatement("INSERT INTO rating(deal_id,message_id,rating_type,rating_user,record_date) VALUES (?,?,?,?,NOW()) ON DUPLICATE KEY UPDATE rating_user=?");
				pstmt2.setString(1, deal_id);
				pstmt2.setString(2, msgID);
				pstmt2.setString(3, "positive");
				pstmt2.setString(4, name);
				pstmt2.setString(5, name);
				pstmt2.executeUpdate();
				
			}
			else
			{
				PreparedStatement pstmt2 = connect.prepareStatement("INSERT INTO rating(deal_id,message_id,rating_type,rating_user,record_date) VALUES (?,?,?,?,Now()) ON DUPLICATE KEY UPDATE rating_user=?");
				pstmt2.setString(1, deal_id);
				pstmt2.setString(2, msgID);
				pstmt2.setString(3, "negative");
				pstmt2.setString(4, name);
				pstmt2.setString(5, name);
				pstmt2.executeUpdate();
				
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
		
		PR("http://www.fatwallet.com/forums/viewvotes.php?messageid=17329394","1234564");
		
	}

}
