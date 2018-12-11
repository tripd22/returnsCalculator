package returnsCalculator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import returnsCalculator.ShareHolding;

public class InputParser {
	
	List<ShareHolding> parse(String filename) {
		
		List<ShareHolding> shares = new ArrayList<ShareHolding>();
		
		try
		  {
		    BufferedReader reader = new BufferedReader(new FileReader(filename));
		    String line;
		    int line_counter = 1;
		    
		    // read the header line of the input file
		    line = reader.readLine();
		    
		    // read in each subsequent line. If they are valid, add to list of StockHoldings
		    while ((line = reader.readLine()) != null)
		    {
		      String[] data = line.split("\t");
		      line_counter++;
		      
		      // if the length of data != 4, then the share data in the input file is an invalid format
		      if (data.length != 4) {
		    	  System.out.println("Input data on line " + line_counter + " was incorrectly formatted");
		    	  return null;
		      }
		      
		      String ticker = data[0];
		      int amount = Integer.parseInt(data[1]);
		      float price = Float.parseFloat(data[2]);
		      float brokerage = Float.parseFloat(data[3]);
		      ShareHolding s = new ShareHolding(amount, price, ticker, brokerage);
		      
		      if (!s.isValid()) {
		    	  System.out.println("Stock Holding data was invalid!");
		    	  return null;
		      }
		      
		      shares.add(s);
		      
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
