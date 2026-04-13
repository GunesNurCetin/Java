import java.util.*;
import java.io.*;

// Temal Bir Varlık Sınıfı (OOP: Encapsulation)
class Asset {
    private String name;
    private double price;

    public Asset(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
}

// Portföy Yönetim Motoru
public class CryptoPortfolioManager {
    private static final String FILE_NAME = "portfolio.txt";
    private List<Asset> myAssets = new ArrayList<>();

    public static void main(String[] args) {
        CryptoPortfolioManager manager = new CryptoPortfolioManager();
        manager.start();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("--- Java Portföy Yönetim Sistemine Hoşgeldiniz ---");

        while (true) {
            System.out.println("\n1. Varlık Ekle | 2. Portföyü Listele | 3. Dosyaya Kaydet | 4. Çıkış");
            System.out.print("Seçiminiz: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());

                if (choice == 1) {
                    System.out.print("Coin Adı (örn: BTC): ");
                    String name = scanner.nextLine();
                    System.out.print("Fiyatı ($): ");
                    double price = Double.parseDouble(scanner.nextLine());
                    myAssets.add(new Asset(name, price));
                    System.out.println("Başarıyla eklendi!");

                } else if (choice == 2) {
                    displayPortfolio();
                } else if (choice == 3) {
                    saveToFile();
                } else if (choice == 4) {
                    System.out.println("Sistemden çıkılıyor...");
                    break;
                }
            } catch (Exception e) {
                System.out.println("Hata: Geçersiz giriş yaptınız!");
            }
        }
    }

    private void displayPortfolio() {
        System.out.println("\n--- Mevcut Portföyünüz ---");
        double total = 0;
        for (Asset a : myAssets) {
            System.out.printf("Varlık: %s | Değer: %.2f$\n", a.getName(), a.getPrice());
            total += a.getPrice();
        }
        System.out.printf("Toplam Portföy Değeri: %.2f$\n", total);
    }

    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Asset a : myAssets) {
                writer.println(a.getName() + "," + a.getPrice());
            }
            System.out.println("Veriler '" + FILE_NAME + "' dosyasına kaydedildi.");
        } catch (IOException e) {
            System.out.println("Dosya yazma hatası: " + e.getMessage());
        }
    }
}
