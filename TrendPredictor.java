/*******************************************************************************
 * PROJECT NAME: Trend Predictor & Linear Regression Engine
 * AUTHOR: Güneş Nur ÇETİN (@GunesNurCetin)
 * DATE: 2026-04-11
 * * AÇIKLAMA (TR): 
 * Bu proje, geçmiş veri setlerini kullanarak gelecekteki değerleri tahmin eden 
 * bir lineer regresyon modelidir. Veri trendlerini matematiksel olarak analiz eder.
 * * ÖZELLİKLER (TR):
 * 1. Trend Analysis: X ve Y eksenindeki veriler arasındaki korelasyonu hesaplama.
 * 2. Future Prediction: Eğilim çizgisi (y = mx + b) üzerinden tahmin yürütme.
 * 3. Statistical Accuracy: Veri setinin ortalama sapmalarını belirleme.
 * * KEY JAVA FEATURES (EN):
 * - Mathematical Modeling: Implementing Slope (m) and Intercept (b) formulas.
 * - Dynamic Lists: Storing coordinate-based data points.
 * - Precision Arithmetic: Using double for high-accuracy floating point math.
 *******************************************************************************/

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TrendPredictor {

    static class DataPoint {
        double x, y;
        DataPoint(double x, double y) { this.x = x; this.y = y; }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<DataPoint> points = new ArrayList<>();

        System.out.println("=========================================");
        System.out.println("   AI TREND PREDICTOR & ANALYZER         ");
        System.out.println("   DEVELOPER: Gunes Nur CETIN            ");
        System.out.println("=========================================");

        System.out.println("Örnek: 1. Gün için 100 TL satış (1 100)");
        System.out.println("Veri girişini bitirmek için 'hesapla' yazın.\n");

        while (true) {
            System.out.print("Veri Gir (X Y): ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("hesapla")) break;

            try {
                String[] parts = input.split(" ");
                points.add(new DataPoint(Double.parseDouble(parts[0]), Double.parseDouble(parts[1])));
            } catch (Exception e) {
                System.out.println("Geçersiz format! Lütfen 'X Y' şeklinde girin.");
            }
        }

        if (points.size() < 2) {
            System.out.println("HATA: Tahmin için en az 2 veri noktası gereklidir.");
            return;
        }

        // Lineer Regresyon Hesaplamaları (y = mx + b)
        double n = points.size();
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;

        for (DataPoint p : points) {
            sumX += p.x;
            sumY += p.y;
            sumXY += p.x * p.y;
            sumX2 += p.x * p.x;
        }

        double slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        double intercept = (sumY - slope * sumX) / n;

        System.out.println("\n--- ANALİZ SONUÇLARI ---");
        System.out.printf("Eğilim Denklemi: y = %.2fx + %.2f%n", slope, intercept);
        
        System.out.print("\nTahmin etmek istediğiniz X değerini girin: ");
        double targetX = scanner.nextDouble();
        double predictedY = (slope * targetX) + intercept;

        System.out.printf("TAHMİN EDİLEN DEĞER: %.2f%n", predictedY);
        System.out.println("=========================================");
    }
}
