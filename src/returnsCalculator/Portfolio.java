package returnsCalculator;

import java.util.HashSet;
import java.util.Set;

public class Portfolio {
	private Set<ETF> etfs = new HashSet<>();
	private Float portfolioTotalCost = 0.0f;
	private Float portfolioTotalValue = 0.0f;
	
	public void addETF(ETF etf) {
		etfs.add(etf);
	}
	
	public void printSummary() {
		Set<ShareHoldingValue> shvs = getShareHoldingValues();
		
		for (ShareHoldingValue shv : shvs) {
			shv.printSummary();
			portfolioTotalCost += shv.getTotalCost();
			portfolioTotalValue += shv.getValueBasedOnAccumulatedDividends() + shv.getValueBasedOnPrice();
		}
		
		printPortfolioSummary();
	}
	
	private Set<ShareHoldingValue> getShareHoldingValues() {
		Set<ShareHoldingValue> shvs = new HashSet<>();
		for (ETF etf : etfs) {
			shvs.add(etf.calculateValue());
		}
		return shvs;
	}
	
	private void printPortfolioSummary() {
		System.out.println("\n--------------------------   TOTAL   --------------------------");
		System.out.println("Current Value: $" + portfolioTotalValue);
		System.out.println("Purchase Cost	: $" + portfolioTotalCost);
		System.out.println("Return: " + (((portfolioTotalValue / portfolioTotalCost) - 1 ) * 100) + "%");
	}
	
	
}
