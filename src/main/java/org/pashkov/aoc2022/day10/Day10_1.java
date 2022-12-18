package org.pashkov.aoc2022.day10;

import org.pashkov.aoc2022.util.FileReaderImpl;

import java.util.*;

/**
 * Roman Pashkov created on 15.12.2022 inside the package - org.pashkov.aoc2022.day10
 */
public class Day10_1 {
    private static final String NOOP = "noop";
    private static final String ADDX = "addx.*";
    private static  final int[] indexes = {20,60,100,140,180,220};

    public static void main(String[] args) {
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
                }
                X = X + Integer.parseInt(instructionsArr[1]);
            } else if (instruction.equals(NOOP)) {
                circle++;
                sygnals.put("Circle " + circle, X);
            }
        }
        for(Map.Entry<String,Integer> entry : sygnals.entrySet()) {
            System.out.println(entry);
        }
        List<Integer> resultSignals = new ArrayList<>();
        for (Integer i : indexes) {
           resultSignals.add(sygnals.get("Circle " + i) * i);
        }
        System.out.println(resultSignals.stream().mapToInt(Integer::intValue).sum());
    }
    private static List<String> getFileInput() {
        return FileReaderImpl.readEachLinesFromFile("AoC2022/day10-1.txt");
    }
}
