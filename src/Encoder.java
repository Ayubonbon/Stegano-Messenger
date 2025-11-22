import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Encoder {

    public static void main(String[] args) {
        /*
            Taking input in the form of a .png file |
            and reading from it.
        */
        Scanner sc = new Scanner(System.in);
        System.out.println("enter the png path : ");
        String imginput = sc.nextLine();

        File imgfile = new File(imginput);
        System.out.println("path received : " + imginput);
        try
        {
            BufferedImage image = ImageIO.read(imgfile);
        }

        catch (IOException e) {
            System.out.println("Error: Cannot read image file.");
            return;
        }
        /*
            Logic for converting text to 8-bit binary value
         */

        System.out.println("enter the text you want to embed in the image : ");

        // using String only to convert plain text to ascii
        String textinput = sc.nextLine();
         /*
            now using stringbuilder to concatenate all the binary outputs
            "string" makes a new string each time something is added and creates too many
            objects but "string builder" is mutable so only 1 object is appended.

           */
        StringBuilder binary = new StringBuilder(5000);

        for (int i = 0; i< textinput.length(); i++) {
            int ascii = textinput.charAt(i);

            for (int bit = 7; bit >= 0; bit--) {
                binary.append((ascii & (1 << bit)) != 0 ? '1' : '0');
            }
            binary.append(' ');
            System.out.print(" " + binary);
        }
        sc.close();
    }
}