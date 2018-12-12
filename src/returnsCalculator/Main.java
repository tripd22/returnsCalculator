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
		
		if (shares != null) {
			for (ShareHolding item: shares) {
				Float currentPrice = priceService.retrievePrice(item.getTicker());
				System.out.println("Ticker: " + item.getTicker() + ",amount: " + item.getAmount() + ", purchasePrice: " + item.getPrice() + ", currentPrice: " + currentPrice +", brokerage: " + item.getBrokerage() );
			}
		}
		
		// testing dividend service
		ASXDividendService d = new ASXDividendService();
		List<DividendPayment> dividends = d.retrieveDividends("VAS");
		
	}

}
