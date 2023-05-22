import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter filepath");
        String filePath = scanner.nextLine();
        BufferedImage test = ImageIO.read(new File(filePath));
        //SeamCarving test = new Seam
        // Carving(ImageIO.read(new File("/Users/yuvro/IdeaProjects/SeamCarving/src/frodoswing(1).png")));
        //test.setPixels();
        //System.out.println(Arrays.deepToString(test.pixels));
        //System.out.println(test.image.getRGB(0,0));
        //System.out.println(test.pixels[0][0].getSaturation());
        //System.out.println(Arrays.deepToString(test.energy));
        //test.setDiffs();
        //System.out.println(Arrays.deepToString(test.diffs));
        //test.setCosts();
        //System.out.println(Arrays.deepToString(test.costs));
        //test.setPath();
        //System.out.println(Arrays.deepToString(test.setPath()));
        try {
            File file = new File("output2.png");
            ImageIO.write(scaleColumns(test, 800), "png", file);
        } catch (IOException e) {
            System.out.println("error");

        }
}
    public static BufferedImage scaleColumns(BufferedImage picture, int newWidth) throws IOException {
        int i = 0;
        int firstWidth = picture.getWidth();

        while(i < firstWidth - newWidth){
            SeamCarving carver = new SeamCarving(picture);
            if(i == 800){
                carver.setPixels();
                carver.setDiffs();
                carver.setCosts();
                try {
                    File file = new File("seam.png");
                    ImageIO.write(carver.showSeam(carver.setPath(picture)), "png", file);
                } catch (IOException e) {
                    System.out.println("error");

                }


            }
            else {
                carver.setPixels();
                carver.setDiffs();
                carver.setCosts();
                picture = carver.removeSeam(carver.setPath(picture));
            }
            i++;
        }
        return picture;
    }
}