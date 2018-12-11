/**
 * 
 */
package returnsCalculator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.Rule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

/**
 * @author darcy
 *
 */
class InputParserTest {
	
	static InputParser parser;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		parser = new InputParser();
	}
	
	/**
	* Tests whether a valid input error is correctly processed
	*/
	@Test
	void testValidInput() {
		List<ShareHolding> shares = parser.parse("exampleInputs/validShareData.txt");
		
		ShareHolding share = shares.get(0);
		assertTrue(share.getTicker().equals("VAS"));
		assertTrue(share.getAmount() == 12);
		assertTrue(share.getPrice() < 66.83 * 1.01 && share.getPrice() > 66.83 * 0.99);
		assertTrue(share.getBrokerage() < 20.00 * 1.01 && share.getBrokerage() > 20.00 * 0.99);
		
		share = shares.get(1);
		assertTrue(share.getTicker().equals("VGE"));
		assertTrue(share.getAmount() == 14);
		assertTrue(share.getPrice() < 55.52 * 1.01 && share.getPrice() > 55.52 * 0.99);
		assertTrue(share.getBrokerage() < 20.00 * 1.01 && share.getBrokerage() > 20.00 * 0.99);
		
		share = shares.get(2);
		assertTrue(share.getTicker().equals("VAS"));
		assertTrue(share.getAmount() == 11);
		assertTrue(share.getPrice() < 68.23 * 1.01 && share.getPrice() > 68.23 * 0.99);
		assertTrue(share.getBrokerage() < 20.00 * 1.01 && share.getBrokerage() > 20.00 * 0.99);
		
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	/**
	* Tests whether non-existent filename throws an exception as expected
	*/
	@Test
	void testIncorrectFileName() {
		List<ShareHolding> shares = parser.parse("ThisFileDoesNotExist.txt");
		thrown.expect(Exception.class);
		assertTrue(shares == null);
	}
	
	/**
	* Tests whether providing incorrect data in input file is caught
	*/
	@Test
	void testInvalidDataNotEnoughDataProvided() {
		List<ShareHolding> shares = parser.parse("exampleInputs/invalidShareData1.txt");
		thrown.expect(Exception.class);
		assertTrue(shares == null);
	}
	
	/**
	* Tests whether an incorrect data type in the input file is caught
	*/
	@Test
	void testInvalidDataIncorrectAmountDataType() {
		List<ShareHolding> shares = parser.parse("exampleInputs/invalidShareData2.txt");
		thrown.expect(Exception.class);
		assertTrue(shares == null);
	}
	
	/**
	* Tests whether invalid stock ticker (non-alphabetic ticker) is caught
	*/
	@Test
	void testInvalidDataNonAlphabeticTicker() {
		List<ShareHolding> shares = parser.parse("exampleInputs/invalidShareData3.txt");
		assertTrue(shares == null);
	}

}
