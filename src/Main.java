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
import java.nio.Buffer;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter filepath");
        String filePath = scanner.nextLine();
        BufferedImage test = ImageIO.read(new File(filePath));
        System.out.println("Current Dimensions: " + test.getWidth() + " x " + test.getHeight());
        System.out.println("Enter the desired new width: ");
        int newWidth = scanner.nextInt();
        System.out.println("Enter the desired new height: ");
        int newHeight = scanner.nextInt();
        try {
            File file = new File("output.png");
            //ImageIO.write(scaleRows(test, 130),  "png", file);
            ImageIO.write(scaleRows(scaleColumns(test, newWidth), newHeight),"png",file);
        } catch (IOException e) {
            System.out.println("error");

        }
}
    public static BufferedImage scaleColumns(BufferedImage picture, int newWidth) throws IOException {
        int i = 0;
        int firstWidth = picture.getWidth();
        while(i < firstWidth - newWidth){
            SeamCarving carver = new SeamCarving(picture);
//            if(i == 0){
//                carver.setPixels();
//                carver.setDiffs();
//                carver.setCosts();
//                //System.out.println(Arrays.deepToString(carver.setPath(picture)));
//                try {
//                    File file = new File("seam.png");
//                    //picture = carver.removeSeam(carver.setPath(picture));
//                    ImageIO.write(carver.showSeam(carver.setPath(picture)), "png", file);
//                } catch (IOException e) {
//                    System.out.println("error");
//                }
//            }
           // else {
                carver.setPixels();
                carver.setDiffs();
                carver.setCosts();
//            try {
//                    File file = new File("seam.png");
//                    //picture = carver.removeSeam(carver.setPath(picture));
//                    ImageIO.write(carver.showSeam(carver.setPath(picture)), "png", file);
//                } catch (IOException e) {
//                    System.out.println("error");
//                }
                picture = carver.removeSeam(carver.setPath(picture));
           // }
            i++;
        }
        return picture;
    }

    public static BufferedImage scaleRows(BufferedImage picture, int newHeight) throws IOException {

        picture = scaleColumns(rotate(picture,90), newHeight);
        picture = rotate(picture,-90);


        return picture;
    }
//    public static BufferedImage rotate(BufferedImage bimg, double angle) {
//
//        int w = bimg.getWidth();
//        int h = bimg.getHeight();
//
//        BufferedImage rotated = new BufferedImage(w, h, bimg.getType());
//        Graphics2D graphic = rotated.createGraphics();
//        graphic.rotate(Math.toRadians(angle), w/2, h/2);
//        graphic.drawImage(bimg, null, 0, 0);
//        graphic.dispose();
//        return rotated;
//    }
private static BufferedImage rotate(BufferedImage buffImage, double angle) {
    double radian = Math.toRadians(angle);
    double sin = Math.abs(Math.sin(radian));
    double cos = Math.abs(Math.cos(radian));

    int width = buffImage.getWidth();
    int height = buffImage.getHeight();

    int nWidth = (int) Math.floor((double) width * cos + (double) height * sin);
    int nHeight = (int) Math.floor((double) height * cos + (double) width * sin);

    BufferedImage rotatedImage = new BufferedImage(
            nWidth, nHeight, BufferedImage.TYPE_INT_ARGB);

    Graphics2D graphics = rotatedImage.createGraphics();

    graphics.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC);

    graphics.translate((nWidth - width) / 2, (nHeight - height) / 2);
    // rotation around the center point
    graphics.rotate(radian, (double) (width / 2), (double) (height / 2));
    graphics.drawImage(buffImage, 0, 0, null);
    graphics.dispose();

    return rotatedImage;
}
}
