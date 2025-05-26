import java.util.Scanner;
import java.util.Random;

public class SayiTahminOyunu {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int rastgeleSayi = random.nextInt(100) + 1; // 1 ile 100 arasında rastgele sayı
        int tahmin;
        int hak = 5;

        System.out.println("1 ile 100 arasında bir sayı tuttum. Tahmin et!");
        System.out.println("Toplam " + hak + " hakkın var.");

        while (hak > 0) {
            System.out.print("Tahminin: ");
            tahmin = scanner.nextInt();

            if (tahmin == rastgeleSayi) {
                System.out.println("Tebrikler! Doğru tahmin ettin. 🎉");
                break;
            } else if (tahmin > rastgeleSayi) {

                System.out.println("Daha küçük bir sayı dene.");
            } else {
                System.out.println("Daha büyük bir sayı dene.");
            }

            hak--;

            if (hak == 0) {
                System.out.println("Tahmin hakkın bitti! Doğru sayı: " + rastgeleSayi);
            }
        }

        scanner.close();
    }
}
