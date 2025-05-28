import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LabirentOyunu extends JFrame {
    private int[][] labirent = {
        {1, 1, 1, 1, 1, 1, 1, 1},
        {1, 0, 0, 0, 1, 0, 0, 1},
        {1, 0, 1, 0, 1, 0, 1, 1},
        {1, 0, 1, 0, 0, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 1, 1, 1, 1, 1, 1},
        {1, 0, 0, 0, 0, 0, 0, 2},
    };

    private int oyuncuX = 1, oyuncuY = 1;

    public LabirentOyunu() {
        setTitle("Labirent Oyunu");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new LabirentPanel());
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int yeniX = oyuncuX, yeniY = oyuncuY;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP: yeniY--; break;
                    case KeyEvent.VK_DOWN: yeniY++; break;
                    case KeyEvent.VK_LEFT: yeniX--; break;
                    case KeyEvent.VK_RIGHT: yeniX++; break;
                }
                if (labirent[yeniY][yeniX] != 1) {
                    oyuncuX = yeniX;
                    oyuncuY = yeniY;
                    repaint();
                    if (labirent[yeniY][yeniX] == 2) {
                        JOptionPane.showMessageDialog(null, "Kazandınız!");
                    }
                }
            }
        });
    }

    class LabirentPanel extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int y = 0; y < labirent.length; y++) {
                for (int x = 0; x < labirent[y].length; x++) {
                    if (labirent[y][x] == 1) g.setColor(Color.BLACK);
                    else if (labirent[y][x] == 2) g.setColor(Color.GREEN);
                    else g.setColor(Color.WHITE);
                    g.fillRect(x * 40, y * 40, 40, 40);
                }
            }
            g.setColor(Color.RED);
            g.fillOval(oyuncuX * 40, oyuncuY * 40, 40, 40);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LabirentOyunu().setVisible(true));
    }
}
