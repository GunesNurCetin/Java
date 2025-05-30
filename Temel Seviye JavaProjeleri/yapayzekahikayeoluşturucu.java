import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class HikayeOlusturucu extends JFrame {
    private JTextField anahtarGirisi;
    private JTextArea sonucAlani;
    private JButton olusturButton;
    private Random random;

    public HikayeOlusturucu() {
        setTitle("Yapay Zeka Hikâye Oluşturucu");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        anahtarGirisi = new JTextField();
        sonucAlani = new JTextArea();
        sonucAlani.setEditable(false);
        olusturButton = new JButton("Hikâye Oluştur");
        random = new Random();

        olusturButton.addActionListener((ActionEvent e) -> hikayeOlustur());

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(new JLabel("Anahtar Kelimeler (örn: kedi, deniz, hazine):"));
        panel.add(anahtarGirisi);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(sonucAlani), BorderLayout.CENTER);
        add(olusturButton, BorderLayout.SOUTH);
    }

    private void hikayeOlustur() {
        String kelimeler = anahtarGirisi.getText();
        if (kelimeler.isEmpty()) {
            sonucAlani.setText("Lütfen en az bir anahtar kelime girin!");
            return;
        }

        String[] hikayeTipleri = {
            "Bir gün, uzak bir diyarda yaşayan {kelime1}, büyülü bir {kelime2} buldu ve macerası başladı...",
            "Karanlık bir gecede, {kelime1} ve {kelime2} kaybolmuştu. Onları kurtaracak kişi kimdi?",
            "{kelime1} hiç beklemediği bir şekilde {kelime2} ile karşılaştı ve büyük bir keşif yaptı!"
        };

        String secilenHikaye = hikayeTipleri[random.nextInt(hikayeTipleri.length)];
        String[] kelimeListesi = kelimeler.split(", ");
        if (kelimeListesi.length >= 2) {
            secilenHikaye = secilenHikaye.replace("{kelime1}", kelimeListesi[0]).replace("{kelime2}", kelimeListesi[1]);
        }

        sonucAlani.setText(secilenHikaye);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HikayeOlusturucu().setVisible(true));
    }
}
