package ua.edu.unicrossplatform.lab02;

import java.util.Locale;

public final class UnitConversionCliDemo {
    private UnitConversionCliDemo() {
    }

    public static void main(String[] args) {
        UnitConverter converter = new UnitConverter();
        System.out.println("lab02 demo");
        print(converter, 120.0, UnitDefinition.MINUTE, UnitDefinition.HOUR);
        print(converter, 5.0, UnitDefinition.KILOMETER, UnitDefinition.MILE);
        print(converter, 72.0, UnitDefinition.KILOMETER_PER_HOUR, UnitDefinition.METER_PER_SECOND);
        print(converter, 25.0, UnitDefinition.CELSIUS, UnitDefinition.FAHRENHEIT);
        print(converter, 2.5, UnitDefinition.LITER, UnitDefinition.MILLILITER);
    }

    private static void print(UnitConverter converter, double value, UnitDefinition from, UnitDefinition to) {
        double result = converter.convert(value, from, to);
        System.out.printf(Locale.ROOT, "%.3f %s = %.3f %s%n", value, from.symbol(), result, to.symbol());
    }
}
