package returnsCalculator;

import java.util.List;

/**
 * The ReturnCalculator class returns the value of a share holding based
 * on it's dividend payments and current price
 */
public class CurrentValueCalculator {
	
	public static float calculateCurrentValue(ShareHolding shareHolding, List<DividendPayment> dividendPayments, float currentPrice) {
		// calculate return based on price variation and brokerage
		float currentValueBasedOnPrice = currentPrice * shareHolding.getAmount();
		
		// calculate return based on dividends
		float sumDividends = 0.0f;
		for (DividendPayment dividendPayment : dividendPayments) {
			if (dividendPayment.getExDividendDate().after(shareHolding.getPurchaseDate())) {
				sumDividends += dividendPayment.getAmount();
			}
		}
		
		float currentValueBasedOnDividends = sumDividends * shareHolding.getAmount();
		
		return currentValueBasedOnPrice + currentValueBasedOnDividends;
		
	}

}
