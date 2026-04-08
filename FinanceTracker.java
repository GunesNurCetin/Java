/*******************************************************************************
  PROJECT NAME: Interactive Personal Finance Tracker
  AUTHOR: [Güneş Nur ÇETİN \GunesNurCetin]
  DATE: 2026-04-08
  
  AÇIKLAMA (TR): 
  Bu, terminal üzerinden çalışan, tek dosyadan oluşan interaktif bir finans takip
  sistemidir. Java'nın modern I/O, Streams ve Lambda özelliklerini sergiler.
  
  ÖZELLİKLER (TR):
  1. Harcama Ekle: Girdiğiniz harcamaları yerel bir csv dosyasına kaydeder.
  2. Tümünü Listele: Kayıtlı tüm harcamaları okur ve formatlı bir tablo olarak sunar.
  3. Analiz Raporu: Toplam harcama, en yüksek harcama ve kategori bazlı analiz yapar.
  
  KEY JAVA FEATURES (EN):
  - Java Records: Compact Immutable Data Models (Expense).
  - File I/O (NIO): Reading and Writing data locally (CSV format).
  - Streams & Lambdas: Advanced data filtering and reduction (for report).
  - Interactive CLI: A robust command-line interface using Scanner.
*******************************************************************************/

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

// =============================================================================
// MODEL: Java Record (Compact & Immutable Data Structure)
// =============================================================================
// Harcama verisini temsil eden modern yapı.
record Expense(String date, String category, double amount, String description) {
    // CSV formatına çevirme
    public String toCsvRow() {
        return date + "," + category + "," + amount + "," + description;
    }

    // CSV'den Expense nesnesi oluşturma (Factory Method)
    public static Expense fromCsvRow(String csvRow) {
        String[] data = csvRow.split(",");
        return new Expense(data[0], data[1], Double.parseDouble(data[2]), data[3]);
    }

    @Override
    public String toString() {
        return String.format("| %-12s | %-15s | %8.2f TL | %-25s |", date, category, amount, description);
    }
}

// =============================================================================
// MAIN SYSTEM CLASS
// =============================================================================
public class FinanceTracker {

    private static final String FILE_NAME = "expenses.csv";
    private static final Scanner scanner = new Scanner(System.in);
    private static final Path filePath = Paths.get(FILE_NAME);

    public static void main(String[] args) {
        initializeFile(); // Dosyayı oluştur veya kontrol et

        System.out.println("=================================================");
        System.out.println("   INTERACTIVE PERSONAL FINANCE TRACKER v1.1   ");
        System.out.println("=================================================");

        boolean exit = false;
        while (!exit) {
            printMenu();
            System.out.print("Seçiminiz (1-4): ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addExpense();
                case "2" -> listAllExpenses();
                case "3" -> generateReport();
                case "4" -> {
                    System.out.println("Sistemden çıkılıyor. İyi günler!");
                    exit = true;
                }
                default -> System.err.println("Geçersiz seçim. Lütfen tekrar deneyin.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- ANA MENÜ ---");
        System.out.println("1. Yeni Harcama Ekle");
        System.out.println("2. Tüm Harcamaları Listele");
        System.out.println("3. Harcama Analiz Raporu");
        System.out.println("4. Çıkış");
    }

    // =============================================================================
    // CORE LOGIC 1: File Initialization (NIO)
    // =============================================================================
    private static void initializeFile() {
        try {
            if (Files.notExists(filePath)) {
                Files.createFile(filePath); // Eğer dosya yoksa oluştur
                System.out.println("INFO: Data file (" + FILE_NAME + ") created.");
            }
        } catch (IOException e) {
            System.err.println("ERROR: Could not create data file. " + e.getMessage());
            System.exit(1);
        }
    }

    // =============================================================================
    // CORE LOGIC 2: Add Expense (File I/O)
    // =============================================================================
    private static void addExpense() {
        System.out.println("\n--- YENİ HARCAMA EKLE ---");
        
        System.out.print("Kategori (örn: Gıda, Ulaşım, Kira): ");
        String category = scanner.nextLine();
        
        System.out.print("Tutar (örn: 150.75): ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("ERROR: Geçersiz tutar formatı.");
            return;
        }

        System.out.print("Açıklama: ");
        String description = scanner.nextLine();
        
        String date = LocalDate.now().toString(); // Bugünün tarihini al

        // Record oluştur
        Expense newExpense = new Expense(date, category, amount, description);

        // Dosyaya ekle (Append mode)
        try (BufferedWriter bw = Files.newBufferedWriter(filePath, StandardOpenOption.APPEND)) {
            bw.write(newExpense.toCsvRow());
            bw.newLine();
            System.out.println("SUCCESS: Harcama kaydedildi.");
        } catch (IOException e) {
            System.err.println("ERROR: Harcama dosyaya kaydedilemedi: " + e.getMessage());
        }
    }

    // =============================================================================
    // CORE LOGIC 3: List Expenses (Streams & NIO)
    // =============================================================================
    private static List<Expense> loadExpenses() {
        List<Expense> expenses = new ArrayList<>();
        try {
            // Dosyayı satır satır oku ve Stream'e çevir
            expenses = Files.lines(filePath)
                    .filter(line -> !line.trim().isEmpty()) // Boş satırları atla
                    .map(Expense::fromCsvRow) // Her satırı Expense record'una çevir
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("ERROR: Harcama listesi okunamadı: " + e.getMessage());
        }
        return expenses;
    }

    private static void listAllExpenses() {
        List<Expense> expenses = loadExpenses();

        if (expenses.isEmpty()) {
            System.out.println("\nINFO: Henüz kaydedilmiş harcama yok.");
            return;
        }

        System.out.println("\n" + "-".repeat(65));
        System.out.println(String.format("| %-12s | %-15s | %-11s | %-25s |", "Tarih", "Kategori", "Tutar", "Açıklama"));
        System.out.println("-".repeat(65));
        expenses.forEach(System.out::println);
        System.out.println("-".repeat(65));
    }

    // =============================================================================
    // CORE LOGIC 4: Analysis Report (Streams & Lambdas)
    // =============================================================================
    private static void generateReport() {
        List<Expense> expenses = loadExpenses();

        if (expenses.isEmpty()) {
            System.out.println("\nINFO: Analiz yapacak veri yok.");
            return;
        }

        System.out.println("\n--- HARCAMA ANALİZ RAPORU ---");

        // 1. Toplam Harcama (reduce lambda)
        double totalExpense = expenses.stream()
                .mapToDouble(Expense::amount)
                .sum();

        // 2. En Yüksek Harcama (max lambda)
        Expense maxExpense = expenses.stream()
                .max(Comparator.comparingDouble(Expense::amount))
                .orElse(null);

        System.out.println(">> Toplam Harcama miktarınız: " + String.format("%.2f TL", totalExpense));
        if (maxExpense != null) {
            System.out.println(">> En Yüksek Harcama: " + maxExpense.category() + " (" + maxExpense.amount() + " TL)");
        }

        // 3. Kategori Bazlı Analiz (groupingBy & Streams)
        System.out.println("\n>> Kategori Bazlı Harcamalar:");
        Map<String, Double> categoryTotals = expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::category,
                        Collectors.summingDouble(Expense::amount)
                ));

        categoryTotals.forEach((category, total) -> {
            System.out.println("   - " + String.format("%-15s", category) + ": " + String.format("%8.2f TL", total));
        });
        
        System.out.println("-".repeat(30));
    }
}
