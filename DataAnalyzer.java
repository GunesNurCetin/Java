/*******************************************************************************
 * PROJECT NAME: Dynamic Data Analyzer & Distribution Visualizer
 * AUTHOR: Güneş Nur ÇETİN (@GunesNurCetin)
 * DATE: 2026-04-11
 * * AÇIKLAMA (TR): 
 * Bu araç, girilen sayısal veri setlerini analiz ederek istatistiksel özet çıkarır.
 * Verilerin dağılımını terminal üzerinde görsel bir histogram grafiğine dönüştürür.
 * * ÖZELLİKLER (TR):
 * 1. Statistical Analysis: Min, Max ve Ortalama değerlerin hesaplanması.
 * 2. Visual Histogram: Veri yoğunluğunu karakter tabanlı grafiklerle gösterme.
 * 3. Dynamic Input: Kullanıcıdan alınan sınırsız veri girişi desteği.
 * * KEY JAVA FEATURES (EN):
 * - Math API: Advanced calculations for statistics and rounding.
 * - Collections: Managing dynamic datasets with ArrayList.
 * - String Formatting: Precise alignment for visual data representation.
 *******************************************************************************/

import java.util.*;

public class DataAnalyzer {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Double> dataPoints = new ArrayList<>();

        System.out.println("=========================================");
        System.out.println("   DATA ANALYZER & VISUALIZER            ");
        System.out.println("   DEVELOPER: Gunes Nur CETIN            ");
        System.out.println("=========================================");

        System.out.println("NOT: Veri girişini bitirmek için 'son' yazın.");

        while (true) {
            System.out.print("Veri Girin: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("son")) break;

            try {
                dataPoints.add(Double.parseDouble(input));
            } catch (NumberFormatException e) {
                System.out.println("Geçersiz sayı! Tekrar deneyin.");
            }
        }

        if (dataPoints.isEmpty()) {
            System.out.println("Hiç veri girilmedi. Program kapatılıyor.");
            return;
        }

        // İstatistiksel Hesaplamalar
        double min = Collections.min(dataPoints);
        double max = Collections.max(dataPoints);
        double sum = 0;
        for (double d : dataPoints) sum += d;
        double avg = sum / dataPoints.size();

        System.out.println("\n--- İSTATİSTİKSEL ÖZET ---");
        System.out.printf("Toplam Veri : %d%n", dataPoints.size());
        System.out.printf("Minimum     : %.2f%n", min);
        System.out.printf("Maximum     : %.2f%n", max);
        System.out.printf("Ortalama    : %.2f%n", avg);

        // Histogram Görselleştirme (Simülasyon)
        System.out.println("\n--- VERİ DAĞILIM GRAFİĞİ (HISTOGRAM) ---");
        displayHistogram(dataPoints);
        
        System.out.println("=========================================");
    }

    private static void displayHistogram(List<Double> data) {
        // Basit bir aralık analizi ile yıldız basma mantığı
        Map<String, Integer> ranges = new LinkedHashMap<>();
        ranges.put("0-25  ", 0);
        ranges.put("26-50 ", 0);
        ranges.put("51-75 ", 0);
        ranges.put("76-100", 0);

        for (double d : data) {
            if (d <= 25) ranges.put("0-25  ", ranges.get("0-25  ") + 1);
            else if (d <= 50) ranges.put("26-50 ", ranges.get("26-50 ") + 1);
            else if (d <= 75) ranges.put("51-75 ", ranges.get("51-75 ") + 1);
            else ranges.put("76-100", ranges.get("76-100") + 1);
        }

        ranges.forEach((label, count) -> {
            System.out.print(label + " | ");
            for (int i = 0; i < count; i++) System.out.print("*");
            System.out.println(" (" + count + ")");
        });
    }
}
