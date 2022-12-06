package org.pashkov.aoc2022.day5;

import org.pashkov.aoc2022.util.FileReaderImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Roman Pashkov created on 06.12.2022 inside the package - org.pashkov.aoc2022.day5
 */
public class Day5_1a {
    public static void main(String[] args) {
        List<List<String>> preparedInput = prepareInputSplitStacksFromMoves();
        List<String> stacks = preparedInput.get(0);
        List<String> moves = preparedInput.get(1);
        List<String> widthOf2DArr = preparedInput.get(2);
        List<String> arrayWidthMap = preparedInput.get(3);
        int width2dArr = Integer.parseInt(widthOf2DArr.get(0));
        String[][] stacksArr = convertStacksTo2DArr(stacks, stacks.size(), width2dArr, arrayWidthMap);
        String[][] afterApplyMoves = applyMoves(stacksArr, moves);
        System.out.println("Result stacks: ");
        print2DArr(afterApplyMoves);
    }

    private static String[][] applyMoves(String[][] stacksArr, List<String> moves) {
        int count = 1;
        for (String moveLine : moves) {
            if (moveLine.length() > 1) {
                String[] tempArr = moveLine.split("\\s");
                List<String> s = Arrays.stream(tempArr)
                        .filter(s1 -> s1.matches("\\d+"))
                        .collect(Collectors.toList());
                stacksArr = applyMove(s, stacksArr);
                count++;
            }
        }
        return stacksArr;
    }

    private static String[][] applyMove(List<String> s, String[][] stacksArr) {
        int toGetQuantityFromStack = Integer.parseInt(s.get(0));
        int fromStackNumber = Integer.parseInt(s.get(1));
        int toStackNumber = Integer.parseInt(s.get(2));

        String[] elementsToBeMoved;

        int stackPosition = fromStackNumber - 1;

        String[] targetStackFrom = getColumn(stacksArr, stackPosition);
        List<String> columnFromList = Arrays.stream(targetStackFrom).collect(Collectors.toList());
        int difference = columnFromList.size();
        List<String> targetStackFromWithoutEmptyLeading = removeEmptyElements(columnFromList);
        elementsToBeMoved = castListToArray(targetStackFromWithoutEmptyLeading.subList(0, toGetQuantityFromStack));
        targetStackFromWithoutEmptyLeading = removeTakenElementsFromStackFrom(targetStackFromWithoutEmptyLeading, elementsToBeMoved, columnFromList.size() - targetStackFromWithoutEmptyLeading.size());
        int temp = difference - targetStackFromWithoutEmptyLeading.size();
        List<String> partEmpty = new ArrayList<>();
        for (int i = 0; i < temp; i++) {
            partEmpty.add("*");
        }
        partEmpty.addAll(targetStackFromWithoutEmptyLeading);
        String[] stackAfterTakenElements = castListToArray(partEmpty);
        insertStackInto2DArray(stacksArr, stackAfterTakenElements, stackPosition);

        int insertIntoStack = toStackNumber - 1;

        String[] targetInputColumn = getColumn(stacksArr, insertIntoStack);
        long freePlaces = Arrays.stream(targetInputColumn).filter(s1 -> s1.equals("*")).count();

        if (freePlaces < elementsToBeMoved.length) {
            int toAddFreeSpace = (int) (elementsToBeMoved.length - freePlaces);
            stacksArr = increase2DArr(stacksArr, toAddFreeSpace);
        }

        targetInputColumn = getColumn(stacksArr, insertIntoStack);
        targetInputColumn = joinToArrays(elementsToBeMoved, targetInputColumn);
        insertStackInto2DArray(stacksArr, targetInputColumn, insertIntoStack);
        return stacksArr;
    }

    private static List<String> removeTakenElementsFromStackFrom(List<String> targetStackWithoutEmptyLeadSpaces, String[] elementsToBeMoved, int emptiesToBeAdded) {
        String[] strings = castListToArray(targetStackWithoutEmptyLeadSpaces);
        for (int i = 0; i < strings.length; i++) {
            if (i < elementsToBeMoved.length) {
                strings[i] = "*";
            }
        }
        targetStackWithoutEmptyLeadSpaces = Arrays.stream(strings).collect(Collectors.toList());
        return targetStackWithoutEmptyLeadSpaces;
    }

    private static String[] joinToArrays(String[] outputTargets, String[] targetInputColumn) {
        List<String> inputInColumnPart = Arrays.stream(targetInputColumn).filter(s -> !s.equals("*")).collect(Collectors.toList());
        int numberOfEmptyPlaces = targetInputColumn.length - inputInColumnPart.size();
        List<String> outputList = Arrays.stream(outputTargets).collect(Collectors.toList());
        List<String> newList = new ArrayList<>();
        newList.addAll(outputList);
        newList.addAll(inputInColumnPart);
        if (targetInputColumn.length > newList.size()) {
            int sizeDivision = targetInputColumn.length - newList.size();
            for (int i = 0; i < sizeDivision; i++) {
                newList.add(0, "*");
            }
        }
        return castListToArray(newList);
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

    private static String[][] insertStackInto2DArray(String[][] stacksArr, String[] targetInputColumn, int insertIntoStack) {
        int row = 0;
        while (true) {
            stacksArr[row][insertIntoStack] = targetInputColumn[row];
            boolean goOn = true;
            if (row + 1 >= stacksArr.length) {
                goOn = false;
            }
            if (!goOn) {
                break;
            } else {
                row++;
            }
        }
        return stacksArr;
    }

    private static String[] castListToArray(List<String> removeEmptyFromTheBeginning) {
        String[] resultArr = new String[removeEmptyFromTheBeginning.size()];
        for (int i = 0; i < removeEmptyFromTheBeginning.size(); i++) {
            resultArr[i] = removeEmptyFromTheBeginning.get(i);
        }
        return resultArr;
    }

    private static List<String> removeEmptyElements(List<String> columnFromList) {
        List<String> result = new ArrayList<>();
        boolean startTaking = false;
        for (String s : columnFromList) {
            if (!"*".equals(s)) {
                startTaking = true;
            }
            if (startTaking) {
                result.add(s);
            }
        }
        return result;
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

    private static List<String> getFileInput() {
        return FileReaderImpl.readEachLinesFromFile("day5-1.txt");
    }

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
        return Arrays.asList(stacks, moves, List.of(substring), List.of(fileInput.get(dividerIndex)));
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
        return s1.split("");
    }

    private static void print2DArr(String[][] stacksArr) {
        for (int i = 0; i < stacksArr.length; i++) {
            for (int j = 0; j < stacksArr[i].length; j++) {
                System.out.print(stacksArr[i][j] + " ");
            }
            System.out.println();
        }
    }

}
