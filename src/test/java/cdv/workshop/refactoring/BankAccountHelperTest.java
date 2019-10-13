package cdv.workshop.refactoring;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BankAccountHelperTest {

    private static final String ACCOUNT_FROM = "777777777777";
    private static final String ACCOUNT_TO = "333333333333";
    private static final Double SUM = 10.0;
    private static final long HOLD_ID = 10;

    private BankAccountRepository repository = mock(BankAccountRepository.class);

    private BankAccountHelper helper = new BankAccountHelper(repository);

    @Test
    void shouldTransferMoneySuccessfully() {

        when(repository.holdMoney(ACCOUNT_FROM, SUM)).thenReturn(HOLD_ID);

        helper.transfer(ACCOUNT_FROM, ACCOUNT_TO, SUM);

        InOrder order = inOrder(repository);
        order.verify(repository).holdMoney(ACCOUNT_FROM, SUM);
        order.verify(repository).transferMoney(ACCOUNT_FROM, ACCOUNT_TO, HOLD_ID);
        order.verifyNoMoreInteractions();
    }

    @Test
    void shouldDetectIllegalFromAccount() {

        RuntimeException e = assertThrows(
                RuntimeException.class,
                () -> helper.transfer("abc", ACCOUNT_TO, SUM));

        assertEquals("Transfer failure", e.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void shouldDetectIllegalToAccount() {

        RuntimeException e = assertThrows(
                RuntimeException.class,
                () -> helper.transfer(ACCOUNT_FROM, "abc", SUM));

        assertEquals("Transfer failure", e.getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void shouldHandleHoldFailure() {

        when(repository.holdMoney(ACCOUNT_FROM, SUM))
                .thenThrow(new RuntimeException("Illegal hold"));

        RuntimeException e = assertThrows(
                RuntimeException.class,
                () -> helper.transfer(ACCOUNT_FROM, ACCOUNT_TO, SUM));

        assertEquals("Transfer failure", e.getMessage());
        verify(repository).holdMoney(ACCOUNT_FROM, SUM);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldHandleMoneyTransferFailure() {

        when(repository.holdMoney(ACCOUNT_FROM, SUM)).thenReturn(HOLD_ID);
        doThrow(new RuntimeException("Illegal transfer")).when(repository)
                .transferMoney(ACCOUNT_FROM, ACCOUNT_TO, HOLD_ID);

        RuntimeException e = assertThrows(
                RuntimeException.class,
                () -> helper.transfer(ACCOUNT_FROM, ACCOUNT_TO, SUM));

        assertEquals("Transfer failure", e.getMessage());
        InOrder order = inOrder(repository);
        order.verify(repository).holdMoney(ACCOUNT_FROM, SUM);
        order.verify(repository).transferMoney(ACCOUNT_FROM, ACCOUNT_TO, HOLD_ID);
        order.verifyNoMoreInteractions();
    }

    @Test
    void shouldMakeStatusReport() {

        String report = helper.makeStatusReport();

        assertEquals("Made 0 transfers", report);
    }

    @Test
    void shouldCalculateCommission() {

        BigDecimal sum = new BigDecimal(10);

        BigDecimal sumWithCommission = helper.calculateCommission(sum);

        assertEquals(new BigDecimal("11.0"), sumWithCommission);
    }

}
