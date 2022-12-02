package org.pashkov.aoc2022.day2;

import org.pashkov.aoc2022.util.FileReaderImpl;

import java.util.*;

/**
 * Roman Pashkov created on 02.12.2022 inside the package - org.pashkov.aoc2022.day2
 */
public class Day2_1 {

    private static final Map<String, Integer> SHAPE_SCORES = Map.of(
            "A", 1,
            "B", 2,
            "C", 3,
            "X", 1,
            "Y", 2,
            "Z", 3
    );
    private final int WIN_SCORE = 6;

    private final int DRAW_SCORE = 3;

    private final int LOST_SCORE = 0;

    public static void main(String[] args) {
        List<String> fileInput = getFileInput();
        System.out.println("Got input: " + fileInput);

        int sum = fileInput.stream()
                .mapToInt(Day2_1::calculateScoreForRound)
                .sum();

        System.out.println("Your score is: " + sum);
    }

    private static int calculateScoreForRound(String value) {
        String[] valueArr = value.split("\\s");
        int checkIfWin = checkIfWinDrawOrLose(valueArr);
        switch (checkIfWin) {
            case 0:
                return SHAPE_SCORES.get(valueArr[1]);
            case 1:
                return 3 + SHAPE_SCORES.get(valueArr[1]);
            case 2:
                return 6 + SHAPE_SCORES.get(valueArr[1]);
            default: return 0;
        }
    }

    private static int checkIfWinDrawOrLose(String[] valueArr) {
        int opponent = Character.hashCode(valueArr[0].charAt(0));
        int player = Character.hashCode(valueArr[1].charAt(0));
        int result = player - opponent;
        if (result == 21 || result == 24) {
            return 2;
        } else if (result == 23) {
            return 1;
        } else {
            return 0;
        }
    }

    private static List<String> getFileInput() {
        return FileReaderImpl.readEachLinesFromFile("day2-1.txt");
    }

}
