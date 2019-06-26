package returnsCalculator;

import java.util.Date;
import java.util.Objects;

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
	
	public String getTicker() {
		return this.ticker;
	}
	
	void setTicker(String ticker) {
		this.ticker = ticker;
	}
	
	public float getAmount() {
		return this.amount;
	}
	
	void setAmount(float amount) {
		this.amount = amount;
	}
	
	public Date getExDividendDate() {
		return this.exDividendDate;
	}
	
	void setExDividendDate(Date exDividendDate) {
		this.exDividendDate = exDividendDate;
	}
	
	public float getReinvestmentPrice() {
		return this.reinvestmentPrice;
	}
	
	void setReinvestmentPrice(float reinvestmentPrice) {
		this.reinvestmentPrice = reinvestmentPrice;
	}
	
	public int compareTo(DividendPayment dividendPayment) {
		if (this.getExDividendDate().after(dividendPayment.getExDividendDate())) {
			return 1;
		} else if (this.getExDividendDate().before(dividendPayment.getExDividendDate())){
			return -1;
		} else {
			return 0;
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		
		if (getClass() != o.getClass()) {
			return false;
		}
		DividendPayment dp = (DividendPayment)o;
		
		if (this.exDividendDate.equals(dp.getExDividendDate()) && this.ticker.equals(dp.getTicker())) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(ticker, exDividendDate);
	}
}
