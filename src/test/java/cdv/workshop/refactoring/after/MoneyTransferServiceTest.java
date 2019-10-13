package cdv.workshop.refactoring.after;

import cdv.workshop.refactoring.BankAccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MoneyTransferServiceTest {

    private static final String ACCOUNT_FROM = "777777777777";
    private static final String ACCOUNT_TO = "333333333333";
    private static final Double SUM = 10.0;
    private static final long HOLD_ID = 10;

    private BankAccountRepository repository = mock(BankAccountRepository.class);

    private MoneyTransferService service = new MoneyTransferService(repository);

    @Test
    void shouldTransferMoneySuccessfully() {

        when(repository.holdMoney(ACCOUNT_FROM, SUM)).thenReturn(HOLD_ID);

        service.transfer(ACCOUNT_FROM, ACCOUNT_TO, SUM);

        InOrder order = inOrder(repository);
        order.verify(repository).holdMoney(ACCOUNT_FROM, SUM);
        order.verify(repository).transferMoney(ACCOUNT_FROM, ACCOUNT_TO, HOLD_ID);
        order.verifyNoMoreInteractions();
    }

    @Test
    void shouldDetectIllegalFromAccount() {

        RuntimeException e = assertThrows(
                RuntimeException.class,
                () -> service.transfer("abc", ACCOUNT_TO, SUM));

        assertEquals("Transfer failure", e.getMessage());
        assertEquals("Invalid account number: abc", e.getCause().getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void shouldDetectIllegalToAccount() {

        RuntimeException e = assertThrows(
                RuntimeException.class,
                () -> service.transfer(ACCOUNT_FROM, "abc", SUM));

        assertEquals("Transfer failure", e.getMessage());
        assertEquals("Invalid account number: abc", e.getCause().getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void shouldHandleHoldFailure() {

        String exceptionMessage = "Illegal hold";
        when(repository.holdMoney(ACCOUNT_FROM, SUM))
                .thenThrow(new RuntimeException(exceptionMessage));

        RuntimeException e = assertThrows(
                RuntimeException.class,
                () -> service.transfer(ACCOUNT_FROM, ACCOUNT_TO, SUM));

        assertEquals("Transfer failure", e.getMessage());
        assertEquals(exceptionMessage, e.getCause().getMessage());
        verify(repository).holdMoney(ACCOUNT_FROM, SUM);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldHandleMoneyTransferFailure() {

        when(repository.holdMoney(ACCOUNT_FROM, SUM)).thenReturn(HOLD_ID);
        String exceptionMessage = "Illegal transfer";
        doThrow(new RuntimeException(exceptionMessage)).when(repository)
                .transferMoney(ACCOUNT_FROM, ACCOUNT_TO, HOLD_ID);

        RuntimeException e = assertThrows(
                RuntimeException.class,
                () -> service.transfer(ACCOUNT_FROM, ACCOUNT_TO, SUM));

        assertEquals("Transfer failure", e.getMessage());
        assertEquals(exceptionMessage, e.getCause().getMessage());
        InOrder order = inOrder(repository);
        order.verify(repository).holdMoney(ACCOUNT_FROM, SUM);
        order.verify(repository).transferMoney(ACCOUNT_FROM, ACCOUNT_TO, HOLD_ID);
        order.verifyNoMoreInteractions();
    }

    @Test
    void shouldMakeStatusReport() {

        String report = service.makeStatusReport();

        assertEquals("Made 0 transfers", report);
    }

}
