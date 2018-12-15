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
					// downcasting division - get rid of any decimal value
					int numShares = (int) (sumDividends / dividendPayment.getReinvestmentPrice());
					
					shareHolding.setAmount(shareHolding.getAmount() + numShares);
					sumDividends -= dividendPayment.getReinvestmentPrice() * numShares;
					
					System.out.println("DRP: " + (shareHolding.getAmount()-numShares) + " --> " + shareHolding.getAmount() + " share(s), leaving $" + sumDividends + " on " + dividendPayment.getExDividendDate());
				}
				
			}
		}
		
		float currentValueBasedOnDividends = sumDividends;
		
		// calculate value based on price variation and brokerage
		float currentValueBasedOnPrice = currentPrice * shareHolding.getAmount();
		
		return currentValueBasedOnPrice + currentValueBasedOnDividends;
		
	}

}
