# Uni Cross Platform

- Лекції у `lectures`
- Завдання лаб у `labs`
- Звіти у `reports`
- Реалізовані модулі: `lab01`-`lab08`.

## Запускати

```bash
# lab01
mvn -pl lab01 exec:java "-Dexec.mainClass=ua.edu.unicrossplatform.lab01.TextStatisticsApp"

# lab02
mvn -pl lab02 exec:java "-Dexec.mainClass=ua.edu.unicrossplatform.lab02.UnitConversionApp"

# lab03
mvn -pl lab03 exec:java "-Dexec.mainClass=ua.edu.unicrossplatform.lab03.Lab03Application"

# lab04
mvn -pl lab04 exec:java "-Dexec.mainClass=ua.edu.unicrossplatform.lab04.Lab04Application"

# lab05
mvn -pl lab05 exec:java "-Dexec.mainClass=ua.edu.unicrossplatform.lab05.CoinGameDemo"

# lab06
mvn -pl lab06 exec:java "-Dexec.mainClass=ua.edu.unicrossplatform.lab06.AtmDemo"

# lab07
mvn -pl lab07 exec:java "-Dexec.mainClass=ua.edu.unicrossplatform.lab07.Lab07Main"

# lab08
mvn -pl lab08 exec:java "-Dexec.mainClass=ua.edu.unicrossplatform.lab08.Lab08Main"
```

## Перевірка

- Повна перевірка модулів виконується командою `mvn test` з кореня репозиторію.
