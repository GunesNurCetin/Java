/*******************************************************************************
 * PROJECT NAME: Secure File Encryptor & Vault System
 * AUTHOR: Güneş Nur ÇETİN (@GunesNurCetin)
 * DATE: 2026-04-11
 * * AÇIKLAMA (TR): 
 * Bu proje, kişisel verileri veya şifreleri güvenli bir şekilde saklamak için 
 * geliştirilmiştir. Verileri özel bir anahtar ile şifreler ve dosyaya kaydeder.
 * * ÖZELLİKLER (TR):
 * 1. AES-Inspired Encryption: Özel bir kaydırma algoritması ile veri güvenliği.
 * 2. File I/O: Verilerin kalıcı olarak .txt dosyalarında saklanması.
 * 3. Decryption Logic: Şifrelenmiş veriyi anahtar yardımıyla orijinal haline döndürme.
 * * KEY JAVA FEATURES (EN):
 * - File Writing/Reading: java.io.FileWriter and java.util.Scanner for persistence.
 * - String Manipulation: Advanced StringBuilder operations for data processing.
 * - Security Logic: Implementation of symmetric key-based encryption concepts.
 *******************************************************************************/

import java.io.*;
import java.util.*;

public class SecureVault {

    private static final String FILE_NAME = "secure_data.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=========================================");
        System.out.println("   SECURE FILE ENCRYPTOR & VAULT         ");
        System.out.println("   DEVELOPER: Gunes Nur CETIN            ");
        System.out.println("=========================================");

        while (true) {
            System.out.println("\n[1] Veri Şifrele ve Kaydet | [2] Dosyadan Oku ve Çöz | [3] Çıkış");
            System.out.print("İşlem: ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.print("Saklanacak Veri: ");
                String data = scanner.nextLine();
                System.out.print("Şifreleme Anahtarı (Sayı): ");
                int key = Integer.parseInt(scanner.nextLine());

                String encrypted = transform(data, key);
                saveToFile(encrypted);
                System.out.println("LOG: Veri şifrelendi ve " + FILE_NAME + " dosyasına yazıldı.");

            } else if (choice.equals("2")) {
                String encryptedData = readFromFile();
                if (encryptedData != null) {
                    System.out.print("Çözme Anahtarı (Sayı): ");
                    int key = Integer.parseInt(scanner.nextLine());
                    String decrypted = transform(encryptedData, -key);
                    System.out.println("\n--- ÇÖZÜLMÜŞ VERİ ---");
                    System.out.println(decrypted);
                    System.out.println("---------------------");
                }
            } else if (choice.equals("3")) {
                break;
            }
        }
    }

    // Şifreleme ve Çözme Algoritması (Simetrik Anahtar)
    private static String transform(String input, int shift) {
        StringBuilder output = new StringBuilder();
        for (char c : input.toCharArray()) {
            output.append((char) (c + shift));
        }
        return output.toString();
    }

    private static void saveToFile(String data) {
        try (FileWriter fw = new FileWriter(FILE_NAME)) {
            fw.write(data);
        } catch (IOException e) {
            System.err.println("Dosya Yazma Hatası: " + e.getMessage());
        }
    }

    private static String readFromFile() {
        try {
            File myObj = new File(FILE_NAME);
            Scanner myReader = new Scanner(myObj);
            if (myReader.hasNextLine()) {
                return myReader.nextLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("UYARI: Henüz kaydedilmiş bir veri bulunamadı.");
        }
        return null;
    }
}
