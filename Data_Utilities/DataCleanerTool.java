import java.util.*;
import java.util.stream.Collectors;

public class DataCleanerTool {

    public static void main(String[] args) {
        // Kirli ve düzensiz ham veri listesi
        List<String> rawData = Arrays.asList(
            "  laptop ", "mouSE", "   KEYBOARD", "moniTÖR ", " kablo", "USB-C "
        );

        System.out.println("--- Java Veri Temizleme Başlatıldı ---");
        System.out.println("Ham Veri: " + rawData);

        // 1. Veri Temizleme İşlemi (Trim: Boşlukları sil, LowerCase: Küçük harfe çevir)
        List<String> cleanedData = rawData.stream()
                .map(String::trim)                // Başındaki ve sonundaki boşlukları atar
                .map(String::toLowerCase)         // Standart olması için küçük harfe çevirir
                .filter(s -> !s.isEmpty())        // Boş olanları listeden çıkarır
                .distinct()                       // Tekrar eden verileri siler
                .collect(Collectors.toList());

        // 2. Formatlama (İlk harflerini büyük yap)
        List<String> formattedData = cleanedData.stream()
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                .sorted()                         // Alfabetik sırala
                .collect(Collectors.toList());

        // 3. Çıktıyı Yazdır
        System.out.println("\n--- İşlenmiş ve Düzenlenmiş Veri ---");
        formattedData.forEach(item -> System.out.println("[Kayıt]: " + item));

        System.out.println("\nSonuç: Veriler başarıyla standardize edildi.");
    }
}
