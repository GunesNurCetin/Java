import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SifreliMesaj extends JFrame {
    private JTextField mesajGirisi;
    private JTextArea sonucAlani;
    private JButton sifreleButton, cozButton;
    private int anahtar = 3; // Şifreleme anahtarı

    public SifreliMesaj() {
        setTitle("Şifreli Mesajlaşma");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        mesajGirisi = new JTextField();
        sonucAlani = new JTextArea();
        sifreleButton = new JButton("Şifrele");
        cozButton = new JButton("Çöz");

        sifreleButton.addActionListener(e -> {
            String mesaj = mesajGirisi.getText();
            sonucAlani.setText(sifrele(mesaj));
        });

        cozButton.addActionListener(e -> {
            String sifreliMesaj = mesajGirisi.getText();
            sonucAlani.setText(coz(sifreliMesaj));
        });

        add(new JLabel("Mesaj Gir:"));
        add(mesajGirisi);
        add(sifreleButton);
        add(cozButton);
        add(new JLabel("Sonuç:"));
        add(sonucAlani);
    }

    private String sifrele(String mesaj) {
        StringBuilder sb = new StringBuilder();
        for (char ch : mesaj.toCharArray()) {
            sb.append((char) (ch + anahtar));
        }
        return sb.toString();
    }

    private String coz(String sifreliMesaj) {
        StringBuilder sb = new StringBuilder();
        for (char ch : sifreliMesaj.toCharArray()) {
            sb.append((char) (ch - anahtar));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SifreliMesaj().setVisible(true));
    }
}
