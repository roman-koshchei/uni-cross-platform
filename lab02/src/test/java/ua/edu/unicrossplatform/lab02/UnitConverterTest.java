package ua.edu.unicrossplatform.lab02;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UnitConverterTest {
    private final UnitConverter converter = new UnitConverter();

    @Test
    void shouldConvertBetweenScaledUnits() {
        assertEquals(2.0, converter.convert(120.0, UnitDefinition.MINUTE, UnitDefinition.HOUR), 0.0001);
        assertEquals(5000.0, converter.convert(5.0, UnitDefinition.KILOMETER, UnitDefinition.METER), 0.0001);
        assertEquals(2500.0, converter.convert(2.5, UnitDefinition.LITER, UnitDefinition.MILLILITER), 0.0001);
    }

    @Test
    void shouldConvertTemperatureWithAffineFormula() {
        assertEquals(32.0, converter.convert(0.0, UnitDefinition.CELSIUS, UnitDefinition.FAHRENHEIT), 0.0001);
        assertEquals(273.15, converter.convert(0.0, UnitDefinition.CELSIUS, UnitDefinition.KELVIN), 0.0001);
        assertEquals(100.0, converter.convert(212.0, UnitDefinition.FAHRENHEIT, UnitDefinition.CELSIUS), 0.0001);
    }

    @Test
    void shouldRejectDifferentCategories() {
        assertThrows(IllegalArgumentException.class,
                () -> converter.convert(1.0, UnitDefinition.METER, UnitDefinition.SECOND));
    }
}
