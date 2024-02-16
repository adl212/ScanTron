import Filters.Test;
import core.DImage;
import processing.core.PImage;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import static FileIO.PDFHelper.getPImagesFromPdf;

public class OpticalMarkReaderMain {
    public static void main(String[] args) {
        /**String pathToPdf = fileChooser();
        System.out.println("Loading pdf at " + pathToPdf);**/
        int x = 105;
        int iy = 105;
        int inBetween = 50;
        ArrayList<PImage> pages = getPImagesFromPdf("assets/OfficialOMRSampleDoc.pdf");
        String[][] answers = new String[6][12];
        for (int i = 0; i < pages.size(); i++) {
            PImage page = pages.get(i);
            int y = 105;
            ArrayList<String> answers = new ArrayList<>();
            DImage img = new DImage(page);       // you can make a DImage from a PImage
            for (int j = 0; j < 25; j++) {
                Test filter = new Test(x, y);
                try {
                    filter.processImage(img, true);
                } catch (ArithmeticException e) {
                    break;
                }
                String answer = filter.getLetter();
                if (answer == "n/a") break;
                answers[i][j] = answer;
                y += inBetween;
            }
        }
        System.out.println(Arrays.deepToString(answers));


        String[] correctAnswers = answers[0];
        String csv1 = generateCSV1(answers, correctAnswers);
        String csv2 = generateCSV2(answers, correctAnswers);
        try {
            writeDataToFile("output.csv", csv1);
            writeDataToFile("output1.csv", csv2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        
        



        /*
        Your code here to...
        (1).  Load the pdf
        (2).  Loop over its pages
        (3).  Create a DImage from each page and process its pixels
        (4).  Output 2 csv files
         */

    }

    private static String generateCSV1(String[][] answers, String[] correct) {
        String output = "page,q1,q2,q3,q4,q5,q6,q7,q8,q9,q10,q11,q12,#right\n";
        for (int i = 0; i < answers.length; i++) {
            output += makeLine(answers[i], correct, i) + "\n";
        }
        return (output);
    }

    private static String makeLine(String[] answer, String[] correct, int pageNum) {
        String line = (pageNum+1)+",";
        int correctNum = 0;
        for (int i = 0; i < answer.length; i++) {
            if (answer[i] == correct[i]) {
                correctNum++;
                line += "1,";
            }
            else {
                line += "0,";
            }
        }
        line += String.valueOf(correctNum);
        return line;
    }

    public static void writeDataToFile(String filePath, String data) throws IOException {
        try (FileWriter f = new FileWriter(filePath);
             BufferedWriter b = new BufferedWriter(f);
             PrintWriter writer = new PrintWriter(b);) {


            writer.println(data);


        } catch (IOException error) {
            System.err.println("There was a problem writing to the file: " + filePath);
            error.printStackTrace();
        }
    }

    private static String generateCSV2(String[][] answers, String[] correct) {
        String output = "Q#,R,W\n";
        ArrayList<Integer> studentsWrong = getStudentsWrong(answers, correct);
        for (int i = 0; i < studentsWrong.size(); i++) {
            int wrong = studentsWrong.get(i);
            int right = 5 - wrong;
            output += (i+1) + "," + right + "," + wrong + "\n";
        }
        return output;
        
    }

    private static ArrayList<Integer> getStudentsWrong(String[][] answers, String[] correct) {
        ArrayList<Integer> numWrongArray = new ArrayList<>();
        for (int c = 0; c < answers[0].length; c++) {
            int count = 0;
            for(int r = 1; r < answers.length; r++){
                if(answers[r][c] != correct[c]) count++;
            }
            numWrongArray.add(count);
        }
        return numWrongArray;
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
