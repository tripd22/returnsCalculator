package returnsCalculator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class parses the dividend table text scraped from the Vanguard 
 * web site, and returns a list of DividendPayment objects
 * @author tripd22
 */
public class DividendTableParser {
	
	static List<DividendPayment> parseDividendTable (String input) throws ParseException {
		List<DividendPayment> dividendPayments = new ArrayList<DividendPayment>();
		
		String[] lines = input.split("\n");
		for (int i = 5; i < 20; i++) {
			String line = lines[i];
			String [] words = line.split("\\s+");
			// if first line, skip
			if (words[0].equals("Distribution")) {
				continue;
			}
			// finished reading all dividends, break
			if (words[1].equals("items")) {
				break;
			}
			
			// get dividend amount
			Float amount = Float.valueOf(words[3]) / 100;
			
			// get ex dividend date
			String exDivDate = words[4] + " " + words[5] + " " + words[6];
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
			Date exDividendDate = sdf.parse(exDivDate);
			
			// get reinvestment price
			String reinvestmentPriceString = words[13];
			Float reinvestmentPrice = Float.parseFloat(reinvestmentPriceString.substring(1, reinvestmentPriceString.length()));
			
			DividendPayment dividendPayment = new DividendPayment(amount, exDividendDate, reinvestmentPrice);
			
			dividendPayments.add(dividendPayment);
			
		}
		
		return dividendPayments;
		
	}

}
