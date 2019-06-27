package returnsCalculator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Program {
	
	private PriceService priceService;
	private DividendService dividendService;
	
	public Program() {
		this.priceService = new ASXPriceService();
		this.dividendService = new ASXDividendService();
	}
	
	public void run(String etfHoldingsFileName, String dividendHistoryFileName) {

		Map<String, Set<Transaction>> transactions = ETFHoldingParser.parse(etfHoldingsFileName);
		Map<String, Set<DividendPayment>> savedDividendPayments = DividendHistoryParser.parse(dividendHistoryFileName);
		Portfolio portfolio = new Portfolio();
		
		// add each ETF ticker to etfSet
		Set<String> etfSet = new HashSet<String>();
		for (String s : transactions.keySet()) {
			etfSet.add(s);
		}
		
		// build portfolio out of etfs
		for (String etfTicker : etfSet) {
			ETF etf = new ETF(etfTicker);
			
			etf.setCurrentPrice(priceService.retrievePrice(etfTicker));
		
			// check if the saved dividends include one from < 3 months ago
			Date cutoffDate = getDate3MonthsAgo();
			boolean recentDividendsIncluded = false;
			
			Set<DividendPayment> dividendPayments = savedDividendPayments.get(etfTicker);
			for (DividendPayment dp : dividendPayments) {
				if (dp.getExDividendDate().after(cutoffDate)) {
					recentDividendsIncluded = true;
				}
			}				
			
			// if the saved dividends don't include recent dividends, query the vanguard website
			if (!recentDividendsIncluded) {
				Set<DividendPayment> newDividendPayments = dividendService.retrieveDividends(etfTicker);
				dividendPayments.addAll(newDividendPayments);
			}
			
			etf.setDividendPayments(dividendPayments);
			etf.setTransactions(transactions.get(etfTicker));
			
			portfolio.addETF(etf);
		}
		
		portfolio.printSummary();
		
		writeDividendPayments(savedDividendPayments, dividendHistoryFileName);
		
	}

	private Date getDate3MonthsAgo() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -3);
		return c.getTime();
	}

	private void writeDividendPayments(Map<String, Set<DividendPayment>> savedDividendPayments, String dividendHistoryFileName) {
		StringBuilder sb = new StringBuilder();
		sb.append("Ticker\tAmount\tExDivDate\tReinvestmentPrice\n");
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		for (Set<DividendPayment> dividendPayments : savedDividendPayments.values()) {
			for (DividendPayment dp : dividendPayments) {
				sb.append(dp.getTicker() + "\t" + dp.getAmount() + "\t" + df.format(dp.getExDividendDate()) + "\t" + dp.getReinvestmentPrice() + "\n");
			}
		}
		
		String output = sb.toString();
		try {
			Files.write(Paths.get(dividendHistoryFileName), output.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	   
	}
	

}
