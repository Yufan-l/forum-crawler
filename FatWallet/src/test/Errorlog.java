package test;


import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

public class Errorlog {
	public static void error()
	{
		Logger log = Logger.getLogger( Errorlog.class);
		BasicConfigurator.configure();
		try{
		int ss[]={1,3,4,6};
		int k=ss[7];
		}
		catch(Exception e)
		{
			log.error("eoor",e);
		}
	}
	
	public static void main(String args[])
	{
		
		error();
		System.out.println("kkk");
		
		
	}

}
