package org.pashkov.aoc2022.day9;

import org.pashkov.aoc2022.util.FileReaderImpl;

import java.util.*;

public class Day9_2 {

    private static Set<String> tailTrack = new TreeSet<>();

    public static void main(String[] args) {
        int[] head = new int[]{0, 0};
        List<int[]> tails = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            tails.add(new int[]{0,0});
        }

        tailTrack.add("0:0");
        List<int[]> headTrack = new LinkedList<>();
        for (String lineCommand : getFileInput()) {
            String[] commandArr = lineCommand.trim().split("\\s");
            String direction = commandArr[0];
            String numberOfSteps = commandArr[1];
            for (int i = 0; i < Integer.parseInt(numberOfSteps); i++) {
                head = adjustHeadPosition(direction, head);
                headTrack.add(head);
//                if (!checkIfCordsAreTouching(head, tail)) {
//                    tail = tailCatchUp(head, tail);
//                    tailTrack.add(tail[0]+":"+tail[1]);
//                }
            }
            adoptAllTailsPosToCurrentHeadPosition(head, tails, 0);
        }
        System.out.println(tailTrack.stream().distinct().count());
    }

    private static void adoptAllTailsPosToCurrentHeadPosition(int[] head, List<int[]> tails, int startPosition) {
       int[]tailUnderCheck = tails.get(startPosition);
       if (!checkIfCordsAreTouching(head,tailUnderCheck)) {
           tailUnderCheck = tailCatchUp(head, tailUnderCheck);
           tailTrack.add(tailUnderCheck[0]+":"+tailUnderCheck[1]);
       }
       boolean runNextRoundOfAdopt = false;
        for (int i = startPosition+1; i < tails.size(); i++) {

        }
    }

    private static int[] tailCatchUp(int[] head, int[] tail) {
        int diffX = head[0] - tail[0];
        int diffY = head[1] - tail[1];
        return new int[]{(int) (tail[0] + Math.signum(diffX)), (int) (tail[1] + Math.signum(diffY))};
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
