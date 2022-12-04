package org.pashkov.aoc2022.day4;

import org.pashkov.aoc2022.util.FileReaderImpl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Roman Pashkov created on 04.12.2022 inside the package - org.pashkov.aoc2022.day4
 */
public class Day4_2 {
    public static void main(String[] args) {
        List<String> fileInput = getFileInput();
        int sumOfOverlappingPairs = fileInput.stream()
                .mapToInt(Day4_2::checkIfPairIsOverlapping)
                .sum();
        System.out.println("Your result is: " + sumOfOverlappingPairs);
    }

    private static int checkIfPairIsOverlapping(String value) {
        String[] twoElvesFromPair = value.split(",");
        String[] firstElfSection = twoElvesFromPair[0].split("-");
        String[] secondElfSection = twoElvesFromPair[1].split("-");
        int sizeOfFirstElfSection = calculateSectionSize(twoElvesFromPair[0]);
        int sizeOfSecondElfSection = calculateSectionSize(twoElvesFromPair[1]);

        String allFirstSection = generateSection(twoElvesFromPair[0]).replaceAll("[\\]\\[\\s]","");
        String allSecondSection = generateSection(twoElvesFromPair[1]).replaceAll("[\\]\\[\\s]","");

        String[] sectionOneArr = allFirstSection.split(",");
        String[] sectionTwoArr = allSecondSection.split(",");

        if (sizeOfFirstElfSection > sizeOfSecondElfSection) {
            if (Arrays.stream(sectionOneArr).anyMatch(s -> s.equals(secondElfSection[0])) ||
                    Arrays.stream(sectionOneArr).anyMatch(s -> s.equals(secondElfSection[1]))) {
                return 1;
            }
        } else {
            if (Arrays.stream(sectionTwoArr).anyMatch(s -> s.equals(firstElfSection[0])) ||
                    Arrays.stream(sectionTwoArr).anyMatch(s -> s.equals(firstElfSection[1]))) {
                return 1;
            }
        }
        return 0;
    }

    private static String generateSection(String s) {
        String[] section = s.split("-");
        return Arrays.toString(IntStream.rangeClosed(Integer.parseInt(section[0]), Integer.parseInt(section[1]))
                .toArray());
    }

    private static List<String> getFileInput() {
        return FileReaderImpl.readEachLinesFromFile("day4-1.txt");
    }

    private static int calculateSectionSize(String str) {
        String[] section = str.split("-");
        return IntStream.rangeClosed(Integer.parseInt(section[0]), Integer.parseInt(section[1])).sum();
    }
}
