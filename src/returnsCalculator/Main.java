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
		
		List<ShareHolding> shares = parser.parse(file);
		
		ASXPriceService priceService = new ASXPriceService();
		ASXDividendService dividendService = new ASXDividendService();
		
		float totalCurrentValue = 0.0f;
		float totalPurchaseValue = 0.0f;
		
		if (shares != null) {
			for (ShareHolding shareHolding: shares) {
				Float currentPrice = priceService.retrievePrice(shareHolding.getTicker());
				//System.out.println("Ticker: " + shareHolding.getTicker() + ",amount: " + shareHolding.getAmount() + ", purchasePrice: " + shareHolding.getPrice() + ", currentPrice: " + currentPrice +", brokerage: " + shareHolding.getBrokerage() );
				List<DividendPayment> dividendPayments = dividendService.retrieveDividends(shareHolding.getTicker());
				
				float currentValue = CurrentValueCalculator.calculateCurrentValue(shareHolding, dividendPayments, currentPrice);
				float purchaseValue = shareHolding.getPrice() * shareHolding.getAmount() + shareHolding.getBrokerage();
				totalCurrentValue += currentValue;
				totalPurchaseValue += purchaseValue;
				System.out.println(shareHolding.getAmount() + " " + shareHolding.getTicker() + ", current value is " + shareHolding.getAmount()*currentPrice + ", w/ divs is " + currentValue);
				
			}
		}
		
		float returnPercentage = totalCurrentValue / totalPurchaseValue;
		
		System.out.println("Purchase Value: " + totalPurchaseValue + ", Current Value: " + totalCurrentValue + ", Return: " + returnPercentage + "%");
		
		
		
	}

}
