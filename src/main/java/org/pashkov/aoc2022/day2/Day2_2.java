package org.pashkov.aoc2022.day2;

import org.pashkov.aoc2022.util.FileReaderImpl;

import java.util.List;
import java.util.Map;

/**
 * Roman Pashkov created on 02.12.2022 inside the package - org.pashkov.aoc2022.day2
 */
public class Day2_2 {

    private final static int MAX_HASH = 90;

    private static final Map<String, Integer> WIN_LOSE_POINTS_MAPPER = Map.of(
            "X", 0,
            "Y", 3,
            "Z", 6
    );
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
                .mapToInt(Day2_2::calculateScoreForRound)
                .sum();

        System.out.println("Your score is: " + sum);
    }

    private static int calculateScoreForRound(String value) {
        String[] valueArr = value.split("\\s");
        int[] checkIfWinAndScoreArr = checkIfWinDrawOrLose(valueArr);
        return checkIfWinAndScoreArr[0] + checkIfWinAndScoreArr[1];
    }

    private static int[] checkIfWinDrawOrLose(String[] valueArr) {
        int opponent = Character.hashCode(valueArr[0].charAt(0));
        int adviceOfHowToPlay = Character.hashCode(valueArr[1].charAt(0));

        int howPlayerShouldPlay = WIN_LOSE_POINTS_MAPPER.get(valueArr[1]);

        int[] roundResultAndRoundScore = new int[2];

        roundResultAndRoundScore[0] = howPlayerShouldPlay;

        int result = 0;
        if (howPlayerShouldPlay == 6) { //win
            int a = opponent + 24 > 90 ? 21 : 24;
            result = opponent + a;
        } else if (howPlayerShouldPlay == 3) { //draw
            result = opponent + 23;
        } else { //lose
            int a = opponent + 25 > 90 ? 22 : 25;
            result = opponent + a;
        }
        String shape = Character.toString(result);
        roundResultAndRoundScore[1] = SHAPE_SCORES.get(shape);
        return roundResultAndRoundScore;
    }
    //op - pl 88 -67 won 21 , 90 - 66 won 24, 89 - 65 won 24; (21 || 24 win), 23 draw; 22 || 25lose,
    private static List<String> getFileInput() {
        return FileReaderImpl.readEachLinesFromFile("day2-1.txt");
    }
}
