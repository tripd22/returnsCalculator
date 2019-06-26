package returnsCalculator;

import java.util.Set;

public interface DividendService {
	public Set<DividendPayment> retrieveDividends(String ticker);
}
