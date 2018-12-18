package returnsCalculator;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;

/**
 * Tests the ShareHolding class
 * @author tripd22
 *
 */
class ShareHoldingTest {

	@Test
	void testIsValid() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = sdf.parse("12/12/2018");
		
		ShareHolding shareHolding1 = new ShareHolding(0, 55.0f, "VAS", 20.00f, date);
		assertFalse(shareHolding1.isValid());
		
		ShareHolding shareHolding2 = new ShareHolding(3,-55.0f, "VAS", 20.00f, date);
		assertFalse(shareHolding2.isValid());
		
		ShareHolding shareHolding3 = new ShareHolding(3, 55.0f, "V4S", 20.00f, date);
		assertFalse(shareHolding3.isValid());
		
		ShareHolding shareHolding4 = new ShareHolding(5, 55.0f, "VAS", -20.00f, date);
		assertFalse(shareHolding4.isValid());
		
		Date futureDate = sdf.parse("12/12/2228");
		
		ShareHolding shareHolding5 = new ShareHolding(5, 55.0f, "VAS", 20.00f, futureDate);
		assertFalse(shareHolding5.isValid());
		
		ShareHolding shareHolding6 = new ShareHolding(5, 55.0f, "VAS", 20.00f, date);
		assertTrue(shareHolding6.isValid());
	}

}
