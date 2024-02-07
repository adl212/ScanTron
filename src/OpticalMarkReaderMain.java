import FileIO.PDFHelper;
import core.DImage;
import processing.core.PImage;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

import static FileIO.PDFHelper.getPImagesFromPdf;

public class OpticalMarkReaderMain {
    public static void main(String[] args) {
        /**String pathToPdf = fileChooser();
        System.out.println("Loading pdf at " + pathToPdf);**/

        ArrayList<PImage> pages = getPImagesFromPdf("assets/omrtest.pdf");
        for (PImage page: pages) {
            DImage img = new DImage(page);       // you can make a DImage from a PImage
            short[][] grid = img.getBWPixelGrid();
            grid = threshold(grid, 128);
            grid = removePadding(grid);
            img.setPixels(grid);
        }
        /*
        Your code here to...
        (1).  Load the pdf
        (2).  Loop over its pages
        (3).  Create a DImage from each page and process its pixels
        (4).  Output 2 csv files
         */

    }

    private static short[][] removePadding(short[][] grid) {
        short[][] newgrid = new short[grid.length][grid[0].length];
        int row = 0;
        for (int r = 0; r < grid.length; r++) {
            if (!rowIsWhite(grid[r])) {
                row++;
                for (int c = 0; c < grid[r].length; c++) {
                    newgrid[row][c] = grid[r][c];
                }
            }
        }
        return newgrid;
    }

    private static boolean rowIsWhite(short[] row) {
        for (int c = 0; c < row.length; c++) {
            if (row[c] != 255) return true;
        }
        return false;
    }

    private static short[][] threshold(short[][] grid, int i) {
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                if (grid[r][c] < i) {
                    grid[r][c] = 0;
                }
                else {
                    grid[r][c] = 255;
                }
            }
        }
        return grid;
    }

    private static String fileChooser() {
        String userDirLocation = System.getProperty("user.dir");
        File userDir = new File(userDirLocation);
        JFileChooser fc = new JFileChooser(userDir);
        int returnVal = fc.showOpenDialog(null);
        File file = fc.getSelectedFile();
        return file.getAbsolutePath();
    }
}
