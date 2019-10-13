package cdv.workshop.refactoring;

/**
 * Внешний компонент, реализация которого
 * в рамках текущего workshop-a нас не интересует
 */
public interface BankAccountRepository {

    void transferMoney(String accountFrom, String accountTo, long sum);

    long holdMoney(String accountFrom, Double sum);

    // .. а также ряд других методов

}
