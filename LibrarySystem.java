/*******************************************************************************
  PROJECT NAME: Smart Library & Reservation Engine
  AUTHOR: [Güneş Nur ÇETİN \ @GunesNurCetin]
  DATE: 2026-04-08
  
  AÇIKLAMA (TR): 
  Bu proje, OOP prensiplerini (Encapsulation, Inheritance, Polymorphism) 
  tek bir dosyada sergileyen profesyonel bir kütüphane yönetim sistemidir.
  
  ÖZELLİKLER (TR):
  1. Gelişmiş Üyelik Sistemi: Üye türlerine göre farklı ödünç alma limitleri.
  2. Özel Hata Yönetimi: Kitap bulunamadığında veya limit aşıldığında özel exception fırlatma.
  3. Koleksiyon Yönetimi: Kitapları ve üyeleri Map/List yapılarında hızlı eşleme.
  
  KEY JAVA FEATURES (EN):
  - Custom Exceptions: Handling business logic errors gracefully.
  - Enums: Representing constant states like Member Types and Book Status.
  - Collections Framework: Using HashMap and ArrayList for high-speed data retrieval.
*******************************************************************************/

import java.util.*;
import java.time.LocalDateTime;

// =============================================================================
// ENUMS & CUSTOM EXCEPTIONS
// =============================================================================
enum MemberType { 
    STUDENT(2), FACULTY(5), REGULAR(1); 
    final int limit;
    MemberType(int limit) { this.limit = limit; }
}

class LibraryException extends Exception {
    public LibraryException(String message) { super(message); }
}

// =============================================================================
// MODELS: Book & Member
// =============================================================================
class Book {
    private String isbn;
    private String title;
    private boolean isBorrowed;

    public Book(String isbn, String title) {
        this.isbn = isbn;
        this.title = title;
        this.isBorrowed = false;
    }

    // Getters & Setters
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public boolean isBorrowed() { return isBorrowed; }
    public void setBorrowed(boolean borrowed) { isBorrowed = borrowed; }

    @Override
    public String toString() { return "[" + isbn + "] " + title + (isBorrowed ? " (ÖDÜNÇ VERİLDİ)" : " (MEVCUT)"); }
}

class Member {
    private String id;
    private String name;
    private MemberType type;
    private List<Book> borrowedBooks = new ArrayList<>();

    public Member(String id, String name, MemberType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public boolean canBorrow() { return borrowedBooks.size() < type.limit; }
    public void addBook(Book book) { borrowedBooks.add(book); }
    public void returnBook(Book book) { borrowedBooks.remove(book); }
    public String getName() { return name; }
    public List<Book> getBorrowedBooks() { return borrowedBooks; }
}

// =============================================================================
// CORE SYSTEM: Library Manager
// =============================================================================
public class LibrarySystem {
    private Map<String, Book> bookCatalog = new HashMap<>();
    private Map<String, Member> members = new HashMap<>();

    public void addBook(Book book) { bookCatalog.put(book.getIsbn(), book); }
    public void registerMember(Member member) { members.put(member.getName(), member); }

    public void processLoan(String memberName, String isbn) throws LibraryException {
        Book book = bookCatalog.get(isbn);
        Member member = members.get(memberName);

        if (book == null) throw new LibraryException("HATA: Kitap katalogda bulunamadı!");
        if (member == null) throw new LibraryException("HATA: Üye kaydı bulunamadı!");
        if (book.isBorrowed()) throw new LibraryException("HATA: Kitap zaten başkasında!");
        if (!member.canBorrow()) throw new LibraryException("HATA: Üye limitine ulaştı!");

        // İşlemi Gerçekleştir
        book.setBorrowed(true);
        member.addBook(book);
        System.out.println("ONAY: '" + book.getTitle() + "' kitabı " + member.getName() + " kullanıcısına verildi.");
    }

    public void showStatus() {
        System.out.println("\n--- KÜTÜPHANE GÜNCEL DURUMU ---");
        bookCatalog.values().forEach(System.out::println);
    }

    // =============================================================================
    // MAIN EXECUTION
    // =============================================================================
    public static void main(String[] args) {
        LibrarySystem library = new LibrarySystem();

        // 1. Veri Girişi
        library.addBook(new Book("101", "Effective Java"));
        library.addBook(new Book("102", "Clean Code"));
        library.addBook(new Book("103", "Design Patterns"));

        library.registerMember(new Member("M1", "Ahmet", MemberType.REGULAR)); // Limit: 1
        library.registerMember(new Member("M2", "Ayşe (Hoca)", MemberType.FACULTY)); // Limit: 5

        System.out.println("=========================================");
        System.out.println("   SMART LIBRARY MANAGEMENT SYSTEM   ");
        System.out.println("=========================================");

        try {
            // Senaryo A: Başarılı Ödünç Alma
            library.processLoan("Ahmet", "101");

            // Senaryo B: Limit Aşımı Testi (Ahmet REGULAR olduğu için 2. kitabı alamaz)
            System.out.println("\n--- Limit Testi Yapılıyor ---");
            library.processLoan("Ahmet", "102");

        } catch (LibraryException e) {
            System.err.println(e.getMessage());
        }

        try {
            // Senaryo C: Yetkili Kullanıcı (Faculty) Testi
            System.out.println("\n--- Yetkili Kullanıcı Testi ---");
            library.processLoan("Ayşe (Hoca)", "102");
            library.processLoan("Ayşe (Hoca)", "103");
        } catch (LibraryException e) {
            System.err.println(e.getMessage());
        }

        // Final Durumu Göster
        library.showStatus();
    }
}
