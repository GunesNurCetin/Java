import java.io.*;
import java.util.*;

class Account implements Serializable {
    private String username;
    private String password;
    private double balance;

    public Account(String username, String password, double balance) {
        this.username = username;
        this.password = password;
        this.balance = balance;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public double getBalance() { return balance; }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount > balance) return false;
        balance -= amount;
        return true;
    }

    @Override
    public String toString() {
        return username + "," + password + "," + balance;
    }

    public static Account fromString(String line) {
        String[] parts = line.split(",");
        return new Account(parts[0], parts[1], Double.parseDouble(parts[2]));
    }
}

public class BankSystem {
    private static final String FILE_NAME = "accounts.txt";
    private static Map<String, Account> accounts = new HashMap<>();
    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        loadAccounts();
        while (true) {
            System.out.println("\n=== BANKA SISTEMI ===");
            System.out.println("1. Kayıt Ol");
            System.out.println("2. Giriş Yap");
            System.out.println("3. Çıkış");
            System.out.print("Seçiminiz: ");
            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1 -> register();
                case 2 -> login();
                case 3 -> {
                    saveAccounts();
                    System.out.println("Sistemden çıkılıyor...");
                    return;
                }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private static void register() {
        System.out.print("Kullanıcı adı: ");
        String username = input.nextLine();
        if (accounts.containsKey(username)) {
            System.out.println("Bu kullanıcı adı zaten alınmış!");
            return;
        }

        System.out.print("Şifre: ");
        String password = input.nextLine();

        Account newAccount = new Account(username, password, 0.0);
        accounts.put(username, newAccount);
        saveAccounts();
        System.out.println("Kayıt başarılı!");
    }

    private static void login() {
        System.out.print("Kullanıcı adı: ");
        String username = input.nextLine();
        System.out.print("Şifre: ");
        String password = input.nextLine();

        Account acc = accounts.get(username);
        if (acc != null && acc.getPassword().equals(password)) {
            System.out.println("Hoş geldiniz, " + username + "!");
            accountMenu(acc);
        } else {
            System.out.println("Hatalı kullanıcı adı veya şifre!");
        }
    }

    private static void accountMenu(Account acc) {
        while (true) {
            System.out.println("\n=== Hesap Menüsü ===");
            System.out.println("1. Bakiye Görüntüle");
            System.out.println("2. Para Yatır");
            System.out.println("3. Para Çek");
            System.out.println("4. Çıkış");
            System.out.print("Seçiminiz: ");
            int choice = input.nextInt();

            switch (choice) {
                case 1 -> System.out.println("Bakiyeniz: " + acc.getBalance() + " TL");
                case 2 -> {
                    System.out.print("Yatırılacak miktar: ");
                    double amount = input.nextDouble();
                    acc.deposit(amount);
                    System.out.println("Yeni bakiye: " + acc.getBalance() + " TL");
                }
                case 3 -> {
                    System.out.print("Çekilecek miktar: ");
                    double amount = input.nextDouble();
                    if (acc.withdraw(amount))
                        System.out.println("Yeni bakiye: " + acc.getBalance() + " TL");
                    else
                        System.out.println("Yetersiz bakiye!");
                }
                case 4 -> {
                    saveAccounts();
                    return;
                }
                default -> System.out.println("Geçersiz seçim!");
            }
        }
    }

    private static void loadAccounts() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                Account acc = Account.fromString(line);
                accounts.put(acc.getUsername(), acc);
            }
        } catch (IOException e) {
            System.out.println("Hesap verisi bulunamadı, yeni dosya oluşturulacak...");
        }
    }

    private static void saveAccounts() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Account acc : accounts.values()) {
                bw.write(acc.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Hesaplar kaydedilemedi!");
        }
    }
}
