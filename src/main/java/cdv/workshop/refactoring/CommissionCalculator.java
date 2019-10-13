package cdv.workshop.refactoring;

import java.math.BigDecimal;

public class CommissionCalculator {

    private static final BigDecimal COMMISSION_RATE = new BigDecimal("1.1");

    public BigDecimal calculateCommission(BigDecimal sum) {
        return COMMISSION_RATE.multiply(sum);
    }

}
