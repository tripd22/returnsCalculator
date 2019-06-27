package returnsCalculator;

/**
 * This class is used to create objects that describe the value of a share holding
 * @author tripd22
 *
 */
public class ShareHoldingValue {
	private float valueBasedOnPrice;
	private float valueBasedOnAccumulatedDividends;
	private int numberOfDRPShares;
	private int numberOfPurchasedShares;
	private float currentPrice;
	private String ticker;
	private float totalCost;
	
	public float getValueBasedOnPrice() {
		return this.valueBasedOnPrice;
	}
	
	void setValueBasedOnPrice(float valueBasedOnPrice) {
		this.valueBasedOnPrice = valueBasedOnPrice;
	}
	
	public float getValueBasedOnAccumulatedDividends() {
		return this.valueBasedOnAccumulatedDividends;
	}
	
	void setValueBasedOnAccumulatedDividends(float valueBasedOnAccumulatedDividends) {
		this.valueBasedOnAccumulatedDividends = valueBasedOnAccumulatedDividends;
	}
	
	public int getNumberOfDRPShares() {
		return this.numberOfDRPShares;
	}
	
	void setNumberOfDRPShares(int numberOfDRPShares) {
		this.numberOfDRPShares = numberOfDRPShares;
	}
	
	public int getNumberOfPurchasedShares() {
		return this.numberOfPurchasedShares;
	}
	
	void setNumberOfPurchasedShares(int numberOfPurchasedShares) {
		this.numberOfPurchasedShares = numberOfPurchasedShares;
	}

	public float getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(float currentPrice) {
		this.currentPrice = currentPrice;
	}
	
	public void printSummary() {
		int numberOfOwnedShares = numberOfDRPShares + numberOfPurchasedShares;
		float totalValue = (valueBasedOnPrice + valueBasedOnAccumulatedDividends);
		
		System.out.println("\n---------------------------   " + ticker + "   ---------------------------");
		System.out.println("You currently own " + numberOfOwnedShares + " shares (" + numberOfDRPShares + " accumulated through the DRP)");
		System.out.println("Current price is $" + currentPrice);
		System.out.println("Current price value is $" + valueBasedOnPrice + " and accumulated dividend value is $" + valueBasedOnAccumulatedDividends);
		System.out.println("Current total value is $" + totalValue);
		System.out.println("Purchase price was $" + totalCost + ", including brokerage");
		System.out.println("Return: " + ((totalValue / totalCost) - 1.0f) * 100 + "%");
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public float getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(float totalCost) {
		this.totalCost = totalCost;
	}
}
