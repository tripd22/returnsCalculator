package returnsCalculator;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ASXDividendService {
	
	List<DividendPayment> retrieveDividends(String ticker) {
		
		HtmlPage page;
		List<DividendPayment> dividendPayments = new ArrayList<DividendPayment>();
		
		// set up webclient
		WebClient client = new WebClient();  
		client.getOptions().setCssEnabled(false);  
		client.getOptions().setJavaScriptEnabled(false);
		
		try {
			// query the asx dividends page
			String searchUrl = "https://www.asx.com.au/asx/markets/dividends.do?by=asxCodes&asxCodes=" + ticker + "&view=all";
			page = client.getPage(searchUrl);
			List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//table[@class='datatable']");
			
			if (items.isEmpty()) {
				System.out.println("Data not found");
			} else {
				String table = items.get(0).asText();
				String[] l = table.split("\\s+");
				
				// iterate through the table String, and find where the dividend amount is stated
				for (int i = 0; i < l.length; i++) {
					String word = l[i];
					if (word.matches("^[0-9.]+c$")) { // i.e. matches a decimal number followed by the character 'c' (cents)
						// turn div amount into float, convert cents to dollars
						String amt = word.substring(0, word.length()-1);
						Float amount = Float.valueOf(amt) / 100;
						
						// get ex div date
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						Date exDivDate = sdf.parse(l[i+1]);
						
						// create new DividendPayment object
						DividendPayment d = new DividendPayment(amount, exDivDate);
						dividendPayments.add(d);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return dividendPayments;
		

	}
}
