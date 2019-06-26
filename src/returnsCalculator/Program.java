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
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Program {
	
	private PriceService priceService;
	private DividendService dividendService;
	
	public Program() {
		this.priceService = new ASXPriceService();
		this.dividendService = new ASXDividendService();
	}
	
	public void run(String file) {

		// parse shareholding list
		InputParser parser = new InputParser();
		List<ShareHolding> originalShares = parser.parse(file);
		
		// read in dividend payments previously found and saved
		DividendHistoryParser dhp = new DividendHistoryParser();
		Map<String, Set<DividendPayment>> savedDividendPayments = dhp.parse("exampleInputs/dividendHistory.txt");
		
		float totalCurrentValue = 0.0f;
		float totalPurchasePrice = 0.0f;
		
		// add each ticker to etfs list exactly once
		// also, add up total purchase price at same time
		Set<String> etfSet = new HashSet<String>();
		for (ShareHolding share : originalShares) {
			totalPurchasePrice += (share.getPrice() * share.getAmount()) + share.getBrokerage();
			etfSet.add(share.getTicker());
		}
	
		Map<String, Float> currentPrices = retrieveCurrentPrices(etfSet);
		
		// iterate through each etf, and calculate the current value of the holding based on price + dividends
		for (String etf : etfSet) {
			Float currentPrice = currentPrices.get(etf);
			// get date 3 months ago
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.MONTH, -3);
			Date cutoffDate = c.getTime();
			
			// check if the saved dividends include one from < 3 months ago
			boolean recentDividendsIncluded = false;
			
			Set<DividendPayment> dividendPayments = savedDividendPayments.get(etf);
			for (DividendPayment dp : dividendPayments) {
				if (dp.getExDividendDate().after(cutoffDate)) {
					recentDividendsIncluded = true;
				}
			}				
			
			// if the saved dividends don't include recent dividends, query the vanguard website
			if (!recentDividendsIncluded) {
				Set<DividendPayment> newDividendPayments = dividendService.retrieveDividends(etf);
				dividendPayments.addAll(newDividendPayments);
			}
			
				
			// add the holdings of the current etf to a list (there might be more than one holding of a given etf)
			List<ShareHolding> shareHoldings = new ArrayList<ShareHolding>();
			for (ShareHolding s : originalShares) {
				if (s.getTicker().equals(etf)) {
					shareHoldings.add(s);
				}
			}
			
			ShareHoldingValue shv;
			try {
				shv = CurrentValueCalculator.calculateCurrentValue(shareHoldings,  new ArrayList<DividendPayment>(dividendPayments),  currentPrice);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			
			// calculate the value of etf holdings at time of purchase
			float etfValueAtPurchase = 0.0f;
			for (ShareHolding shareHolding : originalShares) {
				if (shareHolding.getTicker().equals(etf)) {
					etfValueAtPurchase += shareHolding.getPrice() * shareHolding.getAmount() + shareHolding.getBrokerage();
				}
			}
			
			float etfCurrentValue = shv.getValueBasedOnAccumulatedDividends() + shv.getValueBasedOnPrice();
			float etfReturn =  ((etfCurrentValue / etfValueAtPurchase) - 1.0f) * 100;
			
			printETFSummary(shv, etf, etfCurrentValue, etfValueAtPurchase, etfReturn);
			
			totalCurrentValue += shv.getValueBasedOnAccumulatedDividends() + shv.getValueBasedOnPrice();
		}
		
		writeDividendPayments(savedDividendPayments);
		
		float returnPercentage = ((totalCurrentValue / totalPurchasePrice) - 1.0f) * 100;

		printOverallSummary(totalCurrentValue, totalPurchasePrice, returnPercentage);
		
		return;
		
	}
	
	private static void printETFSummary(ShareHoldingValue shv, String etfName, double etfCurrentValue, double etfValueAtPurchase, double etfReturn) {
		int numberOfOwnedShares = shv.getNumberOfDRPShares() + shv.getNumberOfPurchasedShares();
		double currentPrice = shv.getValueBasedOnPrice() / (shv.getNumberOfDRPShares() + shv.getNumberOfPurchasedShares());
		
		System.out.println("\n---------------------------   " + etfName + "   ---------------------------");
		System.out.println("You currently own " + numberOfOwnedShares + " shares (" + shv.getNumberOfDRPShares() + " accumulated through the DRP)");
		System.out.println("Current price is $" + currentPrice);
		System.out.println("Current price value is $" + shv.getValueBasedOnPrice() + " and accumulated dividend value is $" + shv.getValueBasedOnAccumulatedDividends() );
		System.out.println("Current total value is $" + etfCurrentValue);
		System.out.println("Purchase price was $" + etfValueAtPurchase + ", including brokerage");
		System.out.println("Return: " + etfReturn + "%");
	}
	
	private static void printOverallSummary(float totalCurrentValue, float totalPurchasePrice, float returnPercentage) {
		System.out.println("\n--------------------------   TOTAL   --------------------------");
		System.out.println("Current Value: $" + totalCurrentValue);
		System.out.println("Purchase Cost	: $" + totalPurchasePrice);
		System.out.println("Return: " + returnPercentage + "%");
	}
	
	private Map<String, Float> retrieveCurrentPrices(Set<String> etfs) {
		Map<String, Float> currentPrices = new HashMap<>();
		for (String etf : etfs) {
			Float currentPrice = priceService.retrievePrice(etf);
			currentPrices.put(etf, currentPrice);
		}
		return currentPrices;
	}

	private void writeDividendPayments(Map<String, Set<DividendPayment>> savedDividendPayments) {
		String header = "Ticker\tAmount\tExDivDate\tReinvestmentPrice\n";
		try {
			Files.write(Paths.get("exampleInputs/dividendHistory.txt"), header.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
		for (Set<DividendPayment> dividendPayments : savedDividendPayments.values()) {
			for (DividendPayment dp : dividendPayments) {
				try {
					DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					String s = dp.getTicker() + "\t" + dp.getAmount() + "\t" + df.format(dp.getExDividendDate()) + "\t" + dp.getReinvestmentPrice() + "\n";
				    Files.write(Paths.get("exampleInputs/dividendHistory.txt"), s.getBytes(), StandardOpenOption.APPEND);
				}catch (IOException e) {
				    e.printStackTrace();
				}
			}
		}
	}
	

}
