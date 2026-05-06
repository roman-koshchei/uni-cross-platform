package ua.edu.unicrossplatform.lab02;

public enum MeasurementCategory {
    TIME("Час"),
    DISTANCE("Відстань"),
    SPEED("Швидкість"),
    MASS("Маса"),
    AREA("Площа"),
    TEMPERATURE("Температура"),
    PRESSURE("Тиск"),
    VOLUME("Об'єм"),
    ENERGY("Енергія");

    private final String displayName;

    MeasurementCategory(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
