package returnsCalculator;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * This class is used to retrieve the current price of a share
 * on the ASX. It uses a HashMap to store these prices, so if
 * it is asked to find the same price twice, it doesn't need to
 * use the ASX API twice (redundantly)
 * @author tripd22
 *
 */
public class ASXPriceService {
	
	HashMap<String, Float> retrievedPrices;
	
	/**
	 * Constructor for making the ASXPriceService - instantiate the HashMap
	 */
	public ASXPriceService() {
		retrievedPrices = new HashMap<String, Float>();
	}
	
	/**
	 * Returns the current price of a stock on the ASX given its ticker
	 * @param ticker
	 * @return the current price of the stock
	 */
	Float retrievePrice(String ticker) {
		// first check hash map - might have already looked up this stock's current price
		Float p = retrievedPrices.get(ticker);
		if (p != null) {
			return p;
		}
		
		// otherwise, query the asx api
		try {
			URLConnection connection = new URL("https://www.asx.com.au/asx/1/share/" + ticker + "/").openConnection();
			Scanner scanner = new Scanner(connection.getInputStream());
		    String response = scanner.useDelimiter("\\A").next();
		    
		    // use Gson object to parse the response
		    JsonObject convertedObject = null;
		    convertedObject = new Gson().fromJson(response, JsonObject.class);
		    String price = convertedObject.get("last_price").getAsString();
		    
		    scanner.close();
		    retrievedPrices.put(ticker, Float.parseFloat(price));
		    return Float.parseFloat(price);
			
		} catch (IOException e) {
			// if API call fails (i.e. stock doesn't exist on ASX)
			System.out.println("API call failed");
			return -1.0f;
		}
	}
	
}
