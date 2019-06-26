package returnsCalculator;

import java.text.ParseException;

public class Main {

	public static void main(String[] args) throws ParseException {
		
		if (args.length == 0) {
			System.out.println("No file specified");
			return;
		}
		
		String file = args[0];
		
		new Program().run(file);
		
	}

}
