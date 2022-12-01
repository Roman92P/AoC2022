package org.pashkov.aoc2022.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Roman Pashkov created on 01.12.2022 inside the package - org.pashkov.aoc2022.util.impl
 */
public class FileReaderImpl{

    public static List<String> readEachLinesFromFile(String filename) {
        Path path = Paths.get(filename);
        List<String> result;
        try {
            result = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
