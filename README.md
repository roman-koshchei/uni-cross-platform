# uni-cross-platform

Репозиторій для виконання лабораторних робіт з курсу кросплатформного програмування на Java.

## Стартовий стан

- `program.md` зафіксовано як основний план роботи.
- Підготовчий конспект лекцій зібрано в `lectures/lectures.md`.
- Звіти для лабораторних потрібно вести українською мовою в папці `reports/`.
- Для кожної лабораторної має бути окрема гілка `lab{number}` і окремий Maven-модуль `lab{number}`.
- Реалізовані модулі: `lab01`-`lab08`.

## Статус

| Етап | Статус | Примітка |
| --- | --- | --- |
| Підготовка лекцій | Готово | `lectures/lectures.md` створено |
| Лабораторна 01 | Готово | Модуль `lab01`, звіт `reports/lab01.md` |
| Лабораторна 02 | Готово | Модуль `lab02`, звіт `reports/lab02.md` |
| Лабораторна 03 | Готово | Модуль `lab03`, звіт `reports/lab03.md` |
| Лабораторна 04 | Готово | Модуль `lab04`, звіт `reports/lab04.md` |
| Лабораторна 05 | Готово | Модуль `lab05`, звіт `reports/lab05.md` |
| Лабораторна 06 | Готово | Модуль `lab06`, звіт `reports/lab06.md` |
| Лабораторна 07 | Готово | Модуль `lab07`, звіт `reports/lab07.md` |
| Лабораторна 08 | Готово | Модуль `lab08`, звіт `reports/lab08.md` |

## Робочий порядок

Для кожної лабораторної:

1. Створити гілку `lab{number}`.
2. Створити Maven-модуль `lab{number}`.
3. Прочитати відповідний PDF із `labs/`.
4. Реалізувати код.
5. Зібрати і запустити роботу з тайм-аутом 10 секунд.
6. Написати і запустити тести.
7. Додати звіт у `reports/`.
8. Оновити статус у цій таблиці.

## Запуск

```bash
# lab01
mvn -pl lab01 exec:java -Dexec.mainClass="ua.edu.unicrossplatform.lab01.TextStatisticsApp"

# lab02
mvn -pl lab02 exec:java -Dexec.mainClass="ua.edu.unicrossplatform.lab02.UnitConversionApp"

# lab03
mvn -pl lab03 exec:java -Dexec.mainClass="ua.edu.unicrossplatform.lab03.Lab03Application"

# lab04
mvn -pl lab04 exec:java -Dexec.mainClass="ua.edu.unicrossplatform.lab04.Lab04Application"

# lab05
mvn -pl lab05 exec:java -Dexec.mainClass="ua.edu.unicrossplatform.lab05.CoinGameDemo"

# lab06
mvn -pl lab06 exec:java -Dexec.mainClass="ua.edu.unicrossplatform.lab06.AtmDemo"

# lab07
mvn -pl lab07 exec:java -Dexec.mainClass="ua.edu.unicrossplatform.lab07.Lab07Main"

# lab08
mvn -pl lab08 exec:java -Dexec.mainClass="ua.edu.unicrossplatform.lab08.Lab08Main"
```

## Перевірка

- Повна перевірка модулів виконується командою `mvn test` з кореня репозиторію.
