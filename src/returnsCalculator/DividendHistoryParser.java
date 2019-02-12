package returnsCalculator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import returnsCalculator.DividendPayment;

/**
 * This class is used to parse a text file describing the history of dividends
 * @author tripd22
 *
 */
public class DividendHistoryParser {
	
	/**
	 * Takes a text file listing a series of dividends, and returns a list
	 * of DividendPayment objects based on this data
	 * @param filename
	 * @return a list of ShareHolding objects
	 */
	List<DividendPayment> parse(String filename) {
		
		List<DividendPayment> dividendPayments = new ArrayList<DividendPayment>();
		
		try
		  {
		    BufferedReader reader = new BufferedReader(new FileReader(filename));
		    String line;
		    int line_counter = 1;
		    
		    // read the header line of the input file
		    line = reader.readLine();
		    
		    // read in each subsequent line - if they are valid, add to list of DividendPayments
		    while ((line = reader.readLine()) != null) {
		      String[] words = line.split("\t");
		      line_counter++;
		      
		      // if the length of data != 4, then the share data in the input file is an invalid format
		      if (words.length != 4) {
		    	  System.out.println("Input data on line " + line_counter + " was incorrectly formatted");
		    	  reader.close();
		    	  return null;
		      }
		      
		      String ticker = words[0];
		      float amount = Float.parseFloat(words[1]);
		      
		      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		      Date exDivDate = sdf.parse(words[2]);
		      
		      float reinvestmentPrice = Float.parseFloat(words[3]);
		      
		      DividendPayment dividendPayment = new DividendPayment(ticker, amount, exDivDate, reinvestmentPrice);
		      
		      dividendPayments.add(dividendPayment);

		    }
		    reader.close();
		  }
		  catch (Exception e)
		  {
		    System.err.format("Exception occurred trying to read '%s'.", filename);
		    return null;
		  }
		
		return dividendPayments;
	}

}
