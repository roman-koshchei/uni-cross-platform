# Лабораторна робота № 8

## Титульна сторінка

**Факультет:** ФІТ, КНУ  
**Дисципліна:** Кросплатформне програмування  
**Рік:** 2026  
**Тема:** "Гра Морський бій"  
**Автор:** Roman Koshchei  
**Курс навчання:** [вкажіть курс]  
**Група:** [вкажіть номер групи]  
**Викладач:** [вкажіть ПІБ викладача]

## Постановка задачі

Потрібно створити Java-застосунок для гри "Морський бій". Користувач і комп'ютер по черзі називають координати на полі суперника. Якщо в клітинці є корабель, постріл вважається влучанням, а гравець отримує право зробити ще один хід. Мета - першим влучити в усі кораблі суперника.

Поле кожного гравця має розмір `10x10`. Флот складається з одного корабля на 4 клітинки, двох кораблів на 3 клітинки, трьох кораблів на 2 клітинки та чотирьох кораблів на 1 клітинку. Кораблі не можуть торкатися один одного кутами.

## Мета

Реалізувати графічну гру "Морський бій" `10x10` для користувача проти комп'ютера з автоматичною розстановкою флоту, правилами `MISS/HIT/SUNK`, додатковим ходом після влучання та тестами.

## Реалізація

У модулі `lab08` поле має розмір `10x10`, а флот відповідає класичному набору: `4, 3, 3, 2, 2, 2, 1, 1, 1, 1`. Обидві сторони розставляють кораблі автоматично без перетинів і дотиків навіть по діагоналі. Постріл повертає один із результатів `MISS`, `HIT` або `SUNK`. Після `HIT` та `SUNK` гравець ходить ще раз. Перший хід обирається випадково.

Графічний інтерфейс `SwingNavalBattleFrame` показує два поля `10x10`: власне поле користувача з відкритими кораблями та поле комп'ютера для пострілів. Користувач стріляє натисканням на клітинку поля комп'ютера. Після промаху хід переходить комп'ютеру, а після влучання або потоплення поточний гравець отримує додатковий хід.

Алгоритм стрільби комп'ютера не повторює постріли. Після влучання він додає сусідні клітинки до черги добивання, а після потоплення очищає цю чергу.

Ключові компоненти:

- `Battlefield` зберігає кораблі, генерує флот, перевіряє коректність і обробляє постріли.
- `Ship` моделює корабель та стан його пошкоджень.
- `Coordinate` описує координати клітинки та розбирає введення користувача.
- `ComputerShooter` реалізує алгоритм пошуку цілі й добивання після влучання.
- `NavalBattleGame` керує автоматичним матчем між двома комп'ютерними гравцями для демонстрації логіки.
- `SwingNavalBattleFrame` реалізує графічну гру користувача проти комп'ютера.
- `Lab08Main` запускає GUI-застосунок.

## Програмний код з поясненнями

Фрагмент обробки пострілу:

```java
public ShotResult shoot(Coordinate coordinate) {
    if (!isInside(coordinate)) {
        throw new IllegalArgumentException("Shot is outside the battlefield");
    }
    if (!shots.add(coordinate)) {
        throw new IllegalArgumentException("Repeated shot");
    }
    Ship ship = occupiedCells.get(coordinate);
    if (ship == null) {
        return ShotResult.MISS;
    }
    ship.hit(coordinate);
    return ship.isSunk() ? ShotResult.SUNK : ShotResult.HIT;
}
```

Метод перевіряє межі поля та забороняє повторні постріли. Якщо клітинка порожня, повертається `MISS`. Якщо в клітинці є корабель, він отримує пошкодження, після чого результат залежить від того, чи потоплено корабель.

Фрагмент алгоритму пострілів комп'ютера:

```java
public void acceptResult(Coordinate shot, ShotResult result) {
    if (result == ShotResult.SUNK) {
        targetQueue.clear();
        return;
    }
    if (result != ShotResult.HIT) {
        return;
    }
    for (Coordinate neighbor : neighbors(shot)) {
        if (!usedShots.contains(neighbor) && !targetQueue.contains(neighbor)) {
            targetQueue.add(neighbor);
        }
    }
}
```

Після звичайного влучання комп'ютер додає сусідні клітинки до черги, бо частини корабля можуть бути поруч по горизонталі або вертикалі. Після потоплення корабля черга очищається, оскільки ціль уже знищена.

Фрагмент автоматичного розміщення корабля:

```java
private static Ship placeShip(int size, Random random, Set<Coordinate> blocked) {
    while (true) {
        Orientation orientation = random.nextBoolean()
                ? Orientation.HORIZONTAL
                : Orientation.VERTICAL;
        int rowLimit = orientation == Orientation.HORIZONTAL ? SIZE : SIZE - size + 1;
        int colLimit = orientation == Orientation.HORIZONTAL ? SIZE - size + 1 : SIZE;
        int row = random.nextInt(rowLimit);
        int col = random.nextInt(colLimit);
        List<Coordinate> cells = new ArrayList<>();
        boolean valid = true;
        for (int index = 0; index < size; index++) {
            Coordinate coordinate = orientation == Orientation.HORIZONTAL
                    ? new Coordinate(row, col + index)
                    : new Coordinate(row + index, col);
            if (blocked.contains(coordinate)) {
                valid = false;
                break;
            }
            cells.add(coordinate);
        }
        if (valid) {
            return new Ship(cells);
        }
    }
}
```

Метод випадково обирає орієнтацію і стартову клітинку так, щоб корабель повністю поміщався на полі. Набір `blocked` містить зайняті клітинки та сусідні з ними клітинки, тому нові кораблі не перетинаються і не торкаються один одного.

Фрагмент запуску GUI:

```java
public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new SwingNavalBattleFrame().setVisible(true));
}
```

Точка входу відкриває графічне вікно замість консольного режиму.

Фрагмент обробки пострілу користувача в GUI:

```java
private void humanShot(Coordinate coordinate) {
    if (!gameActive || !humanTurn || computerField.wasShot(coordinate)) {
        return;
    }
    ShotResult result = computerField.shoot(coordinate);
    if (computerField.allShipsSunk()) {
        finishGame("користувач");
        return;
    }
    if (result == ShotResult.MISS) {
        humanTurn = false;
        computerTurnUntilMiss();
    }
}
```

Метод забороняє повторні постріли, виконує постріл по полю комп'ютера, перевіряє перемогу та передає хід комп'ютеру тільки після промаху.

## Тестування

JUnit 5 тести перевіряють:

- коректність автоматично згенерованого флоту;
- результати `HIT` і `SUNK`;
- відсутність повторних пострілів;
- перехід до добивання сусідніх клітин після влучання;
- розбір координат формату `A1` і числового формату.

Команда запуску тестів:

```bash
mvn -q -pl lab08 test
```

## Запуск

GUI-запуск:

```bash
java -cp lab08/target/classes ua.edu.unicrossplatform.lab08.Lab08Main
```

## Висновки

Створено завершену графічну реалізацію "Морський бій" з валідною генерацією флоту, ходами користувача через кліки по полю, алгоритмом пострілів комп'ютера, коректною обробкою пострілів, додатковими ходами та автоматизованими тестами.
