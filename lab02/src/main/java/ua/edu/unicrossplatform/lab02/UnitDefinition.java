package ua.edu.unicrossplatform.lab02;

public enum UnitDefinition {
    SECOND(MeasurementCategory.TIME, "Секунда", "s", 1.0),
    MINUTE(MeasurementCategory.TIME, "Хвилина", "min", 60.0),
    HOUR(MeasurementCategory.TIME, "Година", "h", 3600.0),

    METER(MeasurementCategory.DISTANCE, "Метр", "m", 1.0),
    KILOMETER(MeasurementCategory.DISTANCE, "Кілометр", "km", 1000.0),
    MILE(MeasurementCategory.DISTANCE, "Миля", "mi", 1609.344),

    METER_PER_SECOND(MeasurementCategory.SPEED, "Метр за секунду", "m/s", 1.0),
    KILOMETER_PER_HOUR(MeasurementCategory.SPEED, "Кілометр за годину", "km/h", 0.2777777777777778),
    MILE_PER_HOUR(MeasurementCategory.SPEED, "Миля за годину", "mph", 0.44704),

    GRAM(MeasurementCategory.MASS, "Грам", "g", 1.0),
    KILOGRAM(MeasurementCategory.MASS, "Кілограм", "kg", 1000.0),
    POUND(MeasurementCategory.MASS, "Фунт", "lb", 453.59237),

    SQUARE_METER(MeasurementCategory.AREA, "Квадратний метр", "m2", 1.0),
    SQUARE_KILOMETER(MeasurementCategory.AREA, "Квадратний кілометр", "km2", 1_000_000.0),
    HECTARE(MeasurementCategory.AREA, "Гектар", "ha", 10_000.0),

    CELSIUS(MeasurementCategory.TEMPERATURE, "Градус Цельсія", "C") {
        @Override
        public double toBase(double value) {
            return value;
        }

        @Override
        public double fromBase(double baseValue) {
            return baseValue;
        }
    },
    FAHRENHEIT(MeasurementCategory.TEMPERATURE, "Градус Фаренгейта", "F") {
        @Override
        public double toBase(double value) {
            return (value - 32.0) * 5.0 / 9.0;
        }

        @Override
        public double fromBase(double baseValue) {
            return baseValue * 9.0 / 5.0 + 32.0;
        }
    },
    KELVIN(MeasurementCategory.TEMPERATURE, "Кельвін", "K") {
        @Override
        public double toBase(double value) {
            return value - 273.15;
        }

        @Override
        public double fromBase(double baseValue) {
            return baseValue + 273.15;
        }
    },

    PASCAL(MeasurementCategory.PRESSURE, "Паскаль", "Pa", 1.0),
    KILOPASCAL(MeasurementCategory.PRESSURE, "Кілопаскаль", "kPa", 1000.0),
    BAR(MeasurementCategory.PRESSURE, "Бар", "bar", 100_000.0),

    MILLILITER(MeasurementCategory.VOLUME, "Мілілітр", "ml", 0.001),
    LITER(MeasurementCategory.VOLUME, "Літр", "l", 1.0),
    CUBIC_METER(MeasurementCategory.VOLUME, "Кубічний метр", "m3", 1000.0),

    JOULE(MeasurementCategory.ENERGY, "Джоуль", "J", 1.0),
    KILOJOULE(MeasurementCategory.ENERGY, "Кілоджоуль", "kJ", 1000.0),
    KILOWATT_HOUR(MeasurementCategory.ENERGY, "Кіловат-година", "kWh", 3_600_000.0);

    private final MeasurementCategory category;
    private final String displayName;
    private final String symbol;
    private final double factorToBase;

    UnitDefinition(MeasurementCategory category, String displayName, String symbol) {
        this(category, displayName, symbol, Double.NaN);
    }

    UnitDefinition(MeasurementCategory category, String displayName, String symbol, double factorToBase) {
        this.category = category;
        this.displayName = displayName;
        this.symbol = symbol;
        this.factorToBase = factorToBase;
    }

    public MeasurementCategory category() {
        return category;
    }

    public String symbol() {
        return symbol;
    }

    public double toBase(double value) {
        return value * factorToBase;
    }

    public double fromBase(double baseValue) {
        return baseValue / factorToBase;
    }

    @Override
    public String toString() {
        return displayName + " (" + symbol + ")";
    }
}
