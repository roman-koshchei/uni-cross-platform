package ua.edu.unicrossplatform.lab01;

import java.util.Locale;

public final class TextStatisticsFormatter {
    private TextStatisticsFormatter() {
    }

    public static String format(TextStatistics statistics) {
        StringBuilder builder = new StringBuilder();
        builder.append("Кількість слів: ").append(statistics.wordCount()).append(System.lineSeparator());
        builder.append("Унікальні слова (без регістру): ").append(statistics.uniqueWordCount()).append(System.lineSeparator());
        builder.append("Кількість речень: ").append(statistics.sentenceCount()).append(System.lineSeparator());
        builder.append("Кількість знаків пунктуації: ").append(statistics.punctuationCount()).append(System.lineSeparator());
        builder.append("Середня довжина слова: ")
                .append(String.format(Locale.ROOT, "%.2f", statistics.averageWordLength()))
                .append(System.lineSeparator());
        builder.append("Середня довжина речення (слів): ")
                .append(String.format(Locale.ROOT, "%.2f", statistics.averageSentenceLength()))
                .append(System.lineSeparator());
        builder.append("Топ-10 слів:").append(System.lineSeparator());
        if (statistics.topWords().isEmpty()) {
            builder.append("- немає даних").append(System.lineSeparator());
        } else {
            for (WordFrequency frequency : statistics.topWords()) {
                builder.append("- ").append(frequency.word()).append(": ").append(frequency.count())
                        .append(System.lineSeparator());
            }
        }
        return builder.toString();
    }
}
