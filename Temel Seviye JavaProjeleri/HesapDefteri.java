import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class HesapDefteri extends JFrame {
    private JTextField miktarGirisi;
    private JComboBox<String> turSecimi;
    private DefaultListModel<String> listeModeli;
    private JList<String> liste;
    private JLabel toplamBakiyeLabel;
    private double toplamBakiye = 0;

    public HesapDefteri() {
        setTitle("Hesap Defteri");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        listeModeli = new DefaultListModel<>();
        liste = new JList<>(listeModeli);
        add(new JScrollPane(liste), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        miktarGirisi = new JTextField();
        turSecimi = new JComboBox<>(new String[]{"Gelir", "Gider"});
        JButton ekleButton = new JButton("Ekle");

        toplamBakiyeLabel = new JLabel("Toplam Bakiye: 0.00");

        panel.add(new JLabel("Miktar:"));
        panel.add(miktarGirisi);
        panel.add(new JLabel("Tür:"));
        panel.add(turSecimi);
        panel.add(ekleButton);
        panel.add(toplamBakiyeLabel);

        ekleButton.addActionListener(e -> {
            try {
                double miktar = Double.parseDouble(miktarGirisi.getText());
                String tur = (String) turSecimi.getSelectedItem();

                if ("Gelir".equals(tur)) {
                    toplamBakiye += miktar;
                } else {
                    toplamBakiye -= miktar;
                }

                listeModeli.addElement(tur + ": " + miktar);
                toplamBakiyeLabel.setText("Toplam Bakiye: " + toplamBakiye);
                miktarGirisi.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Lütfen geçerli bir sayı girin!");
            }
        });

        add(panel, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HesapDefteri().setVisible(true));
    }
}
