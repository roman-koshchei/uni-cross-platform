# Лабораторна робота № 1

## Титульна сторінка

**Факультет:** ФІТ, КНУ  
**Дисципліна:** Кросплатформне програмування  
**Рік:** 2026  
**Тема:** "Статистичний аналіз тексту"  
**Автор:** Roman Koshchei  
**Курс навчання:** [вкажіть курс]  
**Група:** [вкажіть номер групи]  
**Викладач:** [вкажіть ПІБ викладача]

## Постановка задачі

Потрібно створити Java-застосунок, у якому користувач обирає текстовий файл, а програма зчитує його вміст і показує статистику: загальну кількість слів, кількість унікальних слів, кількість речень, кількість знаків пунктуації, середню довжину слова, середню довжину речення та десять найчастіших слів.

Застосунок має бути придатним для запуску в різних операційних системах. Для цього реалізацію побудовано на стандартній Java, без прив'язки до платформно-залежних API.

## Мета

Реалізувати кросплатформний Java-застосунок для статистичного аналізу тексту з окремим ядром обчислень, простим Swing-інтерфейсом і консольним demo-режимом для headless запуску.

## Реалізація

Модуль `lab01` створено як окремий Maven-модуль, що наслідує кореневий parent `pom`. Обчислення винесено в клас `TextStatisticsAnalyzer`, тому логіку можна запускати як із GUI, так і з консольної демонстрації.

Нормалізація тексту виконується послідовно: слово визначається як послідовність літер або цифр з можливими внутрішніми апострофами чи дефісами; для підрахунку частот і унікальності слова переводяться в нижній регістр; середня довжина слова рахується лише за літерами й цифрами.

Ключові компоненти:

- `TextStatisticsAnalyzer` обчислює статистику тексту.
- `TextStatistics` і `WordFrequency` зберігають результат аналізу.
- `TextStatisticsFormatter` форматує результат для CLI та GUI.
- `TextStatisticsApp` надає Swing GUI для вибору файлу та показу результатів.
- `TextStatisticsCliDemo` запускає автономну демонстрацію без графічного інтерфейсу.

## Програмний код з поясненнями

Основний фрагмент аналізатора:

```java
public TextStatistics analyze(String text) {
    String safeText = text == null ? "" : text;
    List<String> normalizedWords = extractNormalizedWords(safeText);

    Map<String, Integer> frequencies = new LinkedHashMap<>();
    int totalWordLength = 0;
    for (String word : normalizedWords) {
        frequencies.merge(word, 1, Integer::sum);
        totalWordLength += normalizedLength(word);
    }

    int sentenceCount = countSentences(safeText);
    double averageWordLength = normalizedWords.isEmpty()
            ? 0.0
            : (double) totalWordLength / normalizedWords.size();
    double averageSentenceLength = sentenceCount == 0
            ? 0.0
            : (double) normalizedWords.size() / sentenceCount;

    List<WordFrequency> topWords = frequencies.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                    .thenComparing(Map.Entry.comparingByKey()))
            .limit(10)
            .map(entry -> new WordFrequency(entry.getKey(), entry.getValue()))
            .toList();

    return new TextStatistics(
            normalizedWords.size(),
            frequencies.size(),
            sentenceCount,
            countPunctuation(safeText),
            averageWordLength,
            averageSentenceLength,
            topWords
    );
}
```

Метод спочатку захищає програму від `null`, потім отримує нормалізований список слів. `LinkedHashMap` використано для накопичення частот, а `merge` збільшує лічильник слова без додаткових перевірок. Середні значення рахуються з перевіркою на порожній текст, щоб уникнути ділення на нуль. Список `topWords` сортується за спаданням частоти, а для однакових частот - за алфавітом.

Фрагмент виділення слів:

```java
private static final Pattern WORD_PATTERN =
        Pattern.compile("[\\p{L}\\p{N}]+(?:['’\\-][\\p{L}\\p{N}]+)*");

List<String> extractNormalizedWords(String text) {
    Matcher matcher = WORD_PATTERN.matcher(text);
    List<String> words = new ArrayList<>();
    while (matcher.find()) {
        words.add(matcher.group().toLowerCase(Locale.ROOT));
    }
    return words;
}
```

Регулярний вираз підтримує літери різних алфавітів, цифри, внутрішні апострофи та дефіси. Переведення до нижнього регістру робить підрахунок унікальних слів незалежним від регістру.

## Тестування

Додано JUnit 5 тести, які перевіряють:

- базовий підрахунок статистики;
- коректну обробку порожнього тексту;
- роботу зі словами, що містять апострофи та дефіси.

Команда запуску тестів:

```bash
mvn -q -pl lab01 test
```

## Запуск

CLI/demo:

```bash
java -cp lab01/target/classes ua.edu.unicrossplatform.lab01.TextStatisticsCliDemo
```

GUI:

```bash
java -cp lab01/target/classes ua.edu.unicrossplatform.lab01.TextStatisticsApp
```

Для демонстрації на різних ОС підготовлено окремі GUI і CLI входи, але в цьому звіті не фіксуються результати виконання на системах, де застосунок фактично не запускався.

## Висновки

Створено завершений мінімальний застосунок для статистичного аналізу тексту. Основна логіка відокремлена від інтерфейсу, що спрощує тестування і повторне використання. Реалізація підтримує консольний запуск для автоматичної перевірки та Swing GUI для ручної роботи з файлами.
