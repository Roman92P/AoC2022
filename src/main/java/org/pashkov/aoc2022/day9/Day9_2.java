package org.pashkov.aoc2022.day9;

import org.pashkov.aoc2022.util.FileReaderImpl;

import java.util.*;

public class Day9_2 {
    //2230 no
//2440 to high
    //1380 to low
    //incorect2430
    private static Set<String> tailTrack = new LinkedHashSet<>();

    public static void main(String[] args) {
        int[] head = new int[]{15, 15};
        List<int[]> tails = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            tails.add(new int[]{15, 15});
        }

        List<int[]> headTrack = new LinkedList<>();
        for (String lineCommand : getFileInput()) {
            String[] commandArr = lineCommand.trim().split("\\s");
            String direction = commandArr[0];
            String numberOfSteps = commandArr[1];
            for (int i = 0; i < Integer.parseInt(numberOfSteps); i++) {
                head = adjustHeadPosition(direction, head);
                headTrack.add(head);
                int count = 0;
                while (count < 10) {
                    int[] tempHead = head;
                    if (count > 0) {
                        tempHead = tails.get(count - 1);
                    }
                    int[] tailPart = tails.get(count);

                    if (!checkIfCordsAreTouching(tempHead,tailPart)) {
                        tailPart = adoptAllTailsPosToCurrentHeadPosition(tempHead, tails, tailPart, count);
                    } else {
                        tailTrack.add(tailPart[0] + ":" + tailPart[1]);
                        break;
                    }
                    if (count == 9) {
                        tailTrack.add(tailPart[0] + ":" + tailPart[1]);
                    }
                    count++;
                }
            }
        }
        System.out.println((long) tailTrack.size());
        System.out.println(tailTrack);
        createMapWithResultTrack();
    }

    private static int[] adoptAllTailsPosToCurrentHeadPosition(int[] head, List<int[]> tails, int[] tailUnderCheck, int position) {
            tailUnderCheck = tailCatchUp(head, tailUnderCheck);
            tails.set(position, tailUnderCheck);
        return tailUnderCheck;
    }

    private static int[] tailCatchUp(int[] head, int[] tail) {
        int diffX = head[0] - tail[0];
        int diffY = head[1] - tail[1];
        return new int[]{(int) (tail[0] + Math.signum(diffX)), (int) (tail[1] + Math.signum(diffY))};
    }

    private static boolean checkIfCordsAreTouching(int[] head, int[] tail) {
        int diffX = head[0] - tail[0];
        int diffY = head[1] - tail[1];
        return Math.abs(diffX) > 1 || Math.abs(diffY) > 1 ? false : true;
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
        return FileReaderImpl.readEachLinesFromFile("day9-1e.txt");
    }

    private static String[][] createMapWithResultTrack() {
        int size = tailTrack.stream()
                .map(s -> {
                    String[] tempArr = s.split(":");
                    int a = Integer.parseInt(tempArr[0]);
                    int b = Integer.parseInt(tempArr[1]);
                    return Math.max(a, b);
                }).mapToInt(Integer::valueOf)
                .max().getAsInt();

        String[][] map = new String[size][size];
        for (String[] row : map)
            Arrays.fill(row, ".");
        for (String strCords : tailTrack) {
            String[] split = strCords.split(":");
            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);
            map[map.length - y][x - 1] = "#";
        }
        print2DArray(map);
        return map;
    }

    private static void print2DArray(String[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }
}
