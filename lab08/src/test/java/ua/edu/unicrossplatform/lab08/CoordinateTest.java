package ua.edu.unicrossplatform.lab08;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CoordinateTest {
    @Test
    void shouldParseLetterAndNumberCoordinate() {
        assertEquals(new Coordinate(0, 0), Coordinate.parse("A1"));
        assertEquals(new Coordinate(9, 9), Coordinate.parse("J10"));
    }

    @Test
    void shouldParseNumericCoordinate() {
        assertEquals(new Coordinate(3, 4), Coordinate.parse("3 4"));
    }
}
