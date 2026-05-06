# Лабораторна робота № 6

## Титульна сторінка

**Факультет:** ФІТ, КНУ  
**Дисципліна:** Кросплатформне програмування  
**Рік:** 2026  
**Тема:** "Симуляція роботи банкомату"  
**Автор:** Roman Koshchei  
**Курс навчання:** [вкажіть курс]  
**Група:** [вкажіть номер групи]  
**Викладач:** [вкажіть ПІБ викладача]

## Постановка задачі

Потрібно створити Java-застосунок, який моделює роботу банкомату для кількох користувачів одночасно. Кожен користувач має рахунок у банку. Банкомат має підтримувати відкриття рахунку, закриття рахунку, зняття коштів і поповнення рахунку.

У програмі має бути обмеження на суму одноразового зняття, а всі операції мають журналюватися.

## Мета

Реалізувати потокобезпечну модель рахунків і банкомату з журналюванням операцій та паралельним demo.

## Реалізація

У модулі `lab06` створено сервіс банкомату `AtmService`, який працює з рахунками через `ConcurrentHashMap`, а зміни конкретного рахунку захищаються `ReentrantLock` усередині `BankAccount`. Підтримані операції: відкриття рахунку, закриття рахунку, поповнення та зняття коштів. Для зняття діє ліміт одноразової операції, а всі успішні та відхилені дії записуються в `OperationLog`.

Demo запускає кілька потоків, які паралельно працюють з двома рахунками, після чого виводить фінальні баланси та кількість журналювань. Така модель уникає race condition, бо баланс змінюється тільки в критичній секції рахунку.

Ключові компоненти:

- `AtmService` є головним сервісом операцій банкомату.
- `BankAccount` реалізує потокобезпечний рахунок із блокуванням.
- `OperationLog` зберігає журнал усіх операцій.
- `AtmDemo` запускає багатопотокову консольну демонстрацію.

## Програмний код з поясненнями

Фрагмент зняття коштів у сервісі:

```java
public AccountSnapshot withdraw(String accountId, BigDecimal amount) {
    BankAccount account = requireAccount(accountId);
    BigDecimal normalizedAmount = normalizePositive(amount);
    if (normalizedAmount.compareTo(withdrawLimit) > 0) {
        operationLog.record(accountId, OperationType.REJECTED,
                normalizedAmount, "Перевищено ліміт зняття");
        throw new IllegalArgumentException("Withdraw limit exceeded");
    }
    try {
        BigDecimal balance = account.withdraw(normalizedAmount);
        operationLog.record(accountId, OperationType.WITHDRAW,
                normalizedAmount, "Баланс: " + balance);
        return account.snapshot();
    } catch (RuntimeException ex) {
        operationLog.record(accountId, OperationType.REJECTED,
                normalizedAmount, ex.getMessage());
        throw ex;
    }
}
```

Метод спочатку перевіряє існування рахунку та нормалізує суму. Якщо сума перевищує ліміт, операція записується як відхилена. Успішне зняття і помилки також фіксуються в журналі, тому історія містить повну картину дій.

Фрагмент потокобезпечного рахунку:

```java
BigDecimal withdraw(BigDecimal amount) {
    lock.lock();
    try {
        ensureActive();
        BigDecimal normalizedAmount = normalizePositive(amount);
        if (balance.compareTo(normalizedAmount) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }
        balance = balance.subtract(normalizedAmount);
        return balance;
    } finally {
        lock.unlock();
    }
}
```

`ReentrantLock` гарантує, що одночасні потоки не змінять баланс одного рахунку паралельно. Блок `finally` звільняє блокування навіть у випадку винятку.

## Тестування

JUnit 5 тести перевіряють:

- зміну балансу після поповнення і зняття;
- відмову при перевищенні ліміту зняття;
- коректність паралельних операцій без втрати грошей.

Команда запуску тестів:

```bash
mvn -q -pl lab06 test
```

## Запуск

Демонстрація:

```bash
mvn -q -pl lab06 exec:java -Dexec.mainClass=ua.edu.unicrossplatform.lab06.AtmDemo
```

## Висновки

Реалізовано мінімальну, але завершену модель банкомату з потокобезпечними рахунками, журналюванням і автоматичним паралельним demo. Використання окремого блокування на рівні рахунку дозволяє безпечно виконувати паралельні операції без блокування всієї системи.
