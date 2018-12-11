package returnsCalculator;

public class ShareHolding {
	
	private int amount;
	private float price;
	private String ticker;
	private float brokerage;
	
	public ShareHolding() {
		this.amount = 0;
		this.price = 0.0f;
		this.ticker = null;
		this.brokerage = 0.0f;
	}
	
	public ShareHolding(int amount, float price, String ticker, float brokerage) {
		this.amount = amount;
		this.price = price;
		this.ticker = ticker;
		this.brokerage = brokerage;
	}
	
	void setAmount(int amount) {
		this.amount = amount;
	}
	
	int getAmount() {
		return this.amount;
	}
	
	void setPrice(float price) {
		this.price = price;
	}
	
	float getPrice() {
		return this.price;
	}
	
	void setTicker(String ticker) {
		this.ticker = ticker;
	}
	
	String getTicker() {
		return this.ticker;
	}
	
	void setBrokerage(float brokerage) {
		this.brokerage = brokerage;
	}
	
	float getBrokerage() {
		return this.brokerage;
	}
	
	boolean isValid() {
		if (this.ticker.equals("")) return false;
		if (this.amount < 1) return false;
		if (this.price < 0.0f) return false;
		if (this.brokerage < 0.0f) return false;
		
		// check if the stock ticker is purely alphabetic
		if (!this.getTicker().matches(".*[a-zA-Z]+.*[a-zA-Z]")) {
			return false;
		}
		return true;
	}
}
