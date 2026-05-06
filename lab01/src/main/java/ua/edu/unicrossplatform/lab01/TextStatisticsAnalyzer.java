package ua.edu.unicrossplatform.lab01;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TextStatisticsAnalyzer {
    private static final Pattern WORD_PATTERN = Pattern.compile("[\\p{L}\\p{N}]+(?:['’\\-][\\p{L}\\p{N}]+)*");
    private static final Pattern SENTENCE_SPLIT_PATTERN = Pattern.compile("[.!?]+(?:\\s+|$)");

    public TextStatistics analyze(String text) {
        String safeText = text == null ? "" : text;
        List<String> normalizedWords = extractNormalizedWords(safeText);

        Map<String, Integer> frequencies = new LinkedHashMap<>();
        int totalWordLength = 0;
        for (String word : normalizedWords) {
            frequencies.merge(word, 1, Integer::sum);
            totalWordLength += normalizedLength(word);
        }

        int sentenceCount = countSentences(safeText);
        double averageWordLength = normalizedWords.isEmpty() ? 0.0 : (double) totalWordLength / normalizedWords.size();
        double averageSentenceLength = sentenceCount == 0 ? 0.0 : (double) normalizedWords.size() / sentenceCount;

        List<WordFrequency> topWords = frequencies.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(10)
                .map(entry -> new WordFrequency(entry.getKey(), entry.getValue()))
                .toList();

        return new TextStatistics(
                normalizedWords.size(),
                frequencies.size(),
                sentenceCount,
                countPunctuation(safeText),
                averageWordLength,
                averageSentenceLength,
                topWords
        );
    }

    List<String> extractNormalizedWords(String text) {
        Matcher matcher = WORD_PATTERN.matcher(text);
        List<String> words = new ArrayList<>();
        while (matcher.find()) {
            words.add(matcher.group().toLowerCase(Locale.ROOT));
        }
        return words;
    }

    private int countSentences(String text) {
        String trimmed = text.trim();
        if (trimmed.isEmpty()) {
            return 0;
        }

        int count = 0;
        int start = 0;
        Matcher matcher = SENTENCE_SPLIT_PATTERN.matcher(trimmed);
        while (matcher.find()) {
            String sentence = trimmed.substring(start, matcher.start()).trim();
            if (containsWord(sentence)) {
                count++;
            }
            start = matcher.end();
        }

        String tail = trimmed.substring(start).trim();
        if (containsWord(tail)) {
            count++;
        }
        return count;
    }

    private boolean containsWord(String text) {
        return WORD_PATTERN.matcher(text).find();
    }

    private int normalizedLength(String word) {
        int count = 0;
        for (int i = 0; i < word.length(); i++) {
            char current = word.charAt(i);
            if (Character.isLetterOrDigit(current)) {
                count++;
            }
        }
        return count;
    }

    private int countPunctuation(String text) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            int type = Character.getType(text.charAt(i));
            if (type == Character.CONNECTOR_PUNCTUATION
                    || type == Character.DASH_PUNCTUATION
                    || type == Character.START_PUNCTUATION
                    || type == Character.END_PUNCTUATION
                    || type == Character.INITIAL_QUOTE_PUNCTUATION
                    || type == Character.FINAL_QUOTE_PUNCTUATION
                    || type == Character.OTHER_PUNCTUATION) {
                count++;
            }
        }
        return count;
    }
}
