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
}
