import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Encoder {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
         /*
            Logic for converting text to 8-bit binary value
         */

        System.out.println("enter the text you want to embed in the image : ");

        // using String only to convert plain text to ascii
        String textinput = sc.nextLine();
        // Append a delimiter to mark end of message
        textinput += "#END#";
         /*
            now using stringbuilder to concatenate all the binary outputs
            "string" makes a new string each time something is added and creates too many
            objects but "string builder" is mutable so only 1 object is appended.

           */
        StringBuilder binary = new StringBuilder();

        for (int i = 0; i < textinput.length(); i++) {
            int ascii = textinput.charAt(i);

            for (int bit = 7; bit >= 0; bit--) {
                binary.append((ascii & (1 << bit)) != 0 ? '1' : '0');
            }

        }

        System.out.println(binary);



        /*
            Taking input in the form of a .png file |
            and reading from it.
        */
        System.out.println("enter the png path : ");
        String imginput = sc.nextLine();

        File imgfile = new File(imginput);
        System.out.println("path received : " + imginput);

        BufferedImage image ;
        try {
            image = ImageIO.read(imgfile);

        } catch (IOException e) {
            System.out.println("Error: Cannot read image file.");
            return;
        }

        int width = image.getWidth();
        int height = image.getHeight();

        int totalPixels = width * height * 3;

        if (binary.length() > totalPixels) {
            System.out.println("Error: Message too large for this image!");
            return;
        }

        System.out.println("Encoding message...");

        int dataIndex = 0;

        // Iterate over each pixel
        outer:
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                if (dataIndex >= binary.length()) break outer;

                int pixel = image.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xFF;

                int red   = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8)  & 0xFF;
                int blue  = pixel & 0xFF;

                // Modify RED LSB
                if (dataIndex < binary.length()) {
                    int bit = binary.charAt(dataIndex++) - '0';
                    red = (red & 0xFE) | bit;
                }

                // Modify GREEN LSB
                if (dataIndex < binary.length()) {
                    int bit = binary.charAt(dataIndex++) - '0';
                    green = (green & 0xFE) | bit;
                }

                // Modify BLUE LSB
                if (dataIndex < binary.length()) {
                    int bit = binary.charAt(dataIndex++) - '0';
                    blue = (blue & 0xFE) | bit;
                }

                int newPixel = (alpha << 24) | (red << 16) | (green << 8) | blue;

                image.setRGB(x, y, newPixel);
            }
        }

        // Save output
        try {
            File output = new File("encoded_output.png");
            ImageIO.write(image, "png", output);
            System.out.println("Encoding complete! ");
        } catch (IOException e) {
            System.out.println("Error writing encoded image.");
        }
        sc.close();
    }
}