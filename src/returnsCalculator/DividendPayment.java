package returnsCalculator;

import java.util.Date;

/**
 * This class is used to create objects that describe a single distribution payment
 * @author tripd22
 *
 */
public class DividendPayment implements Comparable<DividendPayment> {

	private String ticker;
	private float amount;
	private Date exDividendDate;
	private float reinvestmentPrice;
	
	public DividendPayment(float amount, Date exDividendDate, float reinvestmentPrice) {
		this.amount = amount;
		this.exDividendDate = exDividendDate;
		this.reinvestmentPrice = reinvestmentPrice;
		this.ticker = "-";
	}
	
	public DividendPayment(String ticker, float amount, Date exDividendDate, float reinvestmentPrice) {
		this.amount = amount;
		this.exDividendDate = exDividendDate;
		this.reinvestmentPrice = reinvestmentPrice;
		this.ticker = ticker;
	}
	
	String getTicker() {
		return this.ticker;
	}
	
	void setTicker(String ticker) {
		this.ticker = ticker;
	}
	
	float getAmount() {
		return this.amount;
	}
	
	void setAmount(float amount) {
		this.amount = amount;
	}
	
	Date getExDividendDate() {
		return this.exDividendDate;
	}
	
	void setExDividendDate(Date exDividendDate) {
		this.exDividendDate = exDividendDate;
	}
	
	float getReinvestmentPrice() {
		return this.reinvestmentPrice;
	}
	
	void setReinvestmentPrice(float reinvestmentPrice) {
		this.reinvestmentPrice = reinvestmentPrice;
	}
	
	public int compareTo(DividendPayment dividendPayment) {
		if (this.getExDividendDate().after(dividendPayment.getExDividendDate())) {
			return 1;
		} else {
			return -1;
		}
	}
}
