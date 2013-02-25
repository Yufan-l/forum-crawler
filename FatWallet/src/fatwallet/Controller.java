package fatwallet;

import java.util.ArrayList;

public class Controller {
	
	public static void main(String args[])throws Exception
	{
		//ParseFirstPage.Loading("http://www.fatwallet.com/forums/hot-deals/electronic/?start="+20);
		
		
		ArrayList<String> previous=ParsePreviousDeals.Loading();
		
		
		
		int num=14;
		for(int i=0;i<num;i++)
		{
			int page=i*20;
			ParseFirstPage.Loading("http://www.fatwallet.com/forums/hot-deals/?start="+Integer.toString(page),previous);
			try {
			    Thread.sleep(4000);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
		}
	
	}
	
	

}
