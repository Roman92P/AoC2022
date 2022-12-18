package org.pashkov.aoc2022.day10;

import org.pashkov.aoc2022.util.FileReaderImpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Day10_2 {
    private static int CRTPixel = 0;
    private static int spriteMiddle = 1;
    private static final String NOOP = "noop";
    private static final String ADDX = "addx.*";
    private static final int[] indexes = {20, 60, 100, 140, 180, 220};
    private static String[][] screen = getEmptyScreen();

    public static void main(String[] args) {
        String[][] screenAfterInputs = getSygnals();
        print2DArr(screenAfterInputs);
    }

    private static String[][] getEmptyScreen() {
        String[][] screen = new String[6][40];
        for (int i = 0; i < screen.length; i++) {
            for (int j = 0; j < screen[i].length; j++) {
                screen[i][j] = ".";
            }
        }
        return screen;
    }

    private static List<String> getFileInput() {
        return FileReaderImpl.readEachLinesFromFile("AoC2022/day10-1.txt");
    }

    private static void print2DArr(String[][] stacksArr) {
        int cycleStart = 0;
        int cycleEnd = 0;
        for (int i = 0; i < stacksArr.length; i++) {
            if (cycleStart + 1 < 100 && cycleStart + 1 != 1) {
                System.out.printf("Cycle %s ->  ", String.valueOf(cycleStart + 1));
            } else if (i == 0) {
                System.out.printf("Cycle %s ->   ", String.valueOf(cycleStart + 1));
            } else {
                System.out.printf("Cycle %s -> ", String.valueOf(cycleStart + 1));
            }
            for (int j = 0; j < stacksArr[i].length; j++) {
                cycleStart++;
                cycleEnd++;
                System.out.print(stacksArr[i][j]);
            }
            System.out.printf(" <- Cycle %s", String.valueOf(cycleEnd));
            System.out.println();
        }
    }

    public static String[][] getSygnals() {
        List<String> cpuInstructions = getFileInput();
        Map<String, Integer> sygnals = new LinkedHashMap<>();
        int X = 1;
        int circle = 0;
        for (String instruction : cpuInstructions) {
            if (instruction.matches(ADDX)) {
                String[] instructionsArr = instruction.split("\\s");
                int addXCounter = 2;
                while (addXCounter > 0) {
                    addXCounter--;
                    circle++;
                    sygnals.put("Circle " + circle, X);
                    updateCRTScreen(circle, X);
                    CRTPixel++;
                }
                X = X + Integer.parseInt(instructionsArr[1]);
                spriteMiddle = X;
                //updateCRTScreen(circle, X);
                //CRTPixel++;
            } else if (instruction.equals(NOOP)) {
                circle++;
                sygnals.put("Circle " + circle, X);
                updateCRTScreen(circle, X);
                CRTPixel++;
            }
        }
        List<Integer> resultSignals = new ArrayList<>();
        for (Integer i : indexes) {
            resultSignals.add(sygnals.get("Circle " + i) * i);
        }
        return screen;
    }

    private static void updateCRTScreen(int circle, int x) {
        int spriteStart = spriteMiddle - 1;
        int spriteEnd = spriteMiddle + 1;
        int row = getRow(circle);
        int col = CRTPixel;
        if (row > 0) {
            col = CRTPixel - (row  * 40);
        }
        System.out.println(col);
        if (col >= spriteStart && col <= spriteEnd) {
            screen[row][col] = "#";
        }
    }

    private static int getRow(int circle) {
        if (circle < 40) {
            return 0;
        } else if (circle > 40 && circle < 80) {
            return 1;
        } else if (circle > 80 && circle < 120) {
            return 2;
        } else if (circle > 120 && circle < 160) {
            return 3;
        } else if (circle > 160 && circle < 200) {
            return 4;
        } else if (circle > 200 && circle < 240) {
            return 5;
        }
        return 0;
    }
}