import java.io.*;
import java.util.*;

class Car {
    private String id;
    private String brand;
    private String model;
    private double dailyPrice;
    private boolean available;

    public Car(String id, String brand, String model, double dailyPrice, boolean available) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.dailyPrice = dailyPrice;
        this.available = available;
    }

    public String getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public double getDailyPrice() { return dailyPrice; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return id + "," + brand + "," + model + "," + dailyPrice + "," + available;
    }

    public static Car fromString(String line) {
        String[] parts = line.split(",");
        return new Car(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]), Boolean.parseBoolean(parts[4]));
    }
}

public class CarRentalSystem {
    private static final String FILE_NAME = "cars.txt";
    private static Map<String, Car> cars = new LinkedHashMap<>();
    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        loadCars();
        while (true) {
            System.out.println("\n=== ARAÇ KİRALAMA SİSTEMİ ===");
            System.out.println("1. Araç Listesi");
            System.out.println("2. Araç Kirala");
            System.out.println("3. Araç İade Et");
            System.out.println("4. Araç Ekle (Yönetici)");
            System.out.println("5. Çıkış");
            System.out.print("Seçiminiz: ");
            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1 -> listCars();
                case 2 -> rentCar();
                case 3 -> returnCar();
                case 4 -> addCar();
                case 5 -> {
                    saveCars();
                    System.out.println("Sistemden çıkılıyor...");
                    return;
                }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private static void listCars() {
        System.out.println("\n--- Araç Listesi ---");
        for (Car c : cars.values()) {
            System.out.printf("ID: %s | %s %s | Günlük: %.2f TL | Durum: %s\n",
                    c.getId(), c.getBrand(), c.getModel(), c.getDailyPrice(),
                    c.isAvailable() ? "Uygun" : "Kirada");
        }
    }

    private static void rentCar() {
        System.out.print("Kiralamak istediğiniz araç ID'si: ");
        String id = input.nextLine();
        Car car = cars.get(id);

        if (car == null) {
            System.out.println("Bu ID ile araç bulunamadı!");
            return;
        }
        if (!car.isAvailable()) {
            System.out.println("Bu araç şu anda kirada!");
            return;
        }

        System.out.print("Kaç gün kiralamak istiyorsunuz? ");
        int days = input.nextInt();
        input.nextLine();

        double total = car.getDailyPrice() * days;
        System.out.println("Toplam ücret: " + total + " TL");
        System.out.print("Kiralamayı onaylıyor musunuz? (E/H): ");
        String confirm = input.nextLine();

        if (confirm.equalsIgnoreCase("E")) {
            car.setAvailable(false);
            saveCars();
            System.out.println("Araç başarıyla kiralandı!");
        } else {
            System.out.println("İşlem iptal edildi.");
        }
    }

    private static void returnCar() {
        System.out.print("İade etmek istediğiniz araç ID'si: ");
        String id = input.nextLine();
        Car car = cars.get(id);

        if (car == null) {
            System.out.println("Bu ID ile araç bulunamadı!");
            return;
        }
        if (car.isAvailable()) {
            System.out.println("Bu araç zaten sistemde mevcut (kirada değil)!");
            return;
        }

        car.setAvailable(true);
        saveCars();
        System.out.println("Araç başarıyla iade edildi!");
    }

    private static void addCar() {
        System.out.print("Araç ID: ");
        String id = input.nextLine();
        if (cars.containsKey(id)) {
            System.out.println("Bu ID zaten mevcut!");
            return;
        }

        System.out.print("Marka: ");
        String brand = input.nextLine();
        System.out.print("Model: ");
        String model = input.nextLine();
        System.out.print("Günlük ücret: ");
        double price = input.nextDouble();
        input.nextLine();

        Car newCar = new Car(id, brand, model, price, true);
        cars.put(id, newCar);
        saveCars();
        System.out.println("Yeni araç eklendi!");
    }

    private static void loadCars() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                Car car = Car.fromString(line);
                cars.put(car.getId(), car);
            }
        } catch (IOException e) {
            System.out.println("Araç verisi bulunamadı, yeni dosya oluşturulacak...");
        }
    }

    private static void saveCars() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Car c : cars.values()) {
                bw.write(c.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Araç verileri kaydedilemedi!");
        }
    }
}

