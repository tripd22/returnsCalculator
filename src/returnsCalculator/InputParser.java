package returnsCalculator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import returnsCalculator.ShareHolding;

/**
 * This class is used to parse an input text file describing a shareholder's holdings
 * @author tripd22
 *
 */
public class InputParser {
	
	/**
	 * Takes a text file listing a shareholder's holdings, and returns a list
	 * of ShareHolding objects based on this data
	 * @param filename
	 * @return a list of ShareHolding objects
	 */
	List<ShareHolding> parse(String filename) {
		
		List<ShareHolding> shares = new ArrayList<ShareHolding>();
		
		try
		  {
		    BufferedReader reader = new BufferedReader(new FileReader(filename));
		    String line;
		    int line_counter = 1;
		    
		    // read the header line of the input file
		    line = reader.readLine();
		    
		    // read in each subsequent line - if they are valid, add to list of StockHoldings
		    while ((line = reader.readLine()) != null) {
		      String[] words = line.split("\t");
		      line_counter++;
		      
		      // if the length of data != 5, then the share data in the input file is an invalid format
		      if (words.length != 5) {
		    	  System.out.println("Input data on line " + line_counter + " was incorrectly formatted");
		    	  reader.close();
		    	  return null;
		      }
		      
		      String ticker = words[0];
		      int amount = Integer.parseInt(words[1]);
		      float price = Float.parseFloat(words[2]);
		      float brokerage = Float.parseFloat(words[3]);
		      
		      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		      Date purchaseDate = sdf.parse(words[4]);
		      
		      ShareHolding shareHolding = new ShareHolding(amount, price, ticker, brokerage, purchaseDate);
		      
		      if (!shareHolding.isValid()) {
		    	  System.out.println("Stock Holding data was invalid!");
		    	  reader.close();
		    	  return null;
		      }
		      
		      shares.add(shareHolding);   
		    }
		    reader.close();
		  }
		  catch (Exception e)
		  {
		    System.err.format("Exception occurred trying to read '%s'.", filename);
		    return null;
		  }
		
		return shares;
	}

}
