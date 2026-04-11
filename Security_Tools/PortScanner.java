/*******************************************************************************
 * PROJECT NAME: Network Port Scanner & Security Analyzer
 * AUTHOR: Güneş Nur ÇETİN (@GunesNurCetin)
 * DATE: 2026-04-11
 * * AÇIKLAMA (TR): 
 * Bu araç, belirli bir IP adresi üzerindeki aktif portları tarayarak hangi servislerin 
 * dış dünyaya açık olduğunu tespit eder. Ağ güvenliği denetimleri için temel bir araçtır.
 * * ÖZELLİKLER (TR):
 * 1. Multi-Port Scanning: Belirlenen port aralığını (örn: 1-1024) hızlıca tarar.
 * 2. Connection Timeout: Yanıt vermeyen portlar için akıllı bekleme süresi yönetimi.
 * 3. Status Reporting: Sadece aktif ve erişilebilir portları raporlar.
 * * KEY JAVA FEATURES (EN):
 * - Socket Networking: Attempting connections via java.net.Socket..
 * - Exception Handling: Managing connection timeouts and unreachable hosts.
 * - Loop Optimization: Efficiently iterating through thousands of potential ports.
 *******************************************************************************/

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class PortScanner {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=========================================");
        System.out.println("   NETWORK PORT SCANNER & ANALYZER       ");
        System.out.println("   DEVELOPER: Gunes Nur CETIN            ");
        System.out.println("=========================================");

        System.out.print("Taranacak IP veya Host (Örn: localhost): ");
        String host = scanner.nextLine();

        System.out.print("Başlangıç Portu: ");
        int startPort = scanner.nextInt();

        System.out.print("Bitiş Portu: ");
        int endPort = scanner.nextInt();

        System.out.println("\nLOG: " + host + " taranıyor... Lütfen bekleyin.\n");

        List<Integer> openPorts = new ArrayList<>();
        int timeout = 200; // Milisaniye cinsinden bekleme süresi

        for (int port = startPort; port <= endPort; port++) {
            if (isPortOpen(host, port, timeout)) {
                System.out.println("[+] AÇIK PORT BULUNDU: " + port);
                openPorts.add(port);
            }
        }

        System.out.println("\n=========================================");
        System.out.println("   TARAMA TAMAMLANDI");
        System.out.println("   Toplam Açık Port: " + openPorts.size());
        if (!openPorts.isEmpty()) {
            System.out.println("   Liste: " + openPorts);
        }
        System.out.println("=========================================");
    }

    /**
     * Belirli bir portun açık olup olmadığını kontrol eder.
     */
    public static boolean isPortOpen(String host, int port, int timeout) {
        try {
            Socket socket = new Socket();
            // Belirlenen timeout süresinde bağlanmayı dener
            socket.connect(new InetSocketAddress(host, port), timeout);
            socket.close();
            return true;
        } catch (Exception e) {
            // Bağlantı reddedildi veya zaman aşımına uğradıysa port kapalıdır
            return false;
        }
    }
}
