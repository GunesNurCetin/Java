# Dosya Yapısı
# FaturaTakipSistemi/
#│
#├── FaturaTakipSistemi.java
#├── musteriler.txt
#├── urunler.txt
#├── faturalar.txt
#└── README.md





import java.io.*;
import java.time.LocalDate;
import java.util.*;

class Musteri {
    private String id;
    private String adSoyad;

    public Musteri(String id, String adSoyad) {
        this.id = id;
        this.adSoyad = adSoyad;
    }

    public String getId() { return id; }
    public String getAdSoyad() { return adSoyad; }

    @Override
    public String toString() {
        return id + "," + adSoyad;
    }

    public static Musteri fromString(String line) {
        String[] parts = line.split(",");
        return new Musteri(parts[0], parts[1]);
    }
}

class Urun {
    private String id;
    private String ad;
    private double fiyat;

    public Urun(String id, String ad, double fiyat) {
        this.id = id;
        this.ad = ad;
        this.fiyat = fiyat;
    }

    public String getId() { return id; }
    public String getAd() { return ad; }
    public double getFiyat() { return fiyat; }

    @Override
    public String toString() {
        return id + "," + ad + "," + fiyat;
    }

    public static Urun fromString(String line) {
        String[] parts = line.split(",");
        return new Urun(parts[0], parts[1], Double.parseDouble(parts[2]));
    }
}

class Fatura {
    private String id;
    private String musteriId;
    private List<Urun> urunler;
    private LocalDate tarih;

    public Fatura(String id, String musteriId, List<Urun> urunler, LocalDate tarih) {
        this.id = id;
        this.musteriId = musteriId;
        this.urunler = urunler;
        this.tarih = tarih;
    }

    public double getToplamTutar() {
        double toplam = 0;
        for (Urun u : urunler) {
            toplam += u.getFiyat();
        }
        return toplam;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(",").append(musteriId).append(",").append(tarih).append(",");
        for (Urun u : urunler) {
            sb.append(u.getId()).append("|");
        }
        return sb.toString();
    }

    public static Fatura fromString(String line, Map<String, Urun> urunMap) {
        String[] parts = line.split(",");
        String id = parts[0];
        String musteriId = parts[1];
        LocalDate tarih = LocalDate.parse(parts[2]);
        String[] urunIds = parts[3].split("\\|");
        List<Urun> urunler = new ArrayList<>();
        for (String uid : urunIds) {
            if (!uid.isEmpty() && urunMap.containsKey(uid)) {
                urunler.add(urunMap.get(uid));
            }
        }
        return new Fatura(id, musteriId, urunler, tarih);
    }

    public LocalDate getTarih() { return tarih; }
    public String getMusteriId() { return musteriId; }
}

public class FaturaTakipSistemi {
    private static final String MUSTERI_FILE = "musteriler.txt";
    private static final String URUN_FILE = "urunler.txt";
    private static final String FATURA_FILE = "faturalar.txt";

    private static Map<String, Musteri> musteriler = new HashMap<>();
    private static Map<String, Urun> urunler = new HashMap<>();
    private static List<Fatura> faturalar = new ArrayList<>();

    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        loadData();
        while (true) {
            System.out.println("\n=== FATURA TAKİP SİSTEMİ ===");
            System.out.println("1. Müşteri Ekle");
            System.out.println("2. Ürün Ekle");
            System.out.println("3. Fatura Oluştur");
            System.out.println("4. Aylık Rapor");
            System.out.println("5. Çıkış");
            System.out.print("Seçiminiz: ");
            int secim = input.nextInt();
            input.nextLine();

            switch (secim) {
                case 1 -> musteriEkle();
                case 2 -> urunEkle();
                case 3 -> faturaOlustur();
                case 4 -> aylikRapor();
                case 5 -> {
                    saveData();
                    System.out.println("Çıkış yapılıyor...");
                    return;
                }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private static void musteriEkle() {
        System.out.print("Müşteri ID: ");
        String id = input.nextLine();
        System.out.print("Ad Soyad: ");
        String adSoyad = input.nextLine();

        musteriler.put(id, new Musteri(id, adSoyad));
        saveData();
        System.out.println("Müşteri eklendi!");
    }

    private static void urunEkle() {
        System.out.print("Ürün ID: ");
        String id = input.nextLine();
        System.out.print("Ürün Adı: ");
        String ad = input.nextLine();
        System.out.print("Fiyat: ");
        double fiyat = input.nextDouble();
        input.nextLine();

        urunler.put(id, new Urun(id, ad, fiyat));
        saveData();
        System.out.println("Ürün eklendi!");
    }

    private static void faturaOlustur() {
        System.out.print("Fatura ID: ");
        String fid = input.nextLine();
        System.out.print("Müşteri ID: ");
        String mid = input.nextLine();
        List<Urun> secilenUrunler = new ArrayList<>();

        while (true) {
            System.out.print("Ürün ID (bitirmek için q): ");
            String uid = input.nextLine();
            if (uid.equalsIgnoreCase("q")) break;
            if (urunler.containsKey(uid)) {
                secilenUrunler.add(urunler.get(uid));
            } else {
                System.out.println("Ürün bulunamadı!");
            }
        }

        Fatura fatura = new Fatura(fid, mid, secilenUrunler, LocalDate.now());
        faturalar.add(fatura);
        saveData();
        System.out.println("Fatura oluşturuldu. Toplam Tutar: " + fatura.getToplamTutar() + " TL");
    }

    private static void aylikRapor() {
        System.out.print("Yıl (örn: 2025): ");
        int yil = input.nextInt();
        System.out.print("Ay (1-12): ");
        int ay = input.nextInt();
        input.nextLine();

        double toplamGelir = 0;
        for (Fatura f : faturalar) {
            if (f.getTarih().getYear() == yil && f.getTarih().getMonthValue() == ay) {
                toplamGelir += f.getToplamTutar();
            }
        }
        System.out.println("Toplam gelir (" + ay + "/" + yil + "): " + toplamGelir + " TL");
    }

    private static void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(MUSTERI_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                Musteri m = Musteri.fromString(line);
                musteriler.put(m.getId(), m);
            }
        } catch (IOException ignored) {}

        try (BufferedReader br = new BufferedReader(new FileReader(URUN_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                Urun u = Urun.fromString(line);
                urunler.put(u.getId(), u);
            }
        } catch (IOException ignored) {}

        try (BufferedReader br = new BufferedReader(new FileReader(FATURA_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                Fatura f = Fatura.fromString(line, urunler);
                faturalar.add(f);
            }
        } catch (IOException ignored) {}
    }

    private static void saveData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(MUSTERI_FILE))) {
            for (Musteri m : musteriler.values()) {
                bw.write(m.toString());
                bw.newLine();
            }
        } catch (IOException ignored) {}

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(URUN_FILE))) {
            for (Urun u : urunler.values()) {
                bw.write(u.toString());
                bw.newLine();
            }
        } catch (IOException ignored) {}

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FATURA_FILE))) {
            for (Fatura f : faturalar) {
                bw.write(f.toString());
                bw.newLine();
            }
        } catch (IOException ignored) {}
    }
}
