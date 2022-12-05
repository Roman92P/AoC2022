package org.pashkov.aoc2022.day5;

import org.pashkov.aoc2022.util.FileReaderImpl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Roman Pashkov created on 05.12.2022 inside the package - org.pashkov.aoc2022.day5
 */
public class Day5_1 {
    public static void main(String[] args) {
        List<List<String>> preparedInput = prepareInputSplitStacksFromMoves();
        List<String> stacks = preparedInput.get(0);
        List<String> moves = preparedInput.get(1);
        List<String> widthOf2DArr = preparedInput.get(2);
        List<String> arrayWidthMap = preparedInput.get(3);
        int width2dArr = Integer.parseInt(widthOf2DArr.get(0));
        String[][] stacksArr = convertStacksTo2DArr(stacks, stacks.size(), width2dArr,arrayWidthMap);
        print2DArr(stacksArr);
        String[][] afterApplyMoves = applyMoves(stacksArr, moves);
        print2DArr(afterApplyMoves);
    }

    private static String[] convertStringToArr(String stackLine, List<String> arrayWidthMap) {
        String s = stackLine.replaceAll("[\\]\\[]", "");
        String[] split = s.split("");
        int count = 0;
        for (int i = 0; i < split.length; i++) {
            if (split[i].equals(" ")) {
                count++;
                if (count == 4) {
                    split[i] = "*";
                    count = 0;
                }
            } else count = 0;
        }
        String s1 = Arrays.toString(split).replaceAll("[\\]\\[\\s,]", "");
        System.out.println(s1);
        return s1.split("");
    }

    private static String[][] applyMoves(String[][] stacksArr, List<String> moves) {
        int count = 1;
        for (String moveLine : moves) {
            if (moveLine.length() > 1) {
                System.out.println("Move: " + count);
                String s = moveLine.replaceAll("\\D", "");
                stacksArr = applyMove(s, stacksArr);
                count++;
            }
            System.out.println("Print after move");
            print2DArr(stacksArr);
        }
        return stacksArr;
    }

    private static String[][] applyMove(String s, String[][] stacksArr) {
        String[] paramsOfMove = s.split("");
        System.out.println(Arrays.toString(paramsOfMove));
        int quantity = Integer.parseInt(paramsOfMove[0]);
        int fromStack = Integer.parseInt(paramsOfMove[1]);
        int toStack = Integer.parseInt(paramsOfMove[2]);

        String[] outputTargets = new String[quantity];

        int stackPosition = fromStack - 1;
        int row = 0;

        for (int i = 0; i < quantity; i++) {
            String targetCrate = stacksArr[row][stackPosition];
            while ("*".equals(targetCrate)) {
                row++;
                targetCrate = stacksArr[row][stackPosition];
            }
            outputTargets[i] = targetCrate;
            stacksArr[row][stackPosition] = "*";
        }
        System.out.println(Arrays.toString(outputTargets));
        int insertIntoStack = toStack - 1;
        row = 0;

        String[] targetInputColumn = getColumn(stacksArr, insertIntoStack);
        System.out.println("input to" + Arrays.toString(targetInputColumn) + ". Size: " + targetInputColumn.length);
        long freePlaces = Arrays.stream(targetInputColumn).filter(s1 -> s1.equals("*")).count();
        System.out.println("Free places: " + freePlaces);

        if (freePlaces < outputTargets.length) {
            int toAddFreeSpace = (int) (outputTargets.length - freePlaces);
            stacksArr = increase2DArr(stacksArr, toAddFreeSpace);
            System.out.println("Print after increasing: ");
            print2DArr(stacksArr);
        }

        for (int i = 0; i < quantity; i++) {
            System.out.println("To insert: " + Arrays.toString(outputTargets));
            String targetCrate = stacksArr[row][insertIntoStack];
            while (true) {
                targetCrate = stacksArr[row][insertIntoStack];
                boolean tempMatcher = true;
                if (row + 1 < stacksArr.length) {
                    tempMatcher = (stacksArr[row + 1][insertIntoStack]).matches("[A-Z]");
                }
                if ("*".equals(targetCrate) && tempMatcher) {
                    stacksArr[row][insertIntoStack] = outputTargets[i];
                    row = 0;
                    break;
                }
                row++;
            }
        }

        return stacksArr;
    }

