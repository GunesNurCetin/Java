
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class DovizCevirici extends JFrame {
    private JComboBox<String> kaynakDoviz, hedefDoviz;
    private JTextField miktarGirisi;
    private JLabel sonucLabel;
    private HashMap<String, Double> kurBilgileri;

    public DovizCevirici() {
        setTitle("Döviz Çevirici");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        kurBilgileri = new HashMap<>();
        kurBilgileri.put("USD", 1.0);
        kurBilgileri.put("EUR", 0.92);
        kurBilgileri.put("TRY", 32.50);
        kurBilgileri.put("GBP", 0.78);

        kaynakDoviz = new JComboBox<>(kurBilgileri.keySet().toArray(new String[0]));
        hedefDoviz = new JComboBox<>(kurBilgileri.keySet().toArray(new String[0]));
        miktarGirisi = new JTextField();
        sonucLabel = new JLabel("Sonuç: ");

        JButton cevirButton = new JButton("Çevir");
        cevirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double miktar = Double.parseDouble(miktarGirisi.getText());
                String kaynak = (String) kaynakDoviz.getSelectedItem();
                String hedef = (String) hedefDoviz.getSelectedItem();
                double sonuc = (miktar / kurBilgileri.get(kaynak)) * kurBilgileri.get(hedef);
                sonucLabel.setText(String.format("Sonuç: %.2f %s", sonuc, hedef));
            }
        });

        add(new JLabel("Miktar:"));
        add(miktarGirisi);
        add(new JLabel("Kaynak Döviz:"));
        add(kaynakDoviz);
        add(new JLabel("Hedef Döviz:"));
        add(hedefDoviz);
        add(cevirButton);
        add(sonucLabel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DovizCevirici().setVisible(true));
    }
}
