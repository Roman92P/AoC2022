package org.pashkov.aoc2022.day1;

import org.pashkov.aoc2022.util.FileReaderImpl;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Roman Pashkov created on 01.12.2022 inside the package - org.pashkov.aoc2022.day1
 */
public class Day1_2 {
    public static void main(String[] args) {
        List<String> fileInput = getFileInput();
        String join = String.join(",", fileInput);
        String[] split = join.split(",,");
        List<Integer> collect = Arrays.stream(split)
                .mapToInt(value -> {
                    String[] split1 = value.split(",");
                    return Arrays.stream(split1)
                            .mapToInt(Integer::parseInt)
                            .sum();
                })
                .boxed()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        List<Integer> integers = collect.subList(0, 3);
        System.out.println(integers.stream().mapToInt(Integer::intValue).sum());
    }
    private static List<String> getFileInput() {
        return FileReaderImpl.readEachLinesFromFile("day1-1.txt");
    }
}
