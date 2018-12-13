package returnsCalculator;

import java.util.Collections;
import java.util.List;

/**
 * The ReturnCalculator class returns the value of a share holding based
 * on it's dividend payments and current price
 */
public class CurrentValueCalculator {
	
	public static float calculateCurrentValue(ShareHolding shareHolding, List<DividendPayment> dividendPayments, float currentPrice) {
		
		// calculate return based on dividends
		float sumDividends = 0.0f;
		
		Collections.sort(dividendPayments);
		
		for (DividendPayment dividendPayment : dividendPayments) {
			if (dividendPayment.getExDividendDate().after(shareHolding.getPurchaseDate())) {
				sumDividends += dividendPayment.getAmount() * shareHolding.getAmount();
				if (sumDividends > dividendPayment.getReinvestmentPrice()) {
					// NOTE - THIS ONLY ALLOWS BUYING ONE SHARE EACH DIVIDEND - MAY NEED TO BUY MORE
					System.out.println("DRP buys one share at " + dividendPayment.getReinvestmentPrice() + " on " + dividendPayment.getExDividendDate());
					shareHolding.setAmount(shareHolding.getAmount() + 1);
					sumDividends -= dividendPayment.getReinvestmentPrice();
				}
				
			}
		}
		
		float currentValueBasedOnDividends = sumDividends;
		
		// calculate value based on price variation and brokerage
		float currentValueBasedOnPrice = currentPrice * shareHolding.getAmount();
		
		return currentValueBasedOnPrice + currentValueBasedOnDividends;
		
	}

}
