package returnsCalculator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * This class calculates the current value of a share holding based
 * on it's dividend payments and current price
 * @author tripd22
 */
public class CurrentValueCalculator {
	
	/**
	 * Calculates the value of a list of holdings of an ETF, based on the current price, the purchase history, the dividend history, and the dividend reinvestment plan
	 * 
	 * @param shareHoldings
	 * @param dividendPayments
	 * @param currentPrice
	 * @return a ShareHoldingValue object containing the current value data for the ETF holding
	 * @throws ParseException
	 */
	public static ShareHoldingValue calculateCurrentValue(List<ShareHolding> shareHoldings, List<DividendPayment> dividendPayments, float currentPrice) throws ParseException {
		
		// set up object to be returned
		ShareHoldingValue shv = new ShareHoldingValue();
		shv.setNumberOfDRPShares(0);
		shv.setNumberOfPurchasedShares(0);
		
		// create object to store overallShareHolding for an ETF
		// if a person owns more than 1 lot of the same ETF, they will be merged into one holding in this object
		ShareHolding overallShareHolding = new ShareHolding();
		overallShareHolding.setAmount(0);
		overallShareHolding.setBrokerage(0.0f);
		overallShareHolding.setPrice(0.0f);
		overallShareHolding.setPurchaseDate(null);
		overallShareHolding.setTicker(shareHoldings.get(0).getTicker());
		
		// add book end values to the shareHoldings and dividendPayments lists
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		Date futureDate = sdf.parse("01 Jan 9999");
		DividendPayment dp = new DividendPayment(0, futureDate, 0);
		ShareHolding sh = new ShareHolding(0, 0.0f, "", 0.0f, futureDate);
		dividendPayments.add(dp);
		shareHoldings.add(sh);
		
		Collections.sort(dividendPayments);
		Collections.sort(shareHoldings);
		
		// set up variables for calculation
		float sumDividends = 0.0f;
		int shareHoldingIndex = 1;
		int dividendPaymentIndex = 1;
		
		ShareHolding currentShareHolding = shareHoldings.get(0);
		DividendPayment currentDividendPayment = dividendPayments.get(0);
		
		// loop to find the next event for the shareholder (either external purchase of more ETF units, or DRP purchase)
		// use this to keep track of the value of the dividends as each event occurs
		while (!(currentDividendPayment.getExDividendDate().equals(futureDate) && currentShareHolding.getPurchaseDate().equals(futureDate))) {
			if (currentShareHolding.getPurchaseDate().before(currentDividendPayment.getExDividendDate())) {
				// if there is an external purchase of ETF units before the next dividend payment, reflect that purchase
				// in the current overallShareHolding
				overallShareHolding.setAmount(overallShareHolding.getAmount() + currentShareHolding.getAmount());
				overallShareHolding.setBrokerage(overallShareHolding.getBrokerage() + currentShareHolding.getBrokerage());
				float effectivePurchasePrice = (overallShareHolding.getPrice()*overallShareHolding.getAmount() + currentShareHolding.getPrice()*currentShareHolding.getAmount()) / (overallShareHolding.getAmount() + currentShareHolding.getAmount());
				overallShareHolding.setPrice(effectivePurchasePrice);
				
				shv.setNumberOfPurchasedShares(shv.getNumberOfPurchasedShares() + currentShareHolding.getAmount());
				
				// increment the shareHolding counter
				currentShareHolding = shareHoldings.get(shareHoldingIndex);
				shareHoldingIndex++;
			} else {
				// otherwise, the next dividend payment occurs before the next external share purchase
				// in this case, calculate the value of the dividend (including any potential DRP event)
				sumDividends += currentDividendPayment.getAmount() * overallShareHolding.getAmount();
				
				if (sumDividends > currentDividendPayment.getReinvestmentPrice()) {
					int numShares = (int) (sumDividends / currentDividendPayment.getReinvestmentPrice());
					
					overallShareHolding.setAmount(overallShareHolding.getAmount() + numShares);
					sumDividends -= currentDividendPayment.getReinvestmentPrice() * numShares;
					shv.setNumberOfDRPShares(shv.getNumberOfDRPShares() + numShares);
				}
				currentDividendPayment = dividendPayments.get(dividendPaymentIndex);
				dividendPaymentIndex++;
			}
		}
		
		// finalise calculation of value of ETF, and add the data to the ShareHoldingValue object
		float currentValueBasedOnDividends = sumDividends;
		float currentValueBasedOnPrice = currentPrice * overallShareHolding.getAmount();
		
		shv.setValueBasedOnAccumulatedDividends(currentValueBasedOnDividends);
		shv.setValueBasedOnPrice(currentValueBasedOnPrice);
		
		return shv;
		
	}

}
