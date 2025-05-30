import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class HavaDurumuUygulamasi extends JFrame {
    private JTextField sehirGirisi;
    private JTextArea sonucAlani;
    private JButton sorgulaButton;
    private final String API_KEY = "YOUR_API_KEY"; // OpenWeather API anahtarını buraya ekleyin

    public HavaDurumuUygulamasi() {
        setTitle("Hava Durumu Uygulaması");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        sehirGirisi = new JTextField();
        sonucAlani = new JTextArea();
        sonucAlani.setEditable(false);
        sorgulaButton = new JButton("Hava Durumu Getir");

        sorgulaButton.addActionListener(e -> havaDurumuGetir(sehirGirisi.getText()));

        add(new JLabel("Şehir Adı:"), BorderLayout.NORTH);
        add(sehirGirisi, BorderLayout.CENTER);
        add(sorgulaButton, BorderLayout.SOUTH);
        add(new JScrollPane(sonucAlani), BorderLayout.EAST);
    }

    private void havaDurumuGetir(String sehir) {
        try {
            String urlStr = "https://api.openweathermap.org/data/2.5/weather?q=" + sehir + "&appid=" + API_KEY + "&units=metric";
            URL url = new URL(urlStr);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(response.toString());
            String havaDurumu = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
            double sicaklik = jsonObject.getJSONObject("main").getDouble("temp");
            double nem = jsonObject.getJSONObject("main").getDouble("humidity");

            sonucAlani.setText("Hava Durumu: " + havaDurumu + "\nSıcaklık: " + sicaklik + "°C\nNem: " + nem + "%");
        } catch (Exception e) {
            sonucAlani.setText("Hata oluştu! Şehir adını kontrol edin.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HavaDurumuUygulamasi().setVisible(true));
    }
}
