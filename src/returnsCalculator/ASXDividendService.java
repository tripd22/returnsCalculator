package returnsCalculator;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
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
		
		// otherwise, query the asx api
		WebClient client = new WebClient();  
		client.getOptions().setCssEnabled(false);  
		client.getOptions().setJavaScriptEnabled(false);  
		try {  
			String searchUrl = "https://www.asx.com.au/asx/markets/dividends.do?by=asxCodes&asxCodes=" + ticker + "&view=all";
			page = client.getPage(searchUrl);
			List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//table[@class='datatable']");
			if (items.isEmpty()) {
				System.out.println("List was empty");
			} else {
				System.out.println("List has data");
				System.out.println(items.get(0).asText());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
		

	}
}
