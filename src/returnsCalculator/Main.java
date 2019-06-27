package returnsCalculator;

import java.text.ParseException;

public class Main {

	public static void main(String[] args) throws ParseException {
		
		if (args.length == 0) {
			System.out.println("No file specified");
			return;
		}
		
		String etfFileName = args[0];
		String dividendHistoryFileName = args[1];
		
		new Program().run(etfFileName, dividendHistoryFileName);
		
	}

}
