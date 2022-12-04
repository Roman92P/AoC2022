package org.pashkov.aoc2022.day4;

import org.pashkov.aoc2022.util.FileReaderImpl;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Roman Pashkov created on 04.12.2022 inside the package - org.pashkov.aoc2022.day4
 */
public class Day4_1 {
    public static void main(String[] args) {
        List<String> fileInput = getFileInput();
        int sumOfOverlappingPairs = fileInput.stream()
                .mapToInt(Day4_1::checkIfPairIsOverlapping)
                .sum();
        System.out.println("Your result is: " + sumOfOverlappingPairs);
    }

    private static int checkIfPairIsOverlapping(String value) {
        String[] twoElvesFromPair = value.split(",");
        String[] firstElfSection = twoElvesFromPair[0].split("-");
        String[] secondElfSection = twoElvesFromPair[1].split("-");
        int sizeOfFirstElfSection = calculateSectionSize(twoElvesFromPair[0]);
        int sizeOfSecondElfSection = calculateSectionSize(twoElvesFromPair[1]);
        int sectionStart;
        int sectionEnd;
        int sectionStartSecondElf;
        int sectionEndSecondElf;
        if (sizeOfFirstElfSection < sizeOfSecondElfSection) {
            sectionStart = Integer.parseInt(firstElfSection[0]);
            sectionEnd = Integer.parseInt(firstElfSection[firstElfSection.length - 1]);
            sectionStartSecondElf = Integer.parseInt(secondElfSection[0]);
            sectionEndSecondElf = Integer.parseInt(secondElfSection[secondElfSection.length - 1]);
        } else {
            sectionStart = Integer.parseInt(secondElfSection[0]);
            sectionEnd = Integer.parseInt(secondElfSection[secondElfSection.length - 1]);
            sectionStartSecondElf = Integer.parseInt(firstElfSection[0]);
            sectionEndSecondElf = Integer.parseInt(firstElfSection[firstElfSection.length - 1]);
        }
        if (sectionStart >= sectionStartSecondElf && sectionEnd <= sectionEndSecondElf) {
            return 1;
        }
        return 0;
    }

    private static List<String> getFileInput() {
        return FileReaderImpl.readEachLinesFromFile("day4-1.txt");
    }

    private static int calculateSectionSize(String str) {
        String[] section = str.split("-");
        return IntStream.rangeClosed(Integer.parseInt(section[0]), Integer.parseInt(section[1])).sum();
    }
}
