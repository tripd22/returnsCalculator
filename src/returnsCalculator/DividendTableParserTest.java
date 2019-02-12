package returnsCalculator;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

/**
 * Test for the DividendTableParser class. Input file in 
 * testInput directory used to generate input String
 * @author tripd22
 *
 */
class DividendTableParserTest {

	@Test
	void testParseDividendTable() {
		
		String filename = "testInputs/dividendTableData.txt";
		
		try {
			File file = new File(filename);
			Scanner scanner = new Scanner(file);
			scanner.useDelimiter("\\Z");
		    String content = scanner.next();
		    scanner.close();
		    
		    List<DividendPayment> dividendPayments;
		    dividendPayments = DividendTableParser.parseDividendTable(content, "VAS");
		    assertEquals(dividendPayments.size(), 10);
		    
		    DividendPayment dp = dividendPayments.get(0);
		    assertTrue(dp.getAmount() > 0.55200803 * 0.99);
		    assertTrue(dp.getAmount() < 0.55200803 * 1.01);
		    assertTrue(dp.getReinvestmentPrice() > 63.4104 * 0.99);
		    assertTrue(dp.getReinvestmentPrice() < 63.4104 * 1.01);
		    
		    dp = dividendPayments.get(6);
		    assertTrue(dp.getAmount() > 0.048857 * 0.99);
		    assertTrue(dp.getAmount() < 0.048857 * 1.01);
		    assertTrue(dp.getReinvestmentPrice() > 58.2360 * 0.99);
		    assertTrue(dp.getReinvestmentPrice() < 58.2360 * 1.01);
		    
		    
		} catch (Exception e) {
			System.err.format("Exception occurred trying to read '%s'.", filename);
		}
		
	}

}
