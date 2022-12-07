package org.pashkov.aoc2022.day6;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Day6_2 {
    public static void main(String[] args) {
        readCharByChar();
    }

    private static void readCharByChar() {
        try (InputStreamReader inputStream = getInputStreamFromFile()) {
            BufferedReader br = new BufferedReader(inputStream);
            String str = "";
            int indexOfMarker = 0;
            while (br.ready()) {
                char c = (char) br.read();
                str = str + c;
                indexOfMarker++;
                if (str.length() >= 14) {
                    String[] tempStr = str.split("");
                    Set<String> uniqueStateOfInput = Arrays.stream(tempStr).collect(Collectors.toCollection(LinkedHashSet::new));
                    if (uniqueStateOfInput.size() < str.length()) {
                        str = str.substring(1);
                    } else {
                        System.out.println("Marker index is: " + indexOfMarker);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static InputStreamReader getInputStreamFromFile() throws FileNotFoundException {
        File file = new File("AoC2022/day6-1.txt");
        return new InputStreamReader(new FileInputStream(file));
    }

}
