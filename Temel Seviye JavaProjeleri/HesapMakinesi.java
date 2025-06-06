import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HesapMakinesi extends JFrame implements ActionListener {

    private final JTextField ekran;
    private double birinciSayi = 0;
    private String islem = "";
    private boolean yeniIslem = true;

    public HesapMakinesi() {
        super("Hesap Makinesi");

        ekran = new JTextField("0");
        ekran.setHorizontalAlignment(SwingConstants.RIGHT);
        ekran.setEditable(false);
        ekran.setFont(new Font("SansSerif", Font.PLAIN, 24));
        ekran.setPreferredSize(new Dimension(300, 60));

        String[] tuslar = {"7", "8", "9", "/", "4", "5", "6", "*",
                           "1", "2", "3", "-", "0", "C", "=", "+" };
        JPanel panelTus = new JPanel(new GridLayout(4, 4, 8, 8));

        for (String yazi : tuslar) {
            JButton btn = new JButton(yazi);
            btn.setFont(new Font("SansSerif", Font.BOLD, 20));
            btn.setFocusPainted(false);
            btn.setBackground(Color.LIGHT_GRAY);
            btn.setForeground(Color.BLACK);
            btn.addActionListener(this);
            panelTus.add(btn);
        }

        setLayout(new BorderLayout(8, 8));
        add(ekran, BorderLayout.NORTH);
        add(panelTus, BorderLayout.CENTER);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String komut = e.getActionCommand();

        if (komut.matches("[0-9]")) {
            ekran.setText(yeniIslem ? komut : ekran.getText() + komut);
            yeniIslem = false;
        } else if (komut.equals("C")) {
            ekran.setText("0");
            birinciSayi = 0;
            islem = "";
            yeniIslem = true;
        } else if (komut.equals("=")) {
            hesaplaVeGoster();
            islem = "";
            yeniIslem = true;
        } else {
            if (!islem.isEmpty()) {
                hesaplaVeGoster();
            }
            birinciSayi = Double.parseDouble(ekran.getText());
            islem = komut;
            yeniIslem = true;
        }
    }

    private void hesaplaVeGoster() {
        double ikinciSayi = Double.parseDouble(ekran.getText());
        double sonuc = switch (islem) {
            case "+" -> birinciSayi + ikinciSayi;
            case "-" -> birinciSayi - ikinciSayi;
            case "*" -> birinciSayi * ikinciSayi;
            case "/" -> (ikinciSayi == 0) ? 0 : birinciSayi / ikinciSayi;
            default -> ikinciSayi;
        };

        ekran.setText(sonuc == (long) sonuc ? String.valueOf((long) sonuc) : String.valueOf(sonuc));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HesapMakinesi().setVisible(true));
    }
}
