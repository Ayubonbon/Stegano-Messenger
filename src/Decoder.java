
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class Decoder {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter encoded PNG path:");
        String imginput = sc.nextLine();

        File imgfile = new File(imginput);
        BufferedImage image;

        try {
            image = ImageIO.read(imgfile);
        } catch (IOException e) {
            System.out.println("Error: Cannot read image file.");
            return;
        }

        int width = image.getWidth();
        int height = image.getHeight();

        StringBuilder binary = new StringBuilder();

        // EXTRACT LSBs FROM EACH PIXEL
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int pixel = image.getRGB(x, y);

                int red   = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8)  & 0xFF;
                int blue  = pixel & 0xFF;

                // extract LSB of red, green, blue
                binary.append(red   & 1);
                binary.append(green & 1);
                binary.append(blue  & 1);
            }
        }

        // Convert binary  text (every 8 bits)
        StringBuilder message = new StringBuilder();

        for (int i = 0; i + 8 <= binary.length(); i += 8) {

            String byteStr = binary.substring(i, i + 8);
            int ascii = Integer.parseInt(byteStr, 2);

            char ch = (char) ascii;
            message.append(ch);

            // Stop when delimiter reached
            if (message.toString().contains("#END#")) {
                break;
            }
        }

        // Remove delimiter
        String finalMessage = message.toString().replace("#END#", "");

        System.out.println("\nDecoded message:");
        System.out.println(finalMessage);

        sc.close();
    }
}
