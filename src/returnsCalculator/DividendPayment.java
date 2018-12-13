package returnsCalculator;

import java.util.Date;

public class DividendPayment implements Comparable<DividendPayment> {

	float amount;
	Date exDividendDate;
	float reinvestmentPrice;
	
	public DividendPayment(float amount, Date exDividendDate, float reinvestmentPrice) {
		this.amount = amount;
		this.exDividendDate = exDividendDate;
		this.reinvestmentPrice = reinvestmentPrice;
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
