package org.pashkov.aoc2022.day8;

import org.pashkov.aoc2022.util.FileReaderImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Roman Pashkov created on 09.12.2022 inside the package - org.pashkov.aoc2022.day8
 */
public class Day8_1 {
    public static void main(String[] args) {
        List<String> fileInput = getFileInput();
        int h = fileInput.size();
        int w = fileInput.get(0).length() - 2;
        int numberOfTreesOnTheEdges = (2 * h) + (2 * w);
        int treesVisibleInInterior = findVisibleTrees(fileInput);
        System.out.println(treesVisibleInInterior + numberOfTreesOnTheEdges);
    }

    private static int findVisibleTrees(List<String> fileInput) {
        int count = 0;
        String[][] strings = convertListTo2DArr(fileInput);
        print2DArray(strings);
        for (int i = 1; i < strings.length - 1; i++) {
            for (int j = 1; j < strings[i].length - 1; j++) {
                String tree = strings[i][j];
                String[] fromTop = getAllTreerFromTop(i, j, "top", strings);
                String[] fromDown = getAllTreerFromTop(i, j, "down", strings);
                String[] fromLeft = getAllTreerFromTop(i, j, "left", strings);
                String[] fromRight = getAllTreerFromTop(i, j, "right", strings);
                int treeInt = Integer.parseInt(tree);
                int numberOfHigherTreesAround = calculateNumberOfTreesHigher(treeInt, fromTop, fromDown, fromLeft, fromRight);
                if (numberOfHigherTreesAround < 4) {
                    count++;
                }
            }
        }
        return count;
    }

    private static int calculateNumberOfTreesHigher(int tree, String[] fromTop, String[] fromDown, String[] fromLeft, String[] fromRight) {
        long count = getIfThereIsHigherTreeInLine(tree, fromTop);
        long count1 = getIfThereIsHigherTreeInLine(tree, fromDown);
        long count2 = getIfThereIsHigherTreeInLine(tree, fromLeft);
        long count3 = getIfThereIsHigherTreeInLine(tree, fromRight);
        return (int) (count + count1 + count2 + count3);
    }

    private static long getIfThereIsHigherTreeInLine(int tree, String[] fromTop) {
        return Arrays.stream(fromTop).mapToInt(Integer::parseInt).filter(value -> value >= tree).findFirst().stream().count();
    }

    private static List<Integer> concatArrays(List<String[]> arrs) {
        List<Integer> result = new ArrayList<>();
        for (String[] arr : arrs) {
            result.addAll(Arrays.stream(arr).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList()));
        }
        return result;
    }

    private static String[] getAllTreerFromTop(int i, int j, String direction, String[][] arr) {
        List<String> result = new ArrayList<>();
        switch (direction) {
            case "top":
                while (i > 0) {
                    i--;
                    result.add(arr[i][j]);
                }
                break;
            case "down":
                while (i < arr.length - 1) {
                    i++;
                    result.add(arr[i][j]);
                }
                break;
            case "left":
                while (j > 0) {
                    j--;
                    result.add(arr[i][j]);
                }
                break;
            case "right":
                while (j < arr[i].length - 1) {
                    j++;
                    result.add(arr[i][j]);
                }
        }
        return result.toArray(new String[0]);
    }

    private static void print2DArray(String[][] strings) {
        for (int i = 0; i < strings.length; i++) {
            for (int j = 0; j < strings[i].length; j++) {
                System.out.print(strings[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    private static String[][] convertListTo2DArr(List<String> list) {
        String[][] arr = new String[list.size()][list.get(0).length()];
        for (int i = 0; i < list.size(); i++) {
            String line = list.get(i);
            String[] lineArr = convertStringToArr(line);
            arr[i] = lineArr;
        }
        return arr;
    }

    private static String[] convertStringToArr(String str) {
        return str.split("");
    }

    private static List<String> getFileInput() {
        return FileReaderImpl.readEachLinesFromFile("day8-1.txt");
    }
}
