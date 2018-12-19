package returnsCalculator;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

/**
 * Tests the CurrentValueCalculator class with sample dividend and price data
 * @author tripd22
 *
 */
class CurrentValueCalculatorTest {

	@Test
	void testCalculateCurrentValue() throws ParseException, FileNotFoundException {
		// import share data
		String shareDataFilename = "testInputs/validShareDataVAS.txt";
		InputParser parser = new InputParser();
		List<ShareHolding> originalShares = parser.parse(shareDataFilename);
		
		// import dividend data
		String dividendDataFilename = "testInputs/dividendTableDataVAS.txt";
		File dividendDataFile = new File(dividendDataFilename);
		Scanner scanner = new Scanner(dividendDataFile);
		scanner.useDelimiter("\\Z");
	    String content = scanner.next();
	    scanner.close();
	    
	    List<DividendPayment> dividendPayments;
	    dividendPayments = DividendTableParser.parseDividendTable(content);
	    
	    // calculate value based on sample price
	    float currentPrice = 71.52f;
		ShareHoldingValue shv = CurrentValueCalculator.calculateCurrentValue(originalShares, dividendPayments, currentPrice);
		
		assertEquals(shv.getNumberOfDRPShares(), 14);
		assertEquals(shv.getNumberOfPurchasedShares(), 200);
		assert(shv.getValueBasedOnPrice() > 15305.279 * 0.99);
		assert(shv.getValueBasedOnPrice() < 15305.279 * 1.01);
		assert(shv.getValueBasedOnAccumulatedDividends() > 23.925568 * 0.99);
		assert(shv.getValueBasedOnAccumulatedDividends() < 23.925568 * 1.01);
		
		}
}
