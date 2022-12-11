package org.pashkov.aoc2022.day9;

import org.pashkov.aoc2022.util.FileReaderImpl;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Roman Pashkov created on 10.12.2022 inside the package - org.pashkov.aoc2022.day9
 */
public class Day9_1 {

    private final static String STARTING_POINT = "0-0";
    private static String head = "0-0";
    private static String tail = "0-0";
    private static List<String> headTrack = new ArrayList<>();
    private static List<String> tailTrack = new ArrayList<>();

    public static void main(String[] args) {
        List<String> fileInput = getFileInput();
        headTrack.add(head);
        tailTrack.add(tail);
        for (String steps : fileInput) {
            System.out.println("Current step: " + steps);
            changeHeadPosition(steps);
        }
        System.out.println(headTrack);
        System.out.println(tailTrack);
        System.out.println(tailTrack.stream().distinct().collect(Collectors.toList()));
        long count = tailTrack.stream().distinct().count();
        System.out.println(count);
    }

    private static void adoptTailPositionToCurrentHead() {
            int[] headCords = getCurrentCoordinates(head);
            int headX = headCords[0];
            int headY = headCords[1];
            int[] tailCords = getCurrentCoordinates(tail);
            int tailX = tailCords[0];
            int tailY = tailCords[1];
            int xSubs = headX - tailX;
            int ySubs = headY - tailY;
            if (xSubs != 0 || ySubs != 0) {

            }

            tail = tailX + "-" + tailY;
            tailTrack.add(tail);
    }

    private static void changeHeadPosition(String howFar) {
        int j = Integer.parseInt(howFar.substring(howFar.length() - 1));
        for (int i = 1; i < j+1; i++) {
            int[] currentHeadCoordinates = getCurrentCoordinates(head);
            int x = currentHeadCoordinates[0];
            int y = currentHeadCoordinates[1];
            switch (howFar.substring(0, howFar.length() - 1).trim()) {
                case "R":
                    x = x + 1;
                    head = x + "-" + y;
                    break;
                case "L":
                    x = x - 1;
                    head = x + "-" + y;
                    break;
                case "D":
                    y = y - 1;
                    head = x + "-" + y;
                    break;
                case "U":
                    y = y + 1;
                    head = x + "-" + y;
                    break;
            }
            headTrack.add(head);
            adoptTailPositionToCurrentHead();
        }
    }

    private static int[] getCurrentCoordinates(String headOrTail) {
        return Arrays.stream(headOrTail.split("-"))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    private static List<String> getFileInput() {
        return FileReaderImpl.readEachLinesFromFile("day9-1e.txt");
    }
}
