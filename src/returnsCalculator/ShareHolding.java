package returnsCalculator;

import java.util.Date;

/**
 * This class is used to create objects that describe a 
 * holding of ETFs
 * @author tripd22
 *
 */
public class ShareHolding implements Comparable<ShareHolding> {
	
	private int amount;
	private float price;
	private String ticker;
	private float brokerage;
	private Date purchaseDate;
	
	public ShareHolding() {
		this.amount = 0;
		this.price = 0.0f;
		this.ticker = null;
		this.brokerage = 0.0f;
		this.purchaseDate = null;
	}
	
	public ShareHolding(int amount, float price, String ticker, float brokerage, Date purchaseDate) {
		this.amount = amount;
		this.price = price;
		this.ticker = ticker;
		this.brokerage = brokerage;
		this.purchaseDate = purchaseDate;
	}
	
	void setAmount(int amount) {
		this.amount = amount;
	}
	
	public int getAmount() {
		return this.amount;
	}
	
	void setPrice(float price) {
		this.price = price;
	}
	
	public float getPrice() {
		return this.price;
	}
	
	void setTicker(String ticker) {
		this.ticker = ticker;
	}
	
	public String getTicker() {
		return this.ticker;
	}
	
	void setBrokerage(float brokerage) {
		this.brokerage = brokerage;
	}
	
	public float getBrokerage() {
		return this.brokerage;
	}
	
	void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	
	public Date getPurchaseDate() {
		return this.purchaseDate;
	}
	
	public boolean isValid() {
		if (this.ticker.equals("")) return false;
		if (this.amount < 1) return false;
		if (this.price < 0.0f) return false;
		if (this.brokerage < 0.0f) return false;
		
		Date date = new Date();
		if (this.purchaseDate.after(date)) return false; 
		
		// check if the stock ticker is purely alphabetic
		if (!this.getTicker().matches("^[a-zA-Z]*$")) {
			return false;
		}
		return true;
	}
	
	public int compareTo(ShareHolding shareHolding) {
		if (this.getPurchaseDate().after(shareHolding.getPurchaseDate())) {
			return 1;
		} else {
			return -1;
		}
	}
}
