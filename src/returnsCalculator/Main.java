package returnsCalculator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Main {

	public static void main(String[] args) throws ParseException {
		
		if (args.length == 0) {
			System.out.println("No file specified");
			return;
		}
		
		String file = args[0];

		// read in dividend payments previously found and saved
		DividendHistoryParser dhp = new DividendHistoryParser();
		List<DividendPayment> savedDividendPayments = dhp.parse("exampleInputs/dividendHistory.txt");
		
		// parse shareholding list
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
			
			// get date 3 months ago
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.MONTH, -3);
			Date cutoffDate = c.getTime();
			
			// check if the saved dividends include one from < 3 months ago
			boolean recentDividendsIncluded = false;
			
			List<DividendPayment> dividendPayments = new ArrayList<DividendPayment>();
			for (DividendPayment dp : savedDividendPayments) {
				if (dp.getTicker().equals(etf)) {
					dividendPayments.add(dp);
					if (dp.getExDividendDate().after(cutoffDate)) {
						recentDividendsIncluded = true;
					}
				}
			}
			
			// if the saved dividends don't include recent dividends, query the vanguard website
			if (!recentDividendsIncluded) {
				List<DividendPayment> newDividendPayments = dividendService.retrieveDividends(etf);
				for (DividendPayment newDividendPayment : newDividendPayments) {
					boolean dividendExists = false;
					for (DividendPayment existingDividendPayment : dividendPayments) {
						if (newDividendPayment.getExDividendDate().equals(existingDividendPayment.getExDividendDate())) {
							dividendExists = true;
						}
					}
					// save the newly found dividends to the local dividend history
					if (!dividendExists) {
						dividendPayments.add(newDividendPayment);
						try {
							DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
							String s = newDividendPayment.getTicker() + "\t" + newDividendPayment.getAmount() + "\t" + df.format(newDividendPayment.getExDividendDate()) + "\t" + newDividendPayment.getReinvestmentPrice() + "\n";
						    Files.write(Paths.get("exampleInputs/dividendHistory.txt"), s.getBytes(), StandardOpenOption.APPEND);
						}catch (IOException e) {
						    e.printStackTrace();
						}
					}
				}
			}
				
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
