package cdv.workshop.refactoring.before;

import cdv.workshop.refactoring.BankAccountRepository;

import java.math.BigDecimal;
// FIXME: неиспользуемый import
import java.util.UUID;
import java.util.regex.Pattern;

// FIXME: (1) название класса слишком общее и не отражает его назначения
//        (2) в классе нарушен порядок определения его членов, как правило
//            используют следующую систему:
//            - константы и static поля
//            - поля уровня экземпляра класса
//            - конструктор
//            - public методы (они определяют интерфейс класса)
//            - private методы
public class BankAccountHelperWithComments {

    private final BankAccountRepository repository;

    public BankAccountHelperWithComments(BankAccountRepository repository) {
        this.repository = repository;
    }

    // Возможен многопоточный доступ - используем volatile !!!
    // FIXME: (1) неудачное название переменной (всего одна буква)
    //        (2) некорректное использование ключевого слова volatile,
    //            так как переменная используется в качестве счетчика,
    //            а операция инкремента (++) не является атомарной
    private volatile byte c;

    // FIXME: неиспользуемая переменная
    private String status;

    private static final BigDecimal COMMISSION_RATE = new BigDecimal("1.1");

    // FIXME: код метода можно использовать прямо в месте вызова,
    //        необходимость в методе отсутствует
    private void incrementCounter() {
        // FIXME: для счетчиков нужно использовать atomic типы,
        //        так как операция инкремента (++) не атомарна
        c++;
    }

    // FIXME: сигнатура метода не однозначая, когда проверка проходит успешно,
    //        возвращается значение true, когда же проверка завершается с ошибкой -
    //        кидается exception
    private boolean check(String account) throws Exception {
        // FIXME: регулярные выражения как правило выносят в констранты,
        //        так как на их создание уходит значимое время и при интенсивном
        //        использовании это может негативно сказаться на производительности
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
            // FIXME: большая вложенность конструкций, это затрудняет чтение кода
            if (check(accountFrom)) {
                if (check(accountTo)) {
                    if (!accountFrom.equals(accountTo)) {
                        long holdId = repository.holdMoney(accountFrom, sum);
                        repository.transferMoney(accountFrom, accountTo, holdId);
                    }
                }
            }
        } catch (Exception e) {
            // FIXME: (1) вывод сообщений правильно делать через систему логирования
            //        (2) как правило нужно или писать сообщение в лог или прокидывать
            //            exception дальше, делать и то и другое - в большинстве случаев
            //            неправльно, так как если exception будет залогирован выше,
            //            мы получим 2 сообщения в логе об одной и той же ошибке, что
            //            усложнит анализ логов в случае инцидента
            e.printStackTrace();
            throw new RuntimeException("Transfer failure");
        } finally {
            incrementCounter();
        }
    }
    // FIXME: код метода можно использовать прямо в месте вызова,
    //        необходимость в методе отсутствует
    private String trimString(String source) {
        return source.trim();
    }

    public String makeStatusReport() {
        // FIXME: в случае конкатенации строк без условных выражений и циклов
        //        использовать StringBuilder не имеет смысла, так как JVM
        //        сделает это в фоне за нас и лишней аллокации памяти не произойдет
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Made ");
        stringBuilder.append(c);
        stringBuilder.append(" transfers");
        return stringBuilder.toString();
    }

    // FIXME: присутствие этого метода нарушает принцип
    //        Single Responsibility для данного класса
    public BigDecimal calculateCommission(BigDecimal sum) {
        return COMMISSION_RATE.multiply(sum);
    }

}
