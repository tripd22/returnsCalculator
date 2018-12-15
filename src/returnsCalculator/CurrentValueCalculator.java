package returnsCalculator;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * The ReturnCalculator class returns the value of a share holding based
 * on it's dividend payments and current price
 */
public class CurrentValueCalculator {
	
	public static ShareHoldingValue calculateCurrentValue(List<ShareHolding> shareHoldings, List<DividendPayment> dividendPayments, float currentPrice) throws ParseException {
		
		
		ShareHolding overallShareHolding = new ShareHolding();
		overallShareHolding.setAmount(0);
		overallShareHolding.setBrokerage(0.0f);
		overallShareHolding.setPrice(0.0f);
		overallShareHolding.setPurchaseDate(null);
		overallShareHolding.setTicker(shareHoldings.get(0).getTicker());
		
		// calculate return based on dividends
		float sumDividends = 0.0f;
		
		// add bookend value, and then sort collections based on dates
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		Date futureDate = sdf.parse("01 Jan 9999");
		DividendPayment dp = new DividendPayment(0, futureDate, 0);
		ShareHolding sh = new ShareHolding(0, 0.0f, "", 0.0f, futureDate);
		
		dividendPayments.add(dp);
		shareHoldings.add(sh);
		
		Collections.sort(dividendPayments);
		Collections.sort(shareHoldings);
		
		int shareHoldingIndex = 1;
		int dividendPaymentIndex = 1;
		
		ShareHolding currentShareHolding = shareHoldings.get(0);
		DividendPayment currentDividendPayment = dividendPayments.get(0);
		
		while (!(currentDividendPayment.getExDividendDate().equals(futureDate) && currentShareHolding.getPurchaseDate().equals(futureDate))) {
			if (currentShareHolding.getPurchaseDate().before(currentDividendPayment.getExDividendDate())) {
				overallShareHolding.setAmount(overallShareHolding.getAmount() + currentShareHolding.getAmount());
				overallShareHolding.setBrokerage(overallShareHolding.getBrokerage() + currentShareHolding.getBrokerage());
				float effectivePurchasePrice = (overallShareHolding.getPrice()*overallShareHolding.getAmount() + currentShareHolding.getPrice()*currentShareHolding.getAmount()) / (overallShareHolding.getAmount() + currentShareHolding.getAmount());
				overallShareHolding.setPrice(effectivePurchasePrice);
				
				//System.out.println("Share Purchase: " + (overallShareHolding.getAmount()-currentShareHolding.getAmount()) + " --> " + overallShareHolding.getAmount() + " share(s)");
				
				// increment the shareHolding counter
				currentShareHolding = shareHoldings.get(shareHoldingIndex);
				shareHoldingIndex++;
			} else {
				sumDividends += currentDividendPayment.getAmount() * overallShareHolding.getAmount();
				if (sumDividends > currentDividendPayment.getReinvestmentPrice()) {
					// downcasting division - get rid of any decimal value
					int numShares = (int) (sumDividends / currentDividendPayment.getReinvestmentPrice());
					
					overallShareHolding.setAmount(overallShareHolding.getAmount() + numShares);
					sumDividends -= currentDividendPayment.getReinvestmentPrice() * numShares;
					
					//System.out.println("DRP: " + (overallShareHolding.getAmount()-numShares) + " --> " + overallShareHolding.getAmount() + " share(s), leaving $" + sumDividends + " on " + currentDividendPayment.getExDividendDate());
				}
				currentDividendPayment = dividendPayments.get(dividendPaymentIndex);
				dividendPaymentIndex++;
			}
		}
		
		
		float currentValueBasedOnDividends = sumDividends;
		
		// calculate value based on price variation and brokerage
		float currentValueBasedOnPrice = currentPrice * overallShareHolding.getAmount();
		
		ShareHoldingValue shv = new ShareHoldingValue();
		shv.setValueBasedOnAccumulatedDividends(currentValueBasedOnDividends);
		shv.setValueBasedOnPrice(currentValueBasedOnPrice);
		
		return shv;
		
	}

}
