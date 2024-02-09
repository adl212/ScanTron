package Filters;

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Test implements PixelFilter, Interactive {
    private int x, y;
    private static int xChange, yChange;

    public String getLetter() {
        return letter;
    }

    private String letter;
    public Test(int x, int y) {
        this.x = x;
        this.y = y;
        xChange = 120;
        yChange = 25;
    }
    public Test() {
        this.x = 105;
        int resp = Integer.parseInt(JOptionPane.showInputDialog("Question #"));
        this.y = 105+50*(resp-1);
        xChange = 120;
        yChange = 25;
    }

    public DImage processImage(DImage img, boolean keepImg) {
        short[][] grid = img.getBWPixelGrid();
        grid = crop(grid, x, y, x + xChange, y + yChange);
        grid = threshold(grid, 160);
        letter = (determineLetterOfRow(grid));
        //grid = removePadding(grid);
        if (!keepImg) img.setPixels(grid);
        return img;
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();
        grid = crop(grid, x, y, x + xChange, y + yChange);
        grid = threshold(grid, 160);
        letter = (determineLetterOfRow(grid));
        //grid = removePadding(grid);
        img.setPixels(grid);
        return img;
    }

    private short[][] crop(short[][] grid, int x1, int y1, int x2, int y2) {
        short[][] newgrid = new short[y2 - y1][x2 - x1];
        for (int r = y1; r < y2; r++) {
            for (int c = x1; c < x2; c++) {
                newgrid[r - y1][c - x1] = grid[r][c];
            }
        }
        return newgrid;
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
                } else {
                    grid[r][c] = 255;
                }
            }
        }
        return grid;
    }

    public static String determineLetterOfRow(short[][] croppedGrid) {
        ArrayList<Integer> columns = makeColumns(croppedGrid);

        //int colNum = findAvgCol(columns);
        int colNum = findMaxCol(columns);
        for (int i = 0; i < 5; i++) {
            if(colNum >= i*24 && colNum <= (i+1)*24) {
                String[] letters = {"A", "B", "C", "D", "E"};
                return letters[i];
            }
        }
    return "n/a";
    }

    private static int findMaxCol(ArrayList<Integer> columns) {
        int[] colVals = new int[5];
        for (int colNum: columns) {
            for (int i = 0; i < 5; i++) {
                if (colNum >= i * 24 && colNum <= (i + 1) * 24) {
                    colVals[i]++;
                    break;
                }
            }
        }
        int max = 0;
        int maxI = -1;
        for (int i = 0; i < colVals.length; i++) {
            if (colVals[i] > max) {
                max = colVals[i];
                maxI = i;
            }
        }
        return (maxI) * 24 + 1;
    }

    private static int findAvgCol(ArrayList<Integer> columns) {
        int colSum = 0;
        for (int j = 0; j < columns.size(); j++) {
            colSum += columns.get(j);
        }
        return colSum/columns.size();
    }

    private static ArrayList<Integer> makeColumns(short[][] croppedGrid) {
        ArrayList<Integer> columns = new ArrayList<>();
        for (int r = 0; r < croppedGrid.length; r++) {
            for (int c = 0; c < croppedGrid[0].length; c++) {
                if(croppedGrid[r][c] == 0){
                    columns.add(c);
                }
            }
        }
        return columns;
    }


    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        System.out.println(mouseX);
        System.out.println(mouseY);

    }

    @Override
    public void keyPressed(char key) {

    }
}
