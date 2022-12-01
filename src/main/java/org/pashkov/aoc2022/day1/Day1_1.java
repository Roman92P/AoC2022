package org.pashkov.aoc2022.day1;

import org.pashkov.aoc2022.util.FileReaderImpl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Roman Pashkov created on 01.12.2022 inside the package - org.pashkov.aoc2022.day1
 */
public class Day1_1 {
    public static void main(String[] args) {
        List<String> fileInput = getFileInput();
        String[] s = fileInput.toString().split("\\s,\\s");
        List<Integer> collect = Arrays.stream(s).mapToInt(value -> {
            String[] split = value.replaceAll("[\\]\\[\\s]","") .split(",");
            return Arrays.stream(split)
                    .mapToInt(Integer::parseInt)
                    .sum();
        }).boxed().collect(Collectors.toList());
        System.out.println(collect.stream().mapToInt(Integer::intValue).max().getAsInt());
    }

    private static List<String> getFileInput() {
        return FileReaderImpl.readEachLinesFromFile("day1-1.txt");
    }
}
