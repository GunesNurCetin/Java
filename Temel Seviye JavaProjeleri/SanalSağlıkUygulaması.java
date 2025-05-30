import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;

public class SaglikTakipUygulamasi extends JFrame {
    private JTextField suGirisi, adimGirisi, uykuGirisi;
    private JTextArea sonucAlani;
    private JButton ekleButton, kaydetButton;
    private static final String DOSYA_ADI = "saglik_verileri.txt";
    private ArrayList<String> saglikVerileri = new ArrayList<>();

    public SaglikTakipUygulamasi() {
        setTitle("Sağlık Takip Uygulaması");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        suGirisi = new JTextField();
        adimGirisi = new JTextField();
        uykuGirisi = new JTextField();
        ekleButton = new JButton("Veri Ekle");
        kaydetButton = new JButton("Kaydet");

        panel.add(new JLabel("Su tüketimi (litre):"));
        panel.add(suGirisi);
        panel.add(new JLabel("Adım sayısı:"));
        panel.add(adimGirisi);
        panel.add(new JLabel("Uyku süresi (saat):"));
        panel.add(uykuGirisi);
        panel.add(ekleButton);
        panel.add(kaydetButton);

        sonucAlani = new JTextArea();
        sonucAlani.setEditable(false);
        add(new JScrollPane(sonucAlani), BorderLayout.CENTER);
        add(panel, BorderLayout.NORTH);

        ekleButton.addActionListener(e -> veriEkle());
        kaydetButton.addActionListener(e -> verileriKaydet());
    }

    private void veriEkle() {
        String veri = "Su: " + suGirisi.getText() + "L, Adım: " + adimGirisi.getText() + ", Uyku: " + uykuGirisi.getText() + " saat";
        saglikVerileri.add(veri);
        sonucAlani.append(veri + "\n");
    }

    private void verileriKaydet() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DOSYA_ADI))) {
            for (String veri : saglikVerileri) {
                writer.println(veri);
            }
            JOptionPane.showMessageDialog(this, "Veriler kaydedildi!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SaglikTakipUygulamasi().setVisible(true));
    }
}
