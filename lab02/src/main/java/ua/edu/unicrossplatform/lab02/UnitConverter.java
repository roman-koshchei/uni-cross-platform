package ua.edu.unicrossplatform.lab02;

import java.util.Arrays;
import java.util.List;

public final class UnitConverter {
    public double convert(double value, UnitDefinition from, UnitDefinition to) {
        if (from.category() != to.category()) {
            throw new IllegalArgumentException("Одиниці належать до різних категорій.");
        }
        double baseValue = from.toBase(value);
        return to.fromBase(baseValue);
    }

    public List<UnitDefinition> unitsFor(MeasurementCategory category) {
        return Arrays.stream(UnitDefinition.values())
                .filter(unit -> unit.category() == category)
                .toList();
    }
}
