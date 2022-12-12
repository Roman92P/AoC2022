package org.pashkov.aoc2022.day9;

import org.pashkov.aoc2022.util.FileReaderImpl;

import java.util.*;

public class Day9_3 {
    public static void main(String[] args) {
        int[] head = new int[]{0, 0};
        int[] tail = new int[]{0, 0};

        Set<String> tailTrack = new TreeSet<>();

        tailTrack.add("0:0");
        List<int[]> headTrack = new LinkedList<>();

        for (String lineCommand : getFileInput()) {
            System.out.println("Step: " + lineCommand);
            System.out.println("Head before step: " + Arrays.toString(head));
            System.out.println("Tail before step: " + Arrays.toString(tail));
            String[] commandArr = lineCommand.trim().split("\\s");
            String direction = commandArr[0];
            String numberOfSteps = commandArr[1];
            for (int i = 0; i < Integer.parseInt(numberOfSteps); i++) {
                head = adjustHeadPosition(direction, head);
                headTrack.add(head);
                if (!checkIfCordsAreTouching(head, tail)) {
                    tail = tailCatchUp(head, tail);
                    System.out.println("Will add tail: " + Arrays.toString(tail));
                    tailTrack.add(tail[0]+":"+tail[1]);

                }
            }
            System.out.println("Head after step: " + Arrays.toString(head));
            System.out.println("Tail after step: " + Arrays.toString(tail));
            System.out.println("____________________________________________");
        }
        System.out.println(tailTrack.stream().distinct().count());
    }

    private static int[] tailCatchUp(int[] head, int[] tail) {
        int diffX = head[0] - tail[0];
        int diffY = head[1] - tail[1];
        return new int[]{(int) (tail[0] + Math.signum(diffX)), (int) (tail[1] + Math.signum(diffY))};
    }

    private static boolean headAndTailAreTouching(int[] head, int[] tail) {
        int x = head[0];
        int y = head[1];
        List<int[]> allAroundDots = new ArrayList<>();
        allAroundDots.add(new int[]{x + 1, y});
        allAroundDots.add(new int[]{x - 1, y});
        allAroundDots.add(new int[]{x, y - 1});
        allAroundDots.add(new int[]{x, y + 1});
        allAroundDots.add(new int[]{x - 1, y + 1});
        allAroundDots.add(new int[]{x + 1, y + 1});
        allAroundDots.add(new int[]{x - 1, y - 1});
        allAroundDots.add(new int[]{x + 1, y - 1});
        allAroundDots.add(new int[]{x, y});
        return allAroundDots.stream().filter(ints -> Arrays.equals(ints, tail)).count() == 1;
    }

    private static boolean checkIfCordsAreTouching(int[] head, int[] tail) {
        int diffX = head[0] - tail[0];
        int diffY = head[1] - tail[1];
        return Math.abs(diffX) > 1 || Math.abs(diffY) > 1? false : true;
    }

    private static int[] adjustHeadPosition(String direction, int[] head) {
        int[] nHead = new int[2];
        int x = head[0];
        int y = head[1];
        switch (direction) {
            case "R":
                x = x + 1;
                break;
            case "L":
                x = x - 1;
                break;
            case "D":
                y = y - 1;
                break;
            case "U":
                y = y + 1;
                break;
        }
        nHead[0] = x;
        nHead[1] = y;
        return nHead;
    }

    private static List<String> getFileInput() {
        return FileReaderImpl.readEachLinesFromFile("AoC2022/day9-1.txt");
    }
}
