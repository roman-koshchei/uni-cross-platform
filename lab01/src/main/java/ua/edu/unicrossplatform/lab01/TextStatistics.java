package ua.edu.unicrossplatform.lab01;

import java.util.List;

public record TextStatistics(
        int wordCount,
        int uniqueWordCount,
        int sentenceCount,
        int punctuationCount,
        double averageWordLength,
        double averageSentenceLength,
        List<WordFrequency> topWords
) {
}
