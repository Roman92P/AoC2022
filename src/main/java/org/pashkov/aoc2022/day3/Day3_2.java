package org.pashkov.aoc2022.day3;

import org.pashkov.aoc2022.util.FileReaderImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Roman Pashkov created on 03.12.2022 inside the package - org.pashkov.aoc2022.day3
 */
public class Day3_2 {
    private static String EMPTY_STRING = "";

    public static void main(String[] args) {
        List<String> fileInput = getFileInput();
        List<String> optimizedInput = fileInput.stream()
                .map(s -> {
                    String[] split = s.split("");
                    return Arrays.stream(split).collect(Collectors.toSet()).stream().sorted().collect(Collectors.joining());
                }).collect(Collectors.toList());

        int count = 0;
        List<String> groups = new LinkedList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : optimizedInput) {
            stringBuilder.append(s);
            stringBuilder.append(",");
            count++;
            if (count == 3) {
                String newGroup = stringBuilder.toString();
                groups.add(newGroup.substring(0, newGroup.length()-1));
                stringBuilder = new StringBuilder();
                count = 0;
            }
        }

        List<String> groupTypes = groups.stream()
                .map(s -> findGroupCommonElement(s))
                .collect(Collectors.toList());

        System.out.println("Here: "+ groupTypes);

        int sum = 0;
        sum = groupTypes.stream()
                .mapToInt(Day3_2::getPositionInAlphabet)
                .sum();
        System.out.println("Result is: " + sum);
    }
    private static String findGroupCommonElement(String strings) {
        String[] split = strings.split(",");
        for (int i = 0; i < split[0].length(); i++) {
            char c = split[0].charAt(i);
            String typeToCheck = Character.toString(c);
            String regex = ".*" + typeToCheck + ".*";
            if (split[1].matches(regex) && split[2].matches(regex)) {
                System.out.printf("%s matches %s and %s.", typeToCheck, split[1], split[2]);
                return typeToCheck;
            }
        }
        return EMPTY_STRING;
    }

    private static List<String> getFileInput() {
        return FileReaderImpl.readEachLinesFromFile("day3-1.txt");
    }

    private static int getPositionInAlphabet(String letter) {
        return Character.isUpperCase(letter.charAt(0)) ?
                Character.hashCode(letter.charAt(0)) - 65 + 27 :
                Character.hashCode(letter.charAt(0)) - 96;
    }
}
