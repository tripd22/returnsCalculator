package returnsCalculator;

public class ShareHoldingValue {
	private float valueBasedOnPrice;
	private float valueBasedOnAccumulatedDividends;
	
	float getValueBasedOnPrice() {
		return this.valueBasedOnPrice;
	}
	
	void setValueBasedOnPrice(float valueBasedOnPrice) {
		this.valueBasedOnPrice = valueBasedOnPrice;
	}
	
	float getValueBasedOnAccumulatedDividends() {
		return this.valueBasedOnAccumulatedDividends;
	}
	
	void setValueBasedOnAccumulatedDividends(float valueBasedOnAccumulatedDividends) {
		this.valueBasedOnAccumulatedDividends = valueBasedOnAccumulatedDividends;
	}
}
