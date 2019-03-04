package returnsCalculator;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * This class is used to retrieve the distributions of a Vanguard ETF.
 * It currently supports VAS, VGS and VGE ETFs
 * @author tripd22
 *
 */
public class ASXDividendService {
	
	/**
	 * Returns a list of distributions for a given ticker
	 * @param ticker
	 * @return A list of distributions
	 */
	public List<DividendPayment> retrieveDividends(String ticker) {
		String url = " ";
		if (ticker.equals("VAS")) {
			url = "https://www.vanguardinvestments.com.au/retail/ret/investments/product.html#/fundDetail/etf/portId=8205/?prices";
		} else if (ticker.equals("VGS")) {
			url = "https://www.vanguardinvestments.com.au/retail/ret/investments/product.html#/fundDetail/etf/portId=8212/?prices";
		} else if (ticker.equals("VGE")) {
			url = "https://www.vanguardinvestments.com.au/adviser/adv/investments/product.html#/fundDetail/etf/portId=8204/assetCode=equity/?prices";
		}
		
		if (url.equals(" ")) {
			System.out.println("That ticker is not supported");
			return null;
		}
		
		// set up selenium to read distribution data
		// simple html parsing not usable because of the way the Vanguard website loads
		ChromeOptions chromeOptions = new ChromeOptions();		
		System.setProperty("webdriver.chrome.driver","/home/darcy/Downloads/chromedriver");
		System.setProperty("webdriver.chrome.silentOutput", "true");
		Logger logger = Logger.getLogger("");
		logger.setLevel(Level.OFF);
		WebDriver driver = new ChromeDriver(chromeOptions);
		driver.get(url);
		
		// if ticker == VGE, first must click "I am outside the U.S. button on the Vanguard webpage"
		if (ticker.equals("VGE")) {
			driver.findElements(By.className("vuiButton")).get(1).click();
			try {
				// need to sleep to allow the page to load after the click
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		List<DividendPayment> dividendPayments = new ArrayList<DividendPayment>();
		
		try {
	        Thread.sleep(1000);   // the page gets loaded completely
	        WebElement data = driver.findElements(By.className("dataTable")).get(1);
	        
	        // call DividendTableParser on the data found
	        dividendPayments = DividendTableParser.parseDividendTable(data.getText(), ticker);
	        
		} catch (InterruptedException e) {
	        e.printStackTrace();
	    } catch (ParseException e) {
			e.printStackTrace();
		}
		
		driver.quit();
		
		return dividendPayments;		

	}
}
