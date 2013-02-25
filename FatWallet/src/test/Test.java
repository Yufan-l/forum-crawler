package test;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;

import org.jsoup.*;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;



public class Test {
	
	 public final static void main(String[] args) throws Exception {
		 File file1 = new File("C:/output.html");
 		if (!file1.exists()) {
 			file1.createNewFile();
 		}
 		FileWriter fw1 = new FileWriter(file1.getAbsoluteFile());
 		final BufferedWriter bw1 = new BufferedWriter(fw1);
 		
 		HashSet<String> hs=new HashSet<String>();
		Document doc = Jsoup.connect("http://www.fatwallet.com/forums/hot-deals/electronic/").get();
		Document doc2 = Jsoup.connect("http://www.fatwallet.com/forums/hot-deals/electronic/?start=20").get();
		Elements links = doc.select("a[href*=/forums/hot-deals/]");
		Elements links2 = doc2.select("a[href*=/forums/hot-deals/]");
		int count=0;
		for(Element E1: links)
		{
			
			if(E1.id().contains("title"))
			{
				hs.add(E1.id());
				count++;
			}
			 
		}
		
		
		for(Element E1: links2)
		{
			
			if(E1.id().contains("title"))
			{
				System.out.println(E1.id());
				if (hs.contains(E1.id()))
				{
					//System.out.println(E1.id());
				}
			}
			 
		}
		 
		 System.out.println(count);
		 //bw1.write(str);
		 bw1.close();
		 
		 
		 
		 
	    }

}
