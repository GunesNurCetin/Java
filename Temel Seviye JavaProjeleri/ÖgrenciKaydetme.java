import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class OgrenciNotKaydedici extends JFrame {
    private JTextField isimGirisi, notGirisi;
    private DefaultListModel<String> listeModeli;
    private JList<String> ogrenciListesi;

    private ArrayList<String> ogrenciNotlari;

    public OgrenciNotKaydedici() {
        setTitle("Öğrenci Not Kaydedici");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        ogrenciNotlari = new ArrayList<>();
        listeModeli = new DefaultListModel<>();
        ogrenciListesi = new JList<>(listeModeli);
        add(new JScrollPane(ogrenciListesi), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));

        isimGirisi = new JTextField();
        notGirisi = new JTextField();
        JButton ekleButton = new JButton("Ekle");
        JButton silButton = new JButton("Sil");

        panel.add(new JLabel("İsim:"));
        panel.add(isimGirisi);
        panel.add(new JLabel("Not:"));
        panel.add(notGirisi);

        ekleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String isim = isimGirisi.getText();
                String not = notGirisi.getText();
                if (!isim.isEmpty() && !not.isEmpty()) {
                    ogrenciNotlari.add(isim + " - " + not);
                    listeModeli.addElement(isim + " - " + not);
                    isimGirisi.setText("");
                    notGirisi.setText("");
                }
            }
        });

        silButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int seciliIndex = ogrenciListesi.getSelectedIndex();
                if (seciliIndex != -1) {
                    ogrenciNotlari.remove(seciliIndex);
                    listeModeli.remove(seciliIndex);
                }
            }
        });

        JPanel butonPaneli = new JPanel();
        butonPaneli.add(ekleButton);
        butonPaneli.add(silButton);

        add(panel, BorderLayout.NORTH);
        add(butonPaneli, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OgrenciNotKaydedici().setVisible(true));
    }
}
