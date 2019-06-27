package returnsCalculator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import returnsCalculator.Transaction;

/**
 * This class is used to parse an input text file describing a shareholder's holdings
 * @author tripd22
 *
 */
public class ETFHoldingParser {
	
	/**
	 * Takes a text file listing a shareholder's transactions, and returns a list
	 * of Transaction objects based on this data
	 * @param filename
	 * @return a list of ShareHolding objects
	 */
	static public Map<String, Set<Transaction>> parse(String filename) {
		
		Map<String, Set<Transaction>> transactions = new HashMap<>();
		
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
		      
		      Transaction transaction = new Transaction(amount, price, ticker, brokerage, purchaseDate);
		      
		      if (!transaction.isValid()) {
		    	  System.out.println("Transaction data was invalid!");
		    	  reader.close();
		    	  return null;
		      }
		      
		      if (!transactions.containsKey(ticker)) {
		    	  transactions.put(ticker, new HashSet<Transaction>());
		      }
		      
		      transactions.get(ticker).add(transaction);
		    }
		    reader.close();
		  }
		  catch (Exception e)
		  {
		    System.err.format("Exception occurred trying to read '%s'.", filename);
		    return null;
		  }
		
		return transactions;
	}

}
