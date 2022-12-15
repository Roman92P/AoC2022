package org.pashkov.aoc2022.day9;

import org.pashkov.aoc2022.util.FileReaderImpl;

import java.util.*;

/**
 * Roman Pashkov created on 15.12.2022 inside the package - org.pashkov.aoc2022.day9
 */
public class Day9 {

    private static Set<String> nineTail = new LinkedHashSet<>();
    private static List<String> allTail = Arrays.asList(
            "15:15",
            "15:15",
            "15:15",
            "15:15",
            "15:15",
            "15:15",
            "15:15",
            "15:15",
            "15:15"
    );

    private static String head = "15:15";

    private static Map<String, Integer> rowMove = new HashMap<>() {
        {
            put("U", 0);
            put("D", 0);
            put("L", -1);
            put("R", 1);

        }
    };

    private static Map<String, Integer> colMove = new HashMap<>() {
        {
            put("U", 1);
            put("D", -1);
            put("L", 0);
            put("R", 0);

        }
    };

    public static void main(String[] args) {
        for (String move : getFileInput()) {
            System.out.println("At this point head is: " + head);
            System.out.println("Move: " + move);
            String[] split = move.trim().split("\\s");
            String[] headArr = head.split(":");

            for (int i = 0; i < Integer.parseInt(split[1]); i++) {
                int hx = Integer.parseInt(headArr[0]) + rowMove.get(split[0]);
                int hy = Integer.parseInt(headArr[1]) + colMove.get(split[0]);
                headArr[0] = String.valueOf(hx);
                headArr[1] = String.valueOf(hy);

                int[] tempHeadArr = {hx, hy};

                int count = 0;
                while (count < 9) {
                    String s = allTail.get(count);
                    String[] tailPartCords = s.split(":");
                    int[] tailCordsArr = {Integer.parseInt(tailPartCords[0]), Integer.parseInt(tailPartCords[1])};
                    boolean headTailConnected = checkIfCordsAreTouching(tempHeadArr, tailCordsArr);
                    if (headTailConnected) {
                        break;
                    }
                    tailCordsArr = tailCatchUp(tempHeadArr, tailCordsArr);
                    tempHeadArr = tailCordsArr;
                    allTail.set(count, tempHeadArr[0] + ":" + tempHeadArr[1]);
                    count++;

                    nineTail.add(allTail.get(8));
                }
                head = headArr[0] + ":" + headArr[1];
            }
            System.out.println("Head at the End: " + head);
            System.out.println("new tails: " + allTail);
        }
        System.out.println(nineTail.size());
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

    private static List<String> getFileInput() {
        return FileReaderImpl.readEachLinesFromFile("day9-1.txt");
    }
}
