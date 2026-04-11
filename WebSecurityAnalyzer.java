/*******************************************************************************
 * PROJECT NAME: Web Security Header & Protocol Analyzer
 * AUTHOR: Güneş Nur ÇETİN (@GunesNurCetin)
 * DATE: 2026-04-11
 * * AÇIKLAMA (TR): 
 * Bu araç, hedef bir web sitesinin HTTP başlıklarını analiz ederek güvenlik 
 * açıklarını raporlar. Web protokolleri ve siber güvenlik denetimi için tasarlanmıştır.
 * * ÖZELLİKLER (TR):
 * 1. Protocol Analysis: Hedef URL üzerinden HTTP/HTTPS yanıt başlıklarını çekme.
 * 2. Security Audit: X-Frame-Options, HSTS ve Server bilgisi gibi kritik verileri denetleme. 
 * 3. Risk Assessment: Eksik güvenlik başlıklarını kullanıcıya uyarı olarak sunma.
 * * KEY JAVA FEATURES (EN):
 * - HttpURLConnection: High-level networking for web requests.
 * - URL Class: Managing and validating web addresses.
 * - Map Iteration: Processing complex header data structures.
 *******************************************************************************/

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class WebSecurityAnalyzer {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=========================================");
        System.out.println("   WEB SECURITY HEADER ANALYZER          ");
        System.out.println("   DEVELOPER: Gunes Nur CETIN            ");
        System.out.println("=========================================");

        System.out.print("Analiz edilecek URL (Örn: https://www.google.com): ");
        String urlString = scanner.nextLine();

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);

            System.out.println("\nLOG: Bağlantı kuruluyor: " + urlString);
            int responseCode = connection.getResponseCode();
            System.out.println("STATUS: HTTP " + responseCode);

            Map<String, List<String>> headers = connection.getHeaderFields();

            System.out.println("\n--- GÜVENLİK ANALİZ RAPORU ---");
            checkHeader(headers, "X-Frame-Options", "Clickjacking koruması");
            checkHeader(headers, "Strict-Transport-Security", "HSTS (HTTPS zorunluluğu)");
            checkHeader(headers, "X-XSS-Protection", "XSS Filtresi");
            checkHeader(headers, "Content-Security-Policy", "CSP Politikası");
            checkHeader(headers, "Server", "Sunucu Bilgisi (Gizlenmeli!)");

            System.out.println("\nLOG: Analiz başarıyla tamamlandı.");

        } catch (Exception e) {
            System.err.println("HATA: URL'ye erişilemedi! " + e.getMessage());
        }
        System.out.println("=========================================");
    }

    private static void checkHeader(Map<String, List<String>> headers, String headerName, String description) {
        if (headers.containsKey(headerName)) {
            System.out.println("[+] " + headerName + ": MEVCUT (" + description + ")");
        } else {
            System.err.println("[-] " + headerName + ": EKSİK! (" + description + ")");
        }
    }
}
