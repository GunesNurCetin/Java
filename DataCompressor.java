/*******************************************************************************
 * PROJECT NAME: Intelligent Data Compression & Archive Tool
 * AUTHOR: Güneş Nur ÇETİN (@GunesNurCetin)
 * DATE: 2026-04-11
 * * AÇIKLAMA (TR): 
 * Bu araç, tekrarlanan karakter dizilerini tespit ederek metin verilerini sıkıştırır.
 * Depolama alanından tasarruf sağlamak için geliştirilmiş bir algoritma simülasyonudur.
 * * ÖZELLİKLER (TR):
 * 1. Data Compression: Tekrarlanan verileri (AAA -> 3A) formatına dönüştürme.
 * 2. Decompression Logic: Sıkıştırılmış veriyi kayıpsız (Lossless) geri açma.
 * 3. Efficiency Report: Sıkıştırma sonrası elde edilen tasarruf oranını hesaplama.
 * * KEY JAVA FEATURES (EN):
 * - Character Processing: Iterative string analysis for pattern recognition.
 * - StringBuilder Optimization: High-performance string construction for large data.
 * - Logic Design: Implementation of Run-Length Encoding (RLE) concepts.
 *******************************************************************************/

import java.util.Scanner;

public class DataCompressor {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=========================================");
        System.out.println("   DATA COMPRESSION & ARCHIVE TOOL       ");
        System.out.println("   DEVELOPER: Gunes Nur CETIN            ");
        System.out.println("=========================================");

        System.out.println("Sıkıştırılacak metni girin (Örn: AAAABBBCC):");
        String input = scanner.nextLine();

        if (input.isEmpty()) {
            System.out.println("HATA: Boş veri işlenemez.");
            return;
        }

        // Sıkıştırma İşlemi
        String compressed = compress(input);
        
        // Verimlilik Analizi
        double ratio = (1.0 - (double) compressed.length() / input.length()) * 100;

        System.out.println("\n--- SIKIŞTIRMA SONUÇLARI ---");
        System.out.println("Orijinal Veri  : " + input);
        System.out.println("Sıkıştırılmış  : " + compressed);
        System.out.printf("Tasarruf Oranı : %%%.2f%n", ratio);

        // Geri Açma İşlemi (Doğrulama)
        System.out.println("\n--- DOĞRULAMA (DECOMPRESSION) ---");
        System.out.println("Orijinal Hali  : " + decompress(compressed));
        System.out.println("=========================================");
    }

    // Run-Length Encoding (RLE) Algoritması
    public static String compress(String text) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            int count = 1;
            while (i + 1 < text.length() && text.charAt(i) == text.charAt(i + 1)) {
                count++;
                i++;
            }
            result.append(count).append(text.charAt(i));
        }
        return result.toString();
    }

    // Sıkıştırılmış Veriyi Geri Açma
    public static String decompress(String compressed) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < compressed.length(); i++) {
            if (Character.isDigit(compressed.charAt(i))) {
                int count = Character.getNumericValue(compressed.charAt(i));
                char character = compressed.charAt(i + 1);
                for (int j = 0; j < count; j++) {
                    result.append(character);
                }
                i++; // Karakteri atla
            }
        }
        return result.toString();
    }
}
