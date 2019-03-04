

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import returnsCalculator.ASXDividendService;
import returnsCalculator.DividendPayment;

/**
 * Tests the ASXDividendService class
 * @author tripd22
 *
 */
class ASXDividendServiceTest {
	
	static ASXDividendService dividendService;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		dividendService = new ASXDividendService();
	}
	
	@Test
	void testRetrieveDividends() {
		List<DividendPayment> dividendPayments = dividendService.retrieveDividends("VAS");
		assertEquals(dividendPayments.size(), 10);
	}
	
	@Test
	void testRetrieveDividendsInvalidTicker() {
		List<DividendPayment> dividendPayments = dividendService.retrieveDividends("444");
		assertNull(dividendPayments);
	}

}
