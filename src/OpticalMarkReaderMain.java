import FileIO.PDFHelper;
import Filters.Test;
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
        int x = 105;
        int iy = 105;
        int inBetween = 50;
        ArrayList<PImage> pages = getPImagesFromPdf("assets/OfficialOMRSampleDoc.pdf");
        for (PImage page: pages) {
            int y = 105;
            ArrayList<String> answers = new ArrayList<>();
            DImage img = new DImage(page);       // you can make a DImage from a PImage
            for (int i = 1584-iy; i > 0; i-=inBetween) {
                Test filter = new Test(x, y);
                try {
                    filter.processImage(img, true);
                } catch (ArithmeticException e) {
                    break;
                }
                String answer = filter.getLetter();
                if (answer == "n/a") break;
                answers.add(answer);
                y += inBetween;
            }
            System.out.println(answers);
        }

        /*
        Your code here to...
        (1).  Load the pdf
        (2).  Loop over its pages
        (3).  Create a DImage from each page and process its pixels
        (4).  Output 2 csv files
         */

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
