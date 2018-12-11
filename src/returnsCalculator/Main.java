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
		
		if (shares != null) {
			for (ShareHolding item: shares) {
				System.out.println("Ticker: " + item.getTicker() + ",amount: " + item.getAmount() + ", price: " + item.getPrice() + ", brokerage: " + item.getBrokerage() );
			}
		}
		
	}

}
