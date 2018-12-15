package returnsCalculator;

import java.util.List;

public class Main {

	public static void main(String[] args) {
		
		if (args.length == 0) {
			System.out.println("No file specified");
			return;
		}
		
		String file = args[0];

		InputParser parser = new InputParser();
		
		List<ShareHolding> modifiedShares = parser.parse(file);
		List<ShareHolding> originalShares = parser.parse(file);
		
		ASXPriceService priceService = new ASXPriceService();
		ASXDividendService dividendService = new ASXDividendService();
		
		float totalCurrentValue = 0.0f;
		float totalPurchasePrice = 0.0f;
		
		if (modifiedShares != null) {
			for (int i = 0; i < modifiedShares.size(); i++) {
				ShareHolding shareHolding = modifiedShares.get(i);
				ShareHolding originalShareHolding = originalShares.get(i);
				
				Float currentPrice = priceService.retrievePrice(shareHolding.getTicker());
				//System.out.println("Ticker: " + shareHolding.getTicker() + ",amount: " + shareHolding.getAmount() + ", purchasePrice: " + shareHolding.getPrice() + ", currentPrice: " + currentPrice +", brokerage: " + shareHolding.getBrokerage() );
				List<DividendPayment> dividendPayments = dividendService.retrieveDividends(shareHolding.getTicker());
				
				float currentValue = CurrentValueCalculator.calculateCurrentValue(shareHolding, dividendPayments, currentPrice);
				float purchasePrice = originalShareHolding.getPrice() * originalShareHolding.getAmount() + shareHolding.getBrokerage();
				totalCurrentValue += currentValue;
				totalPurchasePrice += purchasePrice;
				System.out.println(shareHolding.getAmount() + " " + shareHolding.getTicker() + ", current value is " + shareHolding.getAmount()*currentPrice + ", w/ divs is " + currentValue);
				
			}
		}
		
		float returnPercentage = totalCurrentValue / totalPurchasePrice;
		
		System.out.println("Purchase Value: " + totalPurchasePrice + ", Current Value: " + totalCurrentValue + ", Return: " + returnPercentage + "%");
		
		
		
	}

}
