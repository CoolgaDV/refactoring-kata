package cdv.workshop.refactoring;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.regex.Pattern;

public class BankAccountHelper {

    private final BankAccountRepository repository;

    public BankAccountHelper(BankAccountRepository repository) {
        this.repository = repository;
    }

    // Возможен многопоточный доступ - используем volatile !!!
    private volatile byte c;

    private String status;

    private static final BigDecimal COMMISSION_RATE = new BigDecimal("1.1");

    private void incrementCounter() {
        c++;
    }

    private boolean check(String account) throws Exception {
        Pattern regex = Pattern.compile("^[0-9]{7,14}$");
        if (!regex.matcher(account).matches()) {
            throw new Exception("Invalid account number");
        }
        return true;
    }

    public void transfer(String from, String to, Double sum) {
        try {
            String accountFrom = trimString(from);
            String accountTo = trimString(to);
            if (check(accountFrom)) {
                if (check(accountTo)) {
                    if (!accountFrom.equals(accountTo)) {
                        long holdId = repository.holdMoney(accountFrom, sum);
                        repository.transferMoney(accountFrom, accountTo, holdId);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Transfer failure");
        } finally {
            incrementCounter();
        }
    }

    private String trimString(String source) {
        return source.trim();
    }

    public String makeStatusReport() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Made ");
        stringBuilder.append(c);
        stringBuilder.append(" transfers");
        return stringBuilder.toString();
    }

    public BigDecimal calculateCommission(BigDecimal sum) {
        return COMMISSION_RATE.multiply(sum);
    }

}