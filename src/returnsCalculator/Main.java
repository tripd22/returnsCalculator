package returnsCalculator;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) throws ParseException {
		
		if (args.length == 0) {
			System.out.println("No file specified");
			return;
		}
		
		String file = args[0];

		InputParser parser = new InputParser();
		
		List<ShareHolding> modifiedShares = parser.parse(file);
		List<ShareHolding> originalShares = parser.parse(file);
		List<ShareHolding> deletionShares = parser.parse(file);
		
		float totalCurrentValue = 0.0f;
		float totalPurchasePrice = 0.0f;
		
		// add each ticker to etfs list exactly once
		// also, add up total purchase price at same time
		List<String> etfList = new ArrayList<String>();
		for (ShareHolding share : originalShares) {
			totalPurchasePrice += (share.getPrice() * share.getAmount()) + share.getBrokerage();
			boolean exists = false;
			for (String s : etfList) {
				if (s.equals(share.getTicker())) {
					exists = true;
				}
			}
			if (!exists) etfList.add(share.getTicker());
		}
		
		ASXPriceService priceService = new ASXPriceService();
		ASXDividendService dividendService = new ASXDividendService();
		
//		if (etfList == null) {
//			System.out.println("ETF list is empty");
//			return;
//		}
		
		for (String etf : etfList) {
			// calculate the current value of the etf holdings
			Float currentPrice = priceService.retrievePrice(etf);
			List<DividendPayment> dividendPayments = dividendService.retrieveDividends(etf);
			List<ShareHolding> shareHoldings = new ArrayList<ShareHolding>();
			for (ShareHolding s : originalShares) {
				if (s.getTicker().equals(etf)) {
					shareHoldings.add(s);
				}
			}
			
			ShareHoldingValue shv = CurrentValueCalculator.calculateCurrentValue(shareHoldings,  dividendPayments,  currentPrice);
			
			// calculate the value of etf holdings at time of purchase
			float etfValueAtPurchase = 0.0f;
			for (ShareHolding shareHolding : originalShares) {
				if (shareHolding.getTicker().equals(etf)) {
					etfValueAtPurchase += shareHolding.getPrice() * shareHolding.getAmount() + shareHolding.getBrokerage();
				}
			}
			
			float etfCurrentValue = shv.getValueBasedOnAccumulatedDividends() + shv.getValueBasedOnPrice();
			float etfReturn =  ((etfCurrentValue / etfValueAtPurchase) - 1.0f) * 100;
			
			System.out.println("\n---------------------------   " + etf + "   ---------------------------");
			System.out.println("Current Price Value is " + shv.getValueBasedOnPrice());
			System.out.println("Current Accumulated Dividend Value is " + shv.getValueBasedOnAccumulatedDividends());
			System.out.println("Total Value is " + (shv.getValueBasedOnAccumulatedDividends() + shv.getValueBasedOnPrice()));
			System.out.println("Purchase Value: " + etfValueAtPurchase + ", Current Value: " + etfCurrentValue + ", Return: " + etfReturn + "%");
			totalCurrentValue += shv.getValueBasedOnAccumulatedDividends() + shv.getValueBasedOnPrice();
		}
		
		float returnPercentage = ((totalCurrentValue / totalPurchasePrice) - 1.0f) * 100;

		System.out.println("\n--------------------------   TOTAL   --------------------------");
		System.out.println("Purchase Value: " + totalPurchasePrice + ", Current Value: " + totalCurrentValue + ", Return: " + returnPercentage + "%");
		
		return;
		
	}

}
