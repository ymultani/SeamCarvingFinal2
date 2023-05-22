import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class SeamCarving {
    Pixel[][] pixels;

    int[][] energy;
    int[][] diffs;
    int[][] costs;
    BufferedImage image;

    public SeamCarving(BufferedImage image) throws IOException {
        this.image = image;
        pixels = new Pixel[image.getHeight()][image.getWidth()];
        diffs = new int[image.getHeight()][image.getWidth()];
        costs = new int[image.getHeight()][image.getWidth()];
        energy = new int[image.getHeight()][image.getWidth()];
    }

    public void setPixels(){
        for(int row = 0; row < pixels.length; row++){
            for(int col = 0; col < pixels[0].length; col++){
                int color = image.getRGB(col,row);
                int blue = color & 0xff;
                int green = (color & 0xff00) >> 8;
                int red = (color & 0xff0000) >> 16;
                pixels[row][col] = new Pixel(red,green,blue);
                energy[row][col] = pixels[row][col].getSaturation();
            }
        }
    }

    public void setDiffs(){
        //System.out.println(Arrays.deepToString(diffs));
        for(int row = 0; row < diffs.length; row++){
            for(int col = 0; col < diffs[0].length; col++){
                if(row == 0 && col < diffs[0].length -1){
                    int rightDiff = Math.abs(energy[row][col+1] - energy[row][col]);
                    int downDiff = Math.abs(energy[row+1][col] - energy[row][col]);
                    if(col == 0){
                        diffs[row][col] = rightDiff + downDiff;
                    }
                    else{
                        int leftDiff = Math.abs(energy[row][col-1] - energy[row][col]);
                        diffs[row][col] = leftDiff + rightDiff + downDiff;
                    }
                }
                else if(row == 0 && col == diffs[0].length-1){
                    int leftDiff = Math.abs(energy[row][col-1] - energy[row][col]);
                    int downDiff = Math.abs(energy[row+1][col] - energy[row][col]);
                    diffs[row][col] = leftDiff + downDiff;

                }
                else if(col == 0){
                    if(row == diffs.length -1){
                        int upDiff = Math.abs(energy[row - 1][col] - energy[row][col]);
                        int rightDiff = Math.abs(energy[row][col+1] - energy[row][col]);
                        //int downDiff = Math.abs(energy[row+1][col] - energy[row][col]);
                        diffs[row][col] = rightDiff + upDiff;
                    }
                    else {
                        int rightDiff = Math.abs(energy[row][col + 1] - energy[row][col]);
                        int downDiff = Math.abs(energy[row + 1][col] - energy[row][col]);
                        diffs[row][col] = rightDiff + downDiff;
                    }
                }
                else if(row == diffs.length-1 && col == diffs[0].length-1){
                    int leftDiff = Math.abs(energy[row][col-1] - energy[row][col]);
                    int upDiff = Math.abs(energy[row - 1][col] - energy[row][col]);
                    diffs[row][col] = leftDiff + upDiff;

                }
                else if(col == diffs[0].length-1 && row > 0){
                    int downDiff = Math.abs(energy[row+1][col] - energy[row][col]);
                    int leftDiff = Math.abs(energy[row][col-1] - energy[row][col]);
                    int upDiff = Math.abs(energy[row - 1][col] - energy[row][col]);
                    diffs[row][col] = leftDiff + downDiff + upDiff;

                }
                else if(row == diffs.length-1){
                    int upDiff = Math.abs(energy[row - 1][col] - energy[row][col]);
                    int leftDiff = Math.abs(energy[row][col - 1] - energy[row][col]);
                    int rightDiff = Math.abs(energy[row][col + 1] - energy[row][col]);
                    diffs[row][col] = leftDiff + upDiff + rightDiff;
                }
                else {
                    int upDiff = Math.abs(energy[row - 1][col] - energy[row][col]);
                    int downDiff = Math.abs(energy[row + 1][col] - energy[row][col]);
                    int leftDiff = Math.abs(energy[row][col - 1] - energy[row][col]);
                    int rightDiff = Math.abs(energy[row][col + 1] - energy[row][col]);
                    diffs[row][col] = upDiff + downDiff + leftDiff + rightDiff;
//                System.out.println("UpDIff : " + upDiff);
//                System.out.println("Down :" + downDiff);
//                System.out.println("Left : " + leftDiff);
//                System.out.println("right: "  + rightDiff);
                }
            }
        }
    }

    public void setCosts(){
        for(int i = 0; i < costs.length; i++){
            costs[i] = diffs[i].clone();
        }
        for(int row = costs.length -2; row > 0; row--){
            for(int col = 0; col < costs[0].length-1; col++){
                if(col == 0) {
                    costs[row][col] += Math.min(costs[row + 1][col], costs[row + 1][col + 1]);
                }
                else {
                    costs[row][col] += Math.min(costs[row + 1][col - 1], Math.min(costs[row + 1][col], costs[row + 1][col + 1]));
                }
            }
        }
    }

    public boolean[][] setPath(BufferedImage picture){
        boolean[][] path = new boolean[picture.getHeight()][picture.getWidth()];
        path[costs.length-1][getXMin(costs[costs.length-1], 0, costs[0].length)] = true;
        int col = getXMin(costs[costs.length-1], 0, costs[0].length);
        int previousCol = col;
        for(int row = costs.length-2; row > 0 ; row--){

            //System.out.println("row: " + row);
            int start = (previousCol == 0) ? col: col-1;
            int end = (col == costs[0].length-1) ? col + 1 : col + 2;
            path[row][getXMin(costs[row+1], start, end)] = true;
        }
        return path;
    }

    public int getXMin(int[] row, int start, int end){
        //System.out.println("Length" + costs.length);
        int min = Integer.MAX_VALUE;
        int index = -1;
        for(int i = start; i < end; i++){
            //System.out.println(i);
            if(row[i] < min){
                min = row[i];
                index = i;
            }
        }
        return index;
    }

    public BufferedImage removeSeam(boolean[][] path){
        BufferedImage copy = new BufferedImage(image.getWidth()-1, image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for(int row = 0; row < copy.getHeight(); row++){
            for(int col = 0; col < copy.getWidth()-2; col++){
                if(!path[row][col]){
                    int red = pixels[row][col].getrVal();
                    int green = pixels[row][col].getgVal();
                    int blue = pixels[row][col].getbVal();
                    int color = (red << 16) | (green << 8) | blue;
                    copy.setRGB(col,row, color);
                }
                else{
                    int red = pixels[row][col+1].getrVal();
                    int green = pixels[row][col+1].getgVal();
                    int blue = pixels[row][col+1].getbVal();
                    int color = (red << 16) | (green << 8) | blue;
                    copy.setRGB(col,row, color);
                }
            }
        }
        return copy;
    }
    public BufferedImage showSeam(boolean[][] path) throws IOException {
        BufferedImage copy = ImageIO.read(new File("/Users/yuvro/Downloads/dali2.jpg"));
        for(int row = 0; row < copy.getHeight(); row++){
            for(int col = 0; col < copy.getWidth(); col++){
                if(!path[row][col]){
                    copy.setRGB(col,row, 255);
                }
            }
        }
        return copy;


    }

//    public BufferedImage scaleColumns(int newWidth, BufferedImage pic){
//        int i = 0;
//        while(i < image.getWidth() - newWidth){
//            pic = removeSeam(setPath());
//            i++;
//        }
//        return pic;
//
//    }



}
