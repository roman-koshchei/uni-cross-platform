package ua.edu.unicrossplatform.lab08;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Coordinate(int row, int col) {
    private static final Pattern LETTER_NUMBER = Pattern.compile("([A-Ja-j])(10|[1-9])");

    public static Coordinate parse(String input) {
        String trimmed = input.trim();
        Matcher matcher = LETTER_NUMBER.matcher(trimmed);
        if (matcher.matches()) {
            int col = matcher.group(1).toUpperCase(Locale.ROOT).charAt(0) - 'A';
            int row = Integer.parseInt(matcher.group(2)) - 1;
            return new Coordinate(row, col);
        }

        String[] parts = trimmed.split("\\s+");
        if (parts.length == 2) {
            return new Coordinate(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }
        throw new IllegalArgumentException("Invalid coordinate");
    }

    public String label() {
        return String.valueOf((char) ('A' + col)) + (row + 1);
    }
}
