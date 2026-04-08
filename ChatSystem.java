/*******************************************************************************
  PROJECT NAME: Multi-Client Real-Time Chat Engine
  AUTHOR: [Güneş Nur ÇETİN \ @GunesNurCetin]
  DATE: 2026-04-08
  
  AÇIKLAMA (TR): 
  Bu proje, düşük seviyeli TCP/IP soketlerini kullanarak bir chat sunucusu oluşturur.
  Birden fazla kullanıcının aynı anda bağlanıp birbirine mesaj göndermesini sağlar.
  
  ÖZELLİKLER (TR):
  1. Socket Programming: TCP protokolü üzerinden veri transferi.
  2. Broadcast Mechanism: Bir kullanıcının mesajını diğer tüm bağlı kullanıcılara iletme.
  3. Connection Management: Yeni bağlantıları kabul etme ve kopan bağlantıları temizleme.
  
  KEY JAVA FEATURES (EN):
  - ServerSocket & Socket: Low-level networking API.
  - Multi-threading: Handling each client in a separate thread.
  - PrintWriter & BufferedReader: Stream-based data communication.
*******************************************************************************/

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatSystem {
    // Tüm bağlı istemcileri (clients) tutan thread-safe küme
    private static Set<PrintWriter> clientWriters = ConcurrentHashMap.newKeySet();
    private static final int PORT = 5000;

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("   MULTI-CLIENT CHAT SERVER STARTING...  ");
        System.out.println("=========================================");

        // Sunucuyu ayrı bir thread'de başlat ki ana akış kesilmesin (Opsiyonel simülasyon için)
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("LOG: Sunucu " + PORT + " portunda dinleniyor...");

                while (true) {
                    // Yeni bir bağlantı gelene kadar bloklanır
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("LOG: Yeni bir cihaz bağlandı: " + clientSocket.getRemoteSocketAddress());

                    // Her istemci için yeni bir 'handler' thread başlat
                    new Thread(new ClientHandler(clientSocket)).start();
                }
            } catch (IOException e) {
                System.err.println("SERVER ERROR: " + e.getMessage());
            }
        }).start();

        // GitHub demosu için kısa bir açıklama
        System.out.println("INFO: Bu kod hem sunucu mantığını hem de thread yönetimini içerir.");
        System.out.println("INFO: Gerçek bir chat için 'telnet localhost " + PORT + "' komutuyla bağlanılabilir.");
    }

    // =========================================================================
    // CLIENT HANDLER: Her bir kullanıcıyla ilgilenen işçi sınıf
    // =========================================================================
    private static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Bu istemcinin yazıcısını listeye ekle
                clientWriters.add(out);
                
                out.println("Sisteme hoş geldiniz! Toplam kullanıcı: " + clientWriters.size());

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equalsIgnoreCase("exit")) break;
                    
                    System.out.println("MESAJ ALINDI: " + message);
                    
                    // Alınan mesajı herkese yay (Broadcast)
                    broadcast(message);
                }
            } catch (IOException e) {
                System.err.println("HANDLER ERROR: " + e.getMessage());
            } finally {
                // Bağlantı koptuğunda temizlik yap
                if (out != null) clientWriters.remove(out);
                try { socket.close(); } catch (IOException e) { }
                System.out.println("LOG: Bir kullanıcı ayrıldı.");
            }
        }

        private void broadcast(String message) {
            for (PrintWriter writer : clientWriters) {
                writer.println("CHAT: " + message);
            }
        }
    }
}