    private static String[][] increase2DArr(String[][] stacksArr, int toAddFreeSpace) {
        String[][] greaterArr = new String[stacksArr.length + toAddFreeSpace][stacksArr[0].length];
        for (int i = 0; i < greaterArr.length; i++) {
            for (int j = 0; j < greaterArr[i].length; j++) {
                if (i < toAddFreeSpace) {
                    greaterArr[i][j] = "*";
                } else {
                    greaterArr[i][j] = stacksArr[i - toAddFreeSpace][j];
                }
            }
        }
        return greaterArr;
    }

    public static String[] getColumn(String[][] array, int index) {
        int count = 0;
        String[] strings = new String[array.length];
        while (count < array.length) {
            strings[count] = array[count][index];
            count++;
        }
        return strings;
    }

    private static void print2DArr(String[][] stacksArr) {
        for (int i = 0; i < stacksArr.length; i++) {
            for (int j = 0; j < stacksArr[i].length; j++) {
                System.out.print(stacksArr[i][j]);
            }
            System.out.println();
        }
    }

    private static String[][] convertStacksTo2DArr(List<String> stacks, int h, int w, List<String> arrayWidthMap) {
        String[][] stack2DArr = new String[h][w];
        for (int i = 0; i < stacks.size(); i++) {
            String stackLine = stacks.get(i);
            String[] lineArr = convertStringToArr(stackLine, arrayWidthMap);
            String[] arrLine = new String[w];
            System.arraycopy(lineArr, 0, arrLine, 0, lineArr.length);
            String[] split = Arrays.stream(arrLine).map(s3 -> {
                if (s3 == null) {
                    s3 = "*";
                }
                return s3.replaceAll("[\\]\\[]", "");
            }).collect(Collectors.joining()).split("");
            stack2DArr[i] = split;
        }
        return stack2DArr;
    }


//    private static String[] convertStringToArr(String stackLine, List<String> arrayWidthMap) {
//        String s1 = arrayWidthMap.toString().replaceAll("\\D", "");
//        String substring = s1.substring(s1.length() - 1);
//        String[] strings = new String[Integer.parseInt(substring)];
//        String s2 = stackLine.replaceAll("[\\]\\[]", "");
//        char[] chars = s2.toCharArray();
//        System.out.println(Arrays.toString(chars));
//        int arrLengthCount = 0;
//        for (int i = 0; i < chars.length; i++) {
//            char c = chars[i];
//            if (Character.isAlphabetic(c)) {
//                strings[i] = Character.toString(c);
//            }
//
//        }
//        String s = stackLine.replaceAll("\\s{3}", "*");
//        return s.split(" ");
//    }

    private static List<List<String>> prepareInputSplitStacksFromMoves() {
        List<String> fileInput = getFileInput();
        int dividerIndex = fileInput.stream().mapToInt(s -> {
            int a = 0;
            if (s.matches("^(\\s+\\d\\s*\\d*)+$")) {
                a = fileInput.indexOf(s);
            }
            return a;
        }).sum();

        String s = fileInput.get(dividerIndex);
        String substring = s.substring(s.length() - 1);
        List<String> stacks = fileInput.subList(0, dividerIndex);
        List<String> moves = fileInput.subList(dividerIndex + 1, fileInput.size());
        return Arrays.asList(stacks, moves, Arrays.asList(substring), Arrays.asList(fileInput.get(dividerIndex)));
    }

    private static List<String> getFileInput() {
        return FileReaderImpl.readEachLinesFromFile("day5-1.txt");
    }

}
