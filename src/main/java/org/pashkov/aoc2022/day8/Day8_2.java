package org.pashkov.aoc2022.day8;

import org.pashkov.aoc2022.util.FileReaderImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Roman Pashkov created on 09.12.2022 inside the package - org.pashkov.aoc2022.day8
 */
public class Day8_2 {
    public static void main(String[] args) {
        List<String> fileInput = getFileInput();
        int h = fileInput.size();
        int w = fileInput.get(0).length() - 2;
        int numberOfTreesOnTheEdges = (2 * h) + (2 * w);

        List<Integer> treesVisibleInInteriorScores = findVisibleTrees(fileInput);

        System.out.println(treesVisibleInInteriorScores.stream().mapToInt(Integer::intValue).max().getAsInt());
    }

    private static List<Integer> findVisibleTrees(List<String> fileInput) {
        List<Integer> resultScores = new ArrayList<>();
        String[][] strings = convertListTo2DArr(fileInput);
        for (int i = 1; i < strings.length - 1; i++) {
            for (int j = 1; j < strings[i].length - 1; j++) {
                String tree = strings[i][j];
                String[] fromTop = getAllTreesFromTop(i, j, "top", strings);
                String[] fromDown = getAllTreesFromTop(i, j, "down", strings);
                String[] fromLeft = getAllTreesFromTop(i, j, "left", strings);
                String[] fromRight = getAllTreesFromTop(i, j, "right", strings);
                int treeInt = Integer.parseInt(tree);

                int numberOfHigherTreesAround = calculateNumberOfTreesHigher(treeInt, fromTop, fromDown, fromLeft, fromRight);
                if (numberOfHigherTreesAround < 4) {
                    System.out.println("Calculating see distance for: " + treeInt + " with cords: " + i + " : " + j);
                    int topSeeDistance = calculateSeeDistance(fromTop, treeInt);
                    int downSeeDistance = calculateSeeDistance(fromDown, treeInt);
                    int leftSeeDistance = calculateSeeDistance(fromLeft, treeInt);
                    int rightSeeDistance = calculateSeeDistance(fromRight, treeInt);
                    System.out.println(List.of(topSeeDistance, downSeeDistance, leftSeeDistance, rightSeeDistance));
                    resultScores.add(Stream.of(topSeeDistance, downSeeDistance, leftSeeDistance, rightSeeDistance)
                            .reduce(1, (a, b) -> a * b));
                }
            }
        }
        return resultScores;
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

    private static int calculateSeeDistance(String[] arr, int treeInt) {
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            if (Integer.parseInt(arr[i]) < treeInt) {
                count++;
            } else if (Integer.parseInt(arr[i]) == treeInt) {
                return count + 1;
            } else {
                return count+1;
            }
        }
        return count;
    }

    private static String[] getAllTreesFromTop(int i, int j, String direction, String[][] arr) {
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
