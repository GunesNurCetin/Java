/*******************************************************************************
 * PROJECT NAME: Mini NoSQL Engine & Flat-File Data Store
 * AUTHOR: Güneş Nur ÇETİN (@GunesNurCetin)
 * DATE: 2026-04-11
 * * AÇIKLAMA (TR): 
 * Bu proje, verileri yapılandırılmış bir formatta (Key-Value) yerel dosyada saklayan 
 * hafif bir veri deposudur. Veritabanı yönetim sistemlerinin temel mantığını simüle eder.
 * * ÖZELLİKLER (TR):
 * 1. Persistent Storage: Verileri uygulama kapansa bile dosyada saklama özelliği.
 * 2. Key-Value Mapping: Her veriye benzersiz bir anahtar ile hızlı erişim.
 * 3. Search & Retrieve: Dosya içindeki veriler arasında anlık arama yapabilme.
 * * KEY JAVA FEATURES (EN):
 * - File Persistence: Managing data storage with BufferedWriter/BufferedReader.
 * - Hash Map Integration: Fast in-memory access during runtime.
 * - Exception Management: Robust handling for file missing or corrupt data cases.
 *******************************************************************************/

import java.io.*;
import java.util.*;

public class MiniDatabase {

    private static final String DB_FILE = "storage.db";
    private static Map<String, String> dataStore = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        loadDatabase(); // Program başlarken eski verileri yükle

        System.out.println("=========================================");
        System.out.println("   MINI NOSQL DATA STORE SYSTEM          ");
        System.out.println("   DEVELOPER: Gunes Nur CETIN            ");
        System.out.println("=========================================");

        while (true) {
            System.out.println("\n[1] Veri Ekle/Güncelle | [2] Veri Ara | [3] Tümünü Listele | [4] Çıkış");
            System.out.print("İşlem Seçiniz: ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.print("Anahtar (Key): ");
                String key = scanner.nextLine();
                System.out.print("Değer (Value): ");
                String value = scanner.nextLine();
                
                dataStore.put(key, value);
                saveDatabase();
                System.out.println("LOG: Veri başarıyla diske kaydedildi.");

            } else if (choice.equals("2")) {
                System.out.print("Aranacak Anahtar: ");
                String searchKey = scanner.nextLine();
                if (dataStore.containsKey(searchKey)) {
                    System.out.println("SONUÇ: " + searchKey + " => " + dataStore.get(searchKey));
                } else {
                    System.out.println("HATA: Anahtar bulunamadı.");
                }

            } else if (choice.equals("3")) {
                if (dataStore.isEmpty()) {
                    System.out.println("Veri tabanı şu an boş.");
                } else {
                    System.out.println("\n--- Mevcut Veri Kayıtları ---");
                    dataStore.forEach((k, v) -> System.out.println("K: " + k + " | V: " + v));
                }

            } else if (choice.equals("4")) {
                System.out.println("Veriler güvenli bir şekilde saklandı. Çıkılıyor...");
                break;
            }
        }
    }

    // Verileri dosyadan belleğe (RAM) yükler
    private static void loadDatabase() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DB_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length >= 2) {
                    dataStore.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            // Dosya yoksa sorun değil, yeni oluşturulacak
        }
    }

    // Bellekteki verileri dosyaya yazar
    private static void saveDatabase() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DB_FILE))) {
            for (Map.Entry<String, String> entry : dataStore.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Kaydetme Hatası: " + e.getMessage());
        }
    }
}
