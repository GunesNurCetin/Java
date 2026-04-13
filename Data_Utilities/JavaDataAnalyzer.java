import java.util.*;
import java.util.stream.Collectors;

public class JavaDataAnalyzer {

    public static void main(String[] args) {
        // Simüle edilmiş bir veri kümesi (Örn: Günlük Satış Rakamları)
        List<Double> dataSet = new ArrayList<>(Arrays.asList(120.5, 450.0, 30.2, 890.4, 560.0, 15.5, 1200.0, 45.8));

        System.out.println("--- Java Veri Analiz Yardımcısı ---");
        System.out.println("İşlenen Veri Sayısı: " + dataSet.size());

        // 1. Temel İstatistikler (Veri İşleme)
        double sum = dataSet.stream().mapToDouble(Double::doubleValue).sum();
        double average = sum / dataSet.size();
        double max = Collections.max(dataSet);
        double min = Collections.min(dataSet);

        System.out.printf("Ortalama Değer: %.2f\n", average);
        System.out.println("En Yüksek Değer: " + max);
        System.out.println("En Düşük Değer: " + min);

        // 2. Veri Filtreleme (Örn: Ortalamanın üzerindeki verileri ayıkla)
        List<Double> highPerformers = dataSet.stream()
                .filter(d -> d > average)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        System.out.println("\n--- Ortalamanın Üzerindeki Veriler (Büyükten Küçüğe) ---");
        highPerformers.forEach(val -> System.out.println("> " + val));

        // 3. Veri Raporu Oluşturma (Lambda Kullanımı)
        System.out.println("\nAnaliz Durumu: " + (average > 100 ? "Yüksek Verimlilik" : "Normal Verimlilik"));
    }
}
