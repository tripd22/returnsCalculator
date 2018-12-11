package returnsCalculator;

import java.util.Date;

public class DividendPayment {
	float amount;
	Date date;
	float reinvestmentPrice;
	
	public DividendPayment(float amount, Date date, float reinvestmentPrice) {
		this.amount = amount;
		this.date = date;
		this.reinvestmentPrice = reinvestmentPrice;
	}
	
	float getAmount() {
		return this.amount;
	}
	
	void setAmount(float amount) {
		this.amount = amount;
	}
	
	Date getDate() {
		return this.date;
	}
	
	void setDate(Date date) {
		this.date = date;
	}
	
	float getReinvestmentPrice() {
		return this.reinvestmentPrice;
	}
	
	void setReinvestmentPrice(float reinvestmentPrice) {
		this.reinvestmentPrice = reinvestmentPrice;
	}
}
