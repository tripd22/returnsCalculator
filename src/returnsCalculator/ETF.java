package returnsCalculator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class ETF {

	private String ticker;
	private Set<DividendPayment> dividendPayments;
	private Set<Transaction> transactions;
	private Float currentPrice;
	
	public ETF(String ticker) {
		this.ticker = ticker;
	}
	
	public String getTicker() {
		return ticker;
	}
	
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	
	public Set<DividendPayment> getDividendPayments() {
		return dividendPayments;
	}
	
	public void setDividendPayments(Set<DividendPayment> dividendPayments) {
		this.dividendPayments = dividendPayments;
	}
	
	public Set<Transaction> getTransactions() {
		return transactions;
	}
	
	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}

	public Float getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(Float currentPrice) {
		this.currentPrice = currentPrice;
	}
	
	public ShareHoldingValue calculateValue()  {
		List<Transaction> shareHoldings = new ArrayList<>(this.transactions);
		List<DividendPayment> dividendPayments = new ArrayList<>(this.dividendPayments);
		
		
		// set up object to be returned
		ShareHoldingValue shv = new ShareHoldingValue();
		shv.setNumberOfDRPShares(0);
		shv.setNumberOfPurchasedShares(0);
		
		// create object to store overallShareHolding for an ETF
		// if a person owns more than 1 lot of the same ETF, they will be merged into one holding in this object
		Transaction overallShareHolding = new Transaction();
		overallShareHolding.setAmount(0);
		overallShareHolding.setBrokerage(0.0f);
		overallShareHolding.setPrice(0.0f);
		overallShareHolding.setPurchaseDate(null);
		overallShareHolding.setTicker(shareHoldings.get(0).getTicker());
		
		// add book end values to the shareHoldings and dividendPayments lists
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		Date futureDate = null;
		try {
			futureDate = sdf.parse("01 Jan 9999");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		DividendPayment dp = new DividendPayment(0, futureDate, 0);
		Transaction sh = new Transaction(0, 0.0f, "", 0.0f, futureDate);
		dividendPayments.add(dp);
		shareHoldings.add(sh);
		
		Collections.sort(dividendPayments);
		Collections.sort(shareHoldings);
		
		// set up variables for calculation
		float sumDividends = 0.0f;
		int shareHoldingIndex = 1;
		int dividendPaymentIndex = 1;
		
		Transaction currentShareHolding = shareHoldings.get(0);
		DividendPayment currentDividendPayment = dividendPayments.get(0);
		
		// loop to find the next event for the shareholder (either external purchase of more ETF units, or DRP purchase)
		// use this to keep track of the value of the dividends as each event occurs
		while (!(currentDividendPayment.getExDividendDate().equals(futureDate) && currentShareHolding.getPurchaseDate().equals(futureDate))) {
			if (currentShareHolding.getPurchaseDate().before(currentDividendPayment.getExDividendDate())) {
				// if there is an external purchase of ETF units before the next dividend payment, reflect that purchase
				// in the current overallShareHolding
				overallShareHolding.setAmount(overallShareHolding.getAmount() + currentShareHolding.getAmount());
				overallShareHolding.setBrokerage(overallShareHolding.getBrokerage() + currentShareHolding.getBrokerage());
				float effectivePurchasePrice = (overallShareHolding.getPrice()*overallShareHolding.getAmount() + currentShareHolding.getPrice()*currentShareHolding.getAmount()) / (overallShareHolding.getAmount() + currentShareHolding.getAmount());
				overallShareHolding.setPrice(effectivePurchasePrice);
				
				shv.setNumberOfPurchasedShares(shv.getNumberOfPurchasedShares() + currentShareHolding.getAmount());
				
				// increment the shareHolding counter
				currentShareHolding = shareHoldings.get(shareHoldingIndex);
				shareHoldingIndex++;
			} else {
				// otherwise, the next dividend payment occurs before the next external share purchase
				// in this case, calculate the value of the dividend (including any potential DRP event)
				sumDividends += currentDividendPayment.getAmount() * overallShareHolding.getAmount();
				
				if (sumDividends > currentDividendPayment.getReinvestmentPrice()) {
					int numShares = (int) (sumDividends / currentDividendPayment.getReinvestmentPrice());
					
					overallShareHolding.setAmount(overallShareHolding.getAmount() + numShares);
					sumDividends -= currentDividendPayment.getReinvestmentPrice() * numShares;
					shv.setNumberOfDRPShares(shv.getNumberOfDRPShares() + numShares);
				}
				currentDividendPayment = dividendPayments.get(dividendPaymentIndex);
				dividendPaymentIndex++;
			}
		}
		
		// finalise calculation of value of ETF, and add the data to the ShareHoldingValue object
		float currentValueBasedOnDividends = sumDividends;
		float currentValueBasedOnPrice = currentPrice * overallShareHolding.getAmount();
		
		shv.setValueBasedOnAccumulatedDividends(currentValueBasedOnDividends);
		shv.setValueBasedOnPrice(currentValueBasedOnPrice);
		shv.setCurrentPrice(currentPrice);
		shv.setTicker(ticker);
		
		Float totalCost = 0.0f;
		for (Transaction t : transactions) {
			totalCost += (t.getPrice() * t.getAmount()) + t.getBrokerage();
		}
		
		shv.setTotalCost(totalCost);
		
		return shv;
		
	}
	
}
