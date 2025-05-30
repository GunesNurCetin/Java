import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class YuzTanimaUygulamasi extends JFrame {
    private JLabel goruntuEkrani;
    private CascadeClassifier yuzAlgilayici;
    private VideoCapture kamera;
    private Mat frame;

    public YuzTanimaUygulamasi() {
        setTitle("Yüz Tanıma Uygulaması");
        setSize(640, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        yuzAlgilayici = new CascadeClassifier("haarcascade_frontalface_alt.xml");
        kamera = new VideoCapture(0);
        frame = new Mat();

        goruntuEkrani = new JLabel();
        add(goruntuEkrani);

        new Thread(this::kameraAkisi).start();
    }

    private void kameraAkisi() {
        while (kamera.isOpened()) {
            kamera.read(frame);
            if (!frame.empty()) {
                Mat griFrame = new Mat();
                Imgproc.cvtColor(frame, griFrame, Imgproc.COLOR_BGR2GRAY);
                MatOfRect yuzler = new MatOfRect();
                yuzAlgilayici.detectMultiScale(griFrame, yuzler);

                for (Rect yuz : yuzler.toArray()) {
                    Imgproc.rectangle(frame, yuz.tl(), yuz.br(), new Scalar(0, 255, 0), 2);
                }

                ImageIcon image = new ImageIcon(matToBufferedImage(frame));
                goruntuEkrani.setIcon(image);
            }
        }
    }

    private BufferedImage matToBufferedImage(Mat mat) {
        int width = mat.width(), height = mat.height();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        byte[] data = new byte[width * height * (int) mat.elemSize()];
        mat.get(0, 0, data);
        image.getRaster().setDataElements(0, 0, width, height, data);
        return image;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new YuzTanimaUygulamasi().setVisible(true));
    }
}
