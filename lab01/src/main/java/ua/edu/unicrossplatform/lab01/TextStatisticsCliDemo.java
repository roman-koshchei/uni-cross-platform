package ua.edu.unicrossplatform.lab01;

public final class TextStatisticsCliDemo {
    private TextStatisticsCliDemo() {
    }

    public static void main(String[] args) {
        String sampleText = "Привіт, світе! Це простий тестовий текст. Текст містить слова, слова і ще слова.";
        TextStatisticsAnalyzer analyzer = new TextStatisticsAnalyzer();
        TextStatistics statistics = analyzer.analyze(sampleText);
        System.out.println("lab01 demo");
        System.out.println(TextStatisticsFormatter.format(statistics));
    }
}
