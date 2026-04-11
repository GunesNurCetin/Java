/*******************************************************************************
 * PROJECT NAME: Smart City Parking & Billing Engine
 * AUTHOR: Güneş Nur ÇETİN (@GunesNurCetin)
 * DATE: 2026-04-11
 * * AÇIKLAMA (TR): 
 * Bu proje, bir akıllı şehir otopark yönetim simülasyonudur. Araçların giriş-çıkış
 * zamanlarını takip ederek, geçen süre üzerinden otomatik ücret hesaplaması yapar.
 * * ÖZELLİKLER (TR):
 * 1. Time Management: java.time kütüphanesi ile hassas giriş-çıkış takibi.
 * 2. Dynamic Billing: Saniye/Saat bazlı dinamik ücretlendirme algoritması.
 * 3. Data Modeling: Araç verilerini yönetmek için özel iç sınıf (Inner Class) kullanımı.
 * * KEY JAVA FEATURES (EN):
 * - HashMap: O(1) time complexity for vehicle lookups.
 * - Java Time API: Duration and LocalDateTime for precise calculations.
 * - Object-Oriented Logic: Encapsulation of vehicle data and billing logic.
 *******************************************************************************/

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SmartParkingSystem {
    
    // Araç verilerini kapsülleyen model sınıfı
    static class ParkedVehicle {
        String plate;
        LocalDateTime entryTime;

        ParkedVehicle(String plate) {
            this.plate = plate.toUpperCase();
            this.entryTime = LocalDateTime.now();
        }
    }

    private static final Map<String, ParkedVehicle> parkingLot = new HashMap<>();
    private static final double HOURLY_RATE = 25.0; // Örn: 25 TL / Saat
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=========================================");
        System.out.println("   SMART CITY PARKING SYSTEM ACTIVE      ");
        System.out.println("   DEVELOPER: Gunes Nur CETIN            ");
        System.out.println("=========================================");

        while (true) {
            System.out.println("\n[1] Giriş | [2] Çıkış (Ödeme) | [3] Durum | [4] Kapat");
            System.out.print("İşlem Seçiniz: ");
            
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.print("Plaka Giriniz: ");
                String plate = scanner.nextLine().toUpperCase();
                if (parkingLot.containsKey(plate)) {
                    System.out.println("UYARI: Bu araç zaten içeride kayıtlı!");
                } else {
                    parkingLot.put(plate, new ParkedVehicle(plate));
                    System.out.println("GİRİŞ BAŞARILI: " + LocalDateTime.now().format(formatter));
                }
            } else if (choice.equals("2")) {
                System.out.print("Çıkış Yapacak Plaka: ");
                String plate = scanner.nextLine().toUpperCase();
                if (parkingLot.containsKey(plate)) {
                    calculateAndProcess(plate);
                } else {
                    System.out.println("HATA: Kayıt bulunamadı. Lütfen plakayı kontrol edin.");
                }
            } else if (choice.equals("3")) {
                printParkingLot();
            } else if (choice.equals("4")) {
                System.out.println("Sistem güvenli bir şekilde kapatılıyor...");
                break;
            } else {
                System.out.println("Geçersiz seçenek! Lütfen 1-4 arasında bir seçim yapın.");
            }
        }
    }

    private static void calculateAndProcess(String plate) {
        ParkedVehicle v = parkingLot.get(plate);
        LocalDateTime now = LocalDateTime.now();
        
        // Simülasyon gereği: Saniyeyi saat birimi gibi hesaplıyoruz
        long diffInSeconds = Duration.between(v.entryTime, now).getSeconds();
        if (diffInSeconds == 0) diffInSeconds = 1; // Minimum 1 birim ücretlendirme
        
        double fee = diffInSeconds * HOURLY_RATE;

        System.out.println("\n---------- ÖDEME ÖZETİ ----------");
        System.out.println("PLAKA        : " + plate);
        System.out.println("GİRİŞ        : " + v.entryTime.format(formatter));
        System.out.println("ÇIKIŞ        : " + now.format(formatter));
        System.out.println("TOPLAM SÜRE  : " + diffInSeconds + " Birim (Sn)");
        System.out.printf("TOPLAM ÜCRET : %.2f TL\n", fee);
        System.out.println("---------------------------------");

        parkingLot.remove(plate);
    }

    private static void printParkingLot() {
        if (parkingLot.isEmpty()) {
            System.out.println("Otopark şu an tamamen boş.");
        } else {
            System.out.println("\n--- Aktif Park Halindeki Araçlar ---");
            System.out.println("Toplam Araç Sayısı: " + parkingLot.size());
            parkingLot.forEach((p, v) -> 
                System.out.println("-> Plaka: " + p + " | Giriş: " + v.entryTime.format(formatter)));
        }
    }
}
