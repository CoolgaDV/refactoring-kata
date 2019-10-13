package cdv.workshop.refactoring;

import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

public class MoneyTransferService {

    private static final Pattern BANK_ACCOUNT_REGEX = Pattern.compile("^[0-9]{7,14}$");

    private final BankAccountRepository repository;

    // Concurrent access
    private final AtomicLong counter = new AtomicLong();

    public MoneyTransferService(BankAccountRepository repository) {
        this.repository = repository;
    }

    public void transfer(String accountFrom, String accountTo, Double sum) {
        try {

            String from = formatAccountNumber(accountFrom);
            String to = formatAccountNumber(accountTo);

            if (from.equals(to)) {
                return;
            }

            long holdId = repository.holdMoney(from, sum);
            repository.transferMoney(accountFrom, to, holdId);

        } catch (Exception e) {
            throw new RuntimeException("Transfer failure", e);
        } finally {
            counter.incrementAndGet();
        }
    }

    public String makeStatusReport() {
        return "Made " + counter.get() + " transfers";
    }

    private String formatAccountNumber(String accountNumber) throws Exception {

        String formattedAccount = accountNumber.trim();

        if (!BANK_ACCOUNT_REGEX.matcher(formattedAccount).matches()) {
            throw new Exception("Invalid account number: " + accountNumber);
        }

        return formattedAccount;
    }

}
