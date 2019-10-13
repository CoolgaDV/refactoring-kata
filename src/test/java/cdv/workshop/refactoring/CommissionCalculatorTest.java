package cdv.workshop.refactoring;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommissionCalculatorTest {

    private final CommissionCalculator calculator = new CommissionCalculator();

    @Test
    void shouldCalculateCommission() {

        BigDecimal sum = new BigDecimal(10);

        BigDecimal sumWithCommission = calculator.calculateCommission(sum);

        assertEquals(new BigDecimal("11.0"), sumWithCommission);
    }

}
