import java.io.*;
import java.time.LocalDate;
import java.util.*;

class Kitap {
    private String id;
    private String ad;
    private String yazar;
    private boolean oduncAlindi;

    public Kitap(String id, String ad, String yazar) {
        this.id = id;
        this.ad = ad;
        this.yazar = yazar;
        this.oduncAlindi = false;
    }

    public String getId() { return id; }
    public String getAd() { return ad; }
    public String getYazar() { return yazar; }
    public boolean isOduncAlindi() { return oduncAlindi; }
    public void setOduncAlindi(boolean durum) { this.oduncAlindi = durum; }

    @Override
    public String toString() {
        return id + "," + ad + "," + yazar + "," + oduncAlindi;
    }

    public static Kitap fromString(String line) {
        String[] p = line.split(",");
        Kitap k = new Kitap(p[0], p[1], p[2]);
        k.setOduncAlindi(Boolean.parseBoolean(p[3]));
        return k;
    }
}

class Uye {
    private String id;
    private String adSoyad;

    public Uye(String id, String adSoyad) {
        this.id = id;
        this.adSoyad = adSoyad;
    }

    public String getId() { return id; }
    public String getAdSoyad() { return adSoyad; }

    @Override
    public String toString() {
        return id + "," + adSoyad;
    }

    public static Uye fromString(String line) {
        String[] p = line.split(",");
        return new Uye(p[0], p[1]);
    }
}

class Odunc {
    private String uyeId;
    private String kitapId;
    private LocalDate tarih;

    public Odunc(String uyeId, String kitapId, LocalDate tarih) {
        this.uyeId = uyeId;
        this.kitapId = kitapId;
        this.tarih = tarih;
    }

    public String getUyeId() { return uyeId; }
    public String getKitapId() { return kitapId; }
    public LocalDate getTarih() { return tarih; }

    @Override
    public String toString() {
        return uyeId + "," + kitapId + "," + tarih;
    }

    public static Odunc fromString(String line) {
        String[] p = line.split(",");
        return new Odunc(p[0], p[1], LocalDate.parse(p[2]));
    }
}

public class KutuphaneYonetimSistemi {
    private static final String KITAP_FILE = "kitaplar.txt";
    private static final String UYE_FILE = "uyeler.txt";
    private static final String ODUNC_FILE = "oduncler.txt";

    private static Map<String, Kitap> kitaplar = new HashMap<>();
    private static Map<String, Uye> uyeler = new HashMap<>();
    private static List<Odunc> oduncler = new ArrayList<>();

    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        loadData();

        while (true) {
            System.out.println("\n=== 📚 KÜTÜPHANE YÖNETİM SİSTEMİ ===");
            System.out.println("1. Kitap Ekle");
            System.out.println("2. Üye Ekle");
            System.out.println("3. Kitap Ödünç Ver");
            System.out.println("4. Kitap İade Al");
            System.out.println("5. Tüm Kitapları Listele");
            System.out.println("6. Çıkış");
            System.out.print("Seçiminiz: ");
            int secim = input.nextInt();
            input.nextLine();

            switch (secim) {
                case 1 -> kitapEkle();
                case 2 -> uyeEkle();
                case 3 -> oduncVer();
                case 4 -> iadeAl();
                case 5 -> kitapListele();
                case 6 -> {
                    saveData();
                    System.out.println("📦 Veriler kaydedildi. Çıkılıyor...");
                    return;
                }
                default -> System.out.println("❌ Geçersiz seçim!");
            }
        }
    }

    private static void kitapEkle() {
        System.out.print("Kitap ID: ");
        String id = input.nextLine();
        System.out.print("Kitap Adı: ");
        String ad = input.nextLine();
        System.out.print("Yazar: ");
        String yazar = input.nextLine();

        kitaplar.put(id, new Kitap(id, ad, yazar));
        saveData();
        System.out.println("✅ Kitap eklendi!");
    }

    private static void uyeEkle() {
        System.out.print("Üye ID: ");
        String id = input.nextLine();
        System.out.print("Ad Soyad: ");
        String adSoyad = input.nextLine();

        uyeler.put(id, new Uye(id, adSoyad));
        saveData();
        System.out.println("✅ Üye eklendi!");
    }

    private static void oduncVer() {
        System.out.print("Üye ID: ");
        String uyeId = input.nextLine();
        System.out.print("Kitap ID: ");
        String kitapId = input.nextLine();

        if (!uyeler.containsKey(uyeId)) {
            System.out.println("❌ Üye bulunamadı!");
            return;
        }
        if (!kitaplar.containsKey(kitapId)) {
            System.out.println("❌ Kitap bulunamadı!");
            return;
        }

        Kitap k = kitaplar.get(kitapId);
        if (k.isOduncAlindi()) {
            System.out.println("⚠️ Kitap zaten ödünçte!");
            return;
        }

        k.setOduncAlindi(true);
        oduncler.add(new Odunc(uyeId, kitapId, LocalDate.now()));
        saveData();
        System.out.println("📖 Kitap ödünç verildi!");
    }

    private static void iadeAl() {
        System.out.print("Kitap ID: ");
        String kitapId = input.nextLine();

        if (!kitaplar.containsKey(kitapId)) {
            System.out.println("❌ Kitap bulunamadı!");
            return;
        }

        Kitap k = kitaplar.get(kitapId);
        if (!k.isOduncAlindi()) {
            System.out.println("⚠️ Kitap zaten kütüphanede!");
            return;
        }

        k.setOduncAlindi(false);
        saveData();
        System.out.println("✅ Kitap iade alındı!");
    }

    private static void kitapListele() {
        System.out.println("\n--- KİTAP LİSTESİ ---");
        for (Kitap k : kitaplar.values()) {
            String durum = k.isOduncAlindi() ? "Ödünçte" : "Mevcut";
            System.out.println(k.getId() + " - " + k.getAd() + " (" + k.getYazar() + ") [" + durum + "]");
        }
    }

    private static void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(KITAP_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                Kitap k = Kitap.fromString(line);
                kitaplar.put(k.getId(), k);
            }
        } catch (IOException ignored) {}

        try (BufferedReader br = new BufferedReader(new FileReader(UYE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                Uye u = Uye.fromString(line);
                uyeler.put(u.getId(), u);
            }
        } catch (IOException ignored) {}

        try (BufferedReader br = new BufferedReader(new FileReader(ODUNC_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                Odunc o = Odunc.fromString(line);
                oduncler.add(o);
            }
        } catch (IOException ignored) {}
    }

    private static void saveData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(KITAP_FILE))) {
            for (Kitap k : kitaplar.values()) {
                bw.write(k.toString());
                bw.newLine();
            }
        } catch (IOException ignored) {}

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(UYE_FILE))) {
            for (Uye u : uyeler.values()) {
                bw.write(u.toString());
                bw.newLine();
            }
        } catch (IOException ignored) {}

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ODUNC_FILE))) {
            for (Odunc o : oduncler) {
                bw.write(o.toString());
                bw.newLine();
            }
        } catch (IOException ignored) {}
    }
}
