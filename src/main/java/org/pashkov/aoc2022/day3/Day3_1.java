package org.pashkov.aoc2022.day3;

import org.pashkov.aoc2022.util.FileReaderImpl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Roman Pashkov created on 03.12.2022 inside the package - org.pashkov.aoc2022.day3
 */
public class Day3_1 {

    private static String EMPTY_STRING = "";

    public static void main(String[] args) {
        List<String> fileInput = getFileInput();
        List<String[]> splitRucksacks = fileInput.stream()
                .map(s -> {
                    int length = s.length();
                    String s1 = s.substring(0, length / 2);
                    String s2 = s.substring(length / 2);
                    return new String[]{s1, s2};
                }).collect(Collectors.toList());
        //print split rucksack
        splitRucksacks
                .forEach(strings -> System.out.println(Arrays.toString(strings)));

        List<String> commonTypesInRucksacks = splitRucksacks.stream()
                .map(Day3_1::findCommonElements)
                .collect(Collectors.toList());

        int sum = commonTypesInRucksacks.stream()
                .mapToInt(Day3_1::getPositionInAlphabet)
                .sum();

        System.out.println("Result is: " + sum);
    }

    private static String findCommonElements(String[] strings) {
        String str1 = strings[0];
        String str2 = strings[1];
        String s1 = Arrays.stream(str1.split("")).collect(Collectors.toSet()).stream().sorted().collect(Collectors.joining());
        String s2 = Arrays.stream(str2.split("")).collect(Collectors.toSet()).stream().sorted().collect(Collectors.joining());
        for (int i = 0; i < s1.length(); i++) {
            char c = s1.charAt(i);
            String s = Character.toString(c);
            String regex = ".*" + s + ".*";
            if (s2.matches(regex)) {
                return s;
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
