package returnsCalculator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ASXDividendService {
	
	List<DividendPayment> retrieveDividends(String ticker) {
		String url = " ";
		if (ticker.equals("VAS")) {
			url = "https://www.vanguardinvestments.com.au/retail/ret/investments/product.html#/fundDetail/etf/portId=8205/?prices";
		} else if (ticker.equals("VGS")) {
			url = "https://www.vanguardinvestments.com.au/retail/ret/investments/product.html#/fundDetail/etf/portId=8212/?prices";
		} else if (ticker.equals("VGE")) {
			url = "https://www.vanguardinvestments.com.au/adviser/adv/investments/product.html#/fundDetail/etf/portId=8204/assetCode=equity/?prices";
		}
		
		ChromeOptions chromeOptions = new ChromeOptions();
		//chromeOptions.addArguments("--log-level=3");
		//chromeOptions.addArguments("--silent");
		
		System.setProperty("webdriver.chrome.driver","/home/darcy/Downloads/chromedriver");
		//System.setProperty("webdriver.chrome.args", "--disable-logging");
		System.setProperty("webdriver.chrome.silentOutput", "true");
		Logger logger = Logger.getLogger("");
		logger.setLevel(Level.OFF);
		WebDriver driver = new ChromeDriver(chromeOptions);
		driver.get(url);
		
		if (ticker.equals("VGE")) {
			driver.findElements(By.className("vuiButton")).get(1).click();
		}
		
		List<DividendPayment> dividendPayments = new ArrayList<DividendPayment>();
		
		try {
	        Thread.sleep(10000);   //the page gets loaded completely

	        List<String> pageSource = new ArrayList<String>(Arrays.asList(driver.getPageSource().split("\n")));
	        WebElement data = driver.findElements(By.className("dataTable")).get(1);
	        dividendPayments = DividendTableParser.parseDividendTable(data.getText());
	        
		} catch (InterruptedException e) {
	        e.printStackTrace();
	    } catch (ParseException e) {
			e.printStackTrace();
		}
		
		driver.quit();
		
		return dividendPayments;		

	}
}
