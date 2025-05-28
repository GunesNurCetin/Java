import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

public class DosyaOrganizator {
    private static final String HEDEF_KLASOR = "OrganizeEdilenler";

    public static void main(String[] args) {
        File anaKlasor = new File("C:/Users/Kullanici/Desktop/"); // Düzenlenecek klasör
        organizeDosyalar(anaKlasor);
    }

    public static void organizeDosyalar(File klasor) {
        if (!klasor.isDirectory()) {
            System.out.println("Geçerli bir klasör giriniz.");
            return;
        }

        HashMap<String, String> dosyaTurleri = new HashMap<>();
        dosyaTurleri.put("jpg", "Resimler");
        dosyaTurleri.put("png", "Resimler");
        dosyaTurleri.put("mp4", "Videolar");
        dosyaTurleri.put("pdf", "Belgeler");
        dosyaTurleri.put("txt", "MetinDosyalari");

        for (File dosya : klasor.listFiles()) {
            if (dosya.isFile()) {
                String uzanti = getUzanti(dosya.getName());
                if (dosyaTurleri.containsKey(uzanti)) {
                    File hedefKlasor = new File(klasor, HEDEF_KLASOR + "/" + dosyaTurleri.get(uzanti));
                    hedefKlasor.mkdirs();
                    try {
                        Files.move(dosya.toPath(), Path.of(hedefKlasor.getAbsolutePath(), dosya.getName()), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println(dosya.getName() + " taşındı -> " + hedefKlasor.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println("Organizasyon tamamlandı!");
    }

    private static String getUzanti(String dosyaAdi) {
        int index = dosyaAdi.lastIndexOf('.');
        return (index > 0) ? dosyaAdi.substring(index + 1).toLowerCase() : "";
    }
}
