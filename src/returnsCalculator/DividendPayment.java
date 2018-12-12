package returnsCalculator;

import java.util.Date;

public class DividendPayment {
	float amount;
	Date exDividendDate;
	
	public DividendPayment(float amount, Date exDividendDate) {
		this.amount = amount;
		this.exDividendDate = exDividendDate;
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
	
}
