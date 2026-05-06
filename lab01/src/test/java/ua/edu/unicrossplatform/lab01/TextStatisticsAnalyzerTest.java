package ua.edu.unicrossplatform.lab01;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextStatisticsAnalyzerTest {
    private final TextStatisticsAnalyzer analyzer = new TextStatisticsAnalyzer();

    @Test
    void shouldCalculateStatisticsForMixedCaseText() {
        String text = "Hello, hello! Java code works. Works, yes?";

        TextStatistics statistics = analyzer.analyze(text);

        assertEquals(7, statistics.wordCount());
        assertEquals(5, statistics.uniqueWordCount());
        assertEquals(3, statistics.sentenceCount());
        assertEquals(5, statistics.punctuationCount());
        assertEquals(31.0 / 7.0, statistics.averageWordLength(), 0.0001);
        assertEquals(7.0 / 3.0, statistics.averageSentenceLength(), 0.0001);
        assertEquals(List.of(
                new WordFrequency("hello", 2),
                new WordFrequency("works", 2),
                new WordFrequency("code", 1),
                new WordFrequency("java", 1),
                new WordFrequency("yes", 1)
        ), statistics.topWords());
    }

    @Test
    void shouldReturnZerosForEmptyText() {
        TextStatistics statistics = analyzer.analyze("   ");

        assertEquals(0, statistics.wordCount());
        assertEquals(0, statistics.uniqueWordCount());
        assertEquals(0, statistics.sentenceCount());
        assertEquals(0, statistics.punctuationCount());
        assertEquals(0.0, statistics.averageWordLength(), 0.0001);
        assertEquals(0.0, statistics.averageSentenceLength(), 0.0001);
    }

    @Test
    void shouldKeepApostrophesAndHyphensInsideWords() {
        List<String> words = analyzer.extractNormalizedWords("It's high-speed and rock'n'roll.");

        assertEquals(List.of("it's", "high-speed", "and", "rock'n'roll"), words);
    }
}
