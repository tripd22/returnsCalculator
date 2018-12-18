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

		List<ShareHolding> originalShares = parser.parse(file);
		
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
		
		// iterate through each etf, and calculate the current value of the holding based on price + dividends
		for (String etf : etfList) {
			Float currentPrice = priceService.retrievePrice(etf);
			List<DividendPayment> dividendPayments = dividendService.retrieveDividends(etf);
			
			// add the holdings of the current etf to a list (there might be more than one holding of a given etf)
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
			
			// print the data for the etf in a consistent format
			System.out.println("\n---------------------------   " + etf + "   ---------------------------");
			System.out.println("You currently own " + (shv.getNumberOfDRPShares() + shv.getNumberOfPurchasedShares()) + " shares (" + shv.getNumberOfDRPShares() + " accumulated through the DRP)");
			System.out.println("Current price is $" + (shv.getValueBasedOnPrice() / (shv.getNumberOfDRPShares() + shv.getNumberOfPurchasedShares())));
			System.out.println("Current price value is $" + shv.getValueBasedOnPrice() + " and accumulated dividend value is $" + shv.getValueBasedOnAccumulatedDividends() );
			System.out.println("Current total value is $" + etfCurrentValue);
			System.out.println("Purchase price was $" + etfValueAtPurchase + ", including brokerage");
			System.out.println("Return: " + etfReturn + "%");
			
			totalCurrentValue += shv.getValueBasedOnAccumulatedDividends() + shv.getValueBasedOnPrice();
		}
		
		float returnPercentage = ((totalCurrentValue / totalPurchasePrice) - 1.0f) * 100;

		// print the data for the overall portfolio
		System.out.println("\n--------------------------   TOTAL   --------------------------");
		System.out.println("Current Value: $" + totalCurrentValue);
		System.out.println("Purchase Cost	: $" + totalPurchasePrice);
		System.out.println("Return: " + returnPercentage + "%");
		
		return;
		
	}

}
