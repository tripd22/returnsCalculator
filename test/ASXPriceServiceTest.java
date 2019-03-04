

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import returnsCalculator.ASXPriceService;

/**
 * Tests the ASXPriceService class
 * @author tripd22
 *
 */
class ASXPriceServiceTest {

	static ASXPriceService priceService;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		priceService = new ASXPriceService();
	}
	
	@Test
	void testRetrievePrice() {
		Float price = priceService.retrievePrice("VAS");
		assertNotNull(price);
	}
	
	@Test
	void testRetrievePriceInvalidTicker() {
		Float price = priceService.retrievePrice("444");
		assertEquals(price.floatValue(), -1.0f);
	}

}
