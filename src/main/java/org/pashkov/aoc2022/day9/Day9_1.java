package org.pashkov.aoc2022.day9;

import org.pashkov.aoc2022.util.FileReaderImpl;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Roman Pashkov created on 10.12.2022 inside the package - org.pashkov.aoc2022.day9
 */
public class Day9_1 {

    private static String head = "89:89";
    private static String tail = "89:89";
    private static List<String> headTrack = new LinkedList<>();
    private static List<String> tailTrack = new LinkedList<>();

    public static void main(String[] args) {
        String[][] map = createMap();
//        print2DArr(map);
        List<String> fileInput = getFileInput();
        System.out.println(fileInput.size());
        //6090
//        headTrack.add(head);
//        tailTrack.add(tail);
        for (String steps : fileInput) {
            String direction = "";
            switch (steps.substring(0, 1)) {
                case "R":
                    direction = "right";
                    break;
                case "L":
                    direction = "left";
                    break;
                case "U":
                    direction = "Up";
                    break;
                case "D":
                    direction = "down";
                    break;
            }
            System.out.println(direction + " " + steps);
            changeHeadPosition(steps);
            System.out.println("____________________________________________");
        }
        System.out.println(headTrack);
        System.out.println(tailTrack);
        System.out.println(tailTrack.stream().distinct().collect(Collectors.toList()));
        long count = tailTrack.stream().distinct().count();
        System.out.println(count);
        System.out.println("Not distinct " + tailTrack.size());
        System.out.println("Head moves: " + headTrack.size());

        addTailPositionsToMap(tailTrack.stream().distinct().collect(Collectors.toList()), map);
        print2DArr(map);

    }

    private static String[][] addTailPositionsToMap(List<String> collect, String[][] map) {
        int row = map.length-1;
        for (String cords : collect) {
            String[] split = cords.split(":");
            int x =  Integer.parseInt(split[0]);
            int y =  Integer.parseInt(split[1]);
            map[row-y][x] = "#";
        }
        return map;
    }

    private static void changeHeadPosition(String howFar) {
        int j = Integer.parseInt(howFar.substring(howFar.length() - 1));
        for (int i = 1; i < j + 1; i++) {
            System.out.println("Head before step: " + head);
            int[] currentHeadCoordinates = getCurrentCoordinates(head);
            int x = currentHeadCoordinates[0];
            int y = currentHeadCoordinates[1];
            switch (howFar.substring(0, howFar.length() - 1).trim()) {
                case "R":
                    x = x + 1;
                    head = x + ":" + y;
                    break;
                case "L":
                    x = Math.max(x - 1,0);
                    head = x + ":" + y;
                    break;
                case "D":
                    y = Math.max(y - 1,0);
                    head = x + ":" + y;
                    break;
                case "U":
                    y = y + 1;
                    head = x + ":" + y;
                    break;
            }
            System.out.println("Head after step: " + head);
            headTrack.add(head);
            System.out.println("Tail before step: " + tail);
            if (!nextToEachOther() && !head.equals(tail)) {
                tail = headTrack.get(headTrack.size() - 2);
            }
            System.out.println("Tail after step: " + tail);
            tailTrack.add(tail);
        }
    }

    private static boolean nextToEachOther() {
        int[] currentHeadCoordinates = getCurrentCoordinates(head);
        int x = currentHeadCoordinates[0];
        int y = currentHeadCoordinates[1];
        String[] tempArr = new String[8];
        //R
        tempArr[0] = (x + 1) + ":" + y;
        //L
        tempArr[1] = (x - 1) + ":" + y;
        //D
        tempArr[2] = x + ":" + (y - 1);
        //U
        tempArr[3] = x + ":" + (y + 1);
        //UL
        tempArr[4] = (x - 1) + ":" + (y + 1);
        //UR
        tempArr[5] = (x + 1) + ":" + (y + 1);
        //DL
        tempArr[6] = (x - 1) + ":" + (y - 1);
        //DR
        tempArr[7] = (x + 1) + ":" + (y - 1);
        return Arrays.stream(tempArr).filter(s -> s.equals(tail)).count() >= 1;
    }

    private static int[] getCurrentCoordinates(String headOrTail) {
        return Arrays.stream(headOrTail.split(":"))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    private static List<String> getFileInput() {
        return FileReaderImpl.readEachLinesFromFile("day9-1.txt");
    }

    private static String[][] createMap () {
        int s =Integer.parseInt(head.split(":")[0]);
        String[][] strings = new String[150][150];
        for (int i = 0; i < strings.length; i++) {
            for (int j = 0; j < strings[i].length; j++) {
                strings[i][j] = ".";
            }
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
}
