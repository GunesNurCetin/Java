/*******************************************************************************
  PROJECT NAME: Multi-Threaded Order Processing Engine
  AUTHOR: [Güneş Nur ÇETİN \ @GunesNurCetin]
  DATE: 2026-04-08
  
  AÇIKLAMA (TR): 
  Bu proje, yüksek trafikli sistemlerin arkasındaki "kuyruk" ve "işleme" mantığını 
  simüle eder. Java Threads ve Concurrent Collections kullanılarak üretici-tüketici 
  (Producer-Consumer) modelini sergiler.
  
  ÖZELLİKLER (TR):
  1. Asenkron İşleme: Siparişler ana akışı bozmadan arka planda işlenir.
  2. Thread-Safe Kuyruk: Birden fazla işçinin aynı anda güvenli çalışması.
  3. Gerçekçi Simülasyon: Rastgele işlem süreleri ve durum güncellemeleri.
  
  KEY JAVA FEATURES (EN):
  - Multithreading: Using the Thread class and Sleep for async simulation.
  - BlockingQueue: Thread-safe data structures for task management.
  - Lambda Expressions: Streamlined event handling.
*******************************************************************************/

import java.util.concurrent.*;
import java.util.*;

// =============================================================================
// MODEL: Order (Sipariş Verisi)
// =============================================================================
class Order {
    private final String id;
    private final String productName;
    private String status;

    public Order(String productName) {
        this.id = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.productName = productName;
        this.status = "BEKLEMEDE";
    }

    public void setStatus(String status) { this.status = status; }
    public String getId() { return id; }
    
    @Override
    public String toString() {
        return String.format("[%s] %-20s | Durum: %s", id, productName, status);
    }
}

// =============================================================================
// WORKER: OrderProcessor (İşleyici Thread)
// =============================================================================
class OrderProcessor implements Runnable {
    private final BlockingQueue<Order> queue;
    private final String workerName;

    public OrderProcessor(BlockingQueue<Order> queue, String workerName) {
        this.queue = queue;
        this.workerName = workerName;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Kuyruktan sipariş al (eğer boşsa bekler)
                Order order = queue.take();
                order.setStatus("İŞLENİYOR (" + workerName + ")");
                System.out.println(">> " + workerName + " isleme aldi: " + order.getId());

                // İşleme simülasyonu (Rastgele 1-3 saniye)
                Thread.sleep(new Random().nextInt(2000) + 1000);

                order.setStatus("TAMAMLANDI");
                System.out.println("SUCCESS: " + order.getId() + " paketlendi ve kargoya verildi.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// =============================================================================
// CORE SYSTEM: Main Engine
// =============================================================================
public class OrderSystem {
    public static void main(String[] args) throws InterruptedException {
        // Thread-safe kuyruk yapısı
        BlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();

        System.out.println("=========================================");
        System.out.println("   ASYNC ORDER PROCESSING ENGINE v1.0   ");
        System.out.println("=========================================\n");

        // 1. İşçileri (Worker Threads) Başlat
        Thread worker1 = new Thread(new OrderProcessor(orderQueue, "İşçi-1"));
        Thread worker2 = new Thread(new OrderProcessor(orderQueue, "İşçi-2"));
        
        // Daemon threads program bitince otomatik kapanır
        worker1.setDaemon(true);
        worker2.setDaemon(true);
        
        worker1.start();
        worker2.start();

        // 2. Sipariş Üretimi (Producer)
        String[] items = {"Laptop", "Akıllı Telefon", "Monitör", "Klavye", "Mouse Pad"};
        
        for (String item : items) {
            Order newOrder = new Order(item);
            System.out.println("YENİ SİPARİŞ GELDİ: " + newOrder);
            orderQueue.put(newOrder); // Kuyruğa ekle
            Thread.sleep(500); // Siparişler arası kısa bekleme
        }

        // Sistem bir süre çalışsın ki çıktıları görelim
        System.out.println("\n--- Tüm siparişler kuyruğa eklendi. İşlemler devam ediyor... ---\n");
        Thread.sleep(8000); 
        
        System.out.println("\n=========================================");
        System.out.println("      SİSTEM SİMÜLASYONU SONLANDI       ");
        System.out.println("=========================================");
    }
}
