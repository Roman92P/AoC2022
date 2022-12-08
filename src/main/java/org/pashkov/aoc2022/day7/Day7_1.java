package org.pashkov.aoc2022.day7;

import org.pashkov.aoc2022.util.FileReaderImpl;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Roman Pashkov created on 08.12.2022 inside the package - org.pashkov.aoc2022.day7
 */
public class Day7_1 {
    private static final String COMMAND = "\\$ cd.*";
    private static final String ROOT_DIR = "$ cd /";
    private static final String LIST_DIR_ELEMENTS = "$ ls";
    private static final String GO_LEVEL_IN_REGEX = "\\$ cd\\s[a-z]+";
    private static final String GO_LEVEL_UP = "$ cd ..";
    private static final String DIR_RECOGNIZE_REGEX = "dir\\s[a-z]+";
    private static final String FILE_RECOGNIZE_REGEX = "\\d+\\s[a-z](\\.[a-z]{3})*";

    public static void main(String[] args) {

        Map<String, List<String>> systemStructureMap = getSystemStructureMap();

        LinkedHashMap<String, List<String>> collect = systemStructureMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        for (Map.Entry<String, List<String>> entry : collect.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

        System.out.println("Starting summing");
        List<Long> dirSums = calculateSumsForDirs(systemStructureMap);

        List<Long> sortedFilteredSums = dirSums.stream()
                .sorted()
                .filter(aLong -> aLong <= 100000)
                .collect(Collectors.toList());

        System.out.println(sortedFilteredSums.get(sortedFilteredSums.size() - 1));
    }

    private static List<Long> calculateSumsForDirs(Map<String, List<String>> systemStructureMap) {
        List<Long> result = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : systemStructureMap.entrySet()) {
            List<String> dirContent = entry.getValue();
            long l = convertContentToLongSize(entry.getKey(), dirContent, systemStructureMap);
            System.out.println("Added long: " + l);
            result.add(l);
        }
        return result;
    }

    private static long convertContentToLongSize(String key, List<String> content, Map<String, List<String>> systemStructureMap) {

        long sum = 0;
        for (String c : content){
            String[] contentArr = c.split(",");
            long dirSumSize = 0;
            for (int i = 0; i < contentArr.length; i++) {
                String dirOrFile = contentArr[i].trim();
                if (dirOrFile.matches(DIR_RECOGNIZE_REGEX)) {
                    String dirName = dirOrFile.split("\\s")[1];
                    String dirPath = key + "/" + dirName;
                    if (dirPath.matches("[\\/\\/]{2}.*")) {
                        dirPath = dirPath.substring(1);
                    }
                    System.out.println("Trying to get: " + dirPath);
                    List<String> strings = systemStructureMap.get(dirPath);
                    System.out.println("Received: " + strings);
                    convertContentToLongSize(dirPath, strings, systemStructureMap);
                } else if (dirOrFile.matches(FILE_RECOGNIZE_REGEX)) {
                    String[] fileArr = dirOrFile.split("\\s");
                    sum = Long.parseLong(dirSumSize + fileArr[0]);
                }
            }

        }
//        return content.stream()
//                .mapToLong(value -> {
//                    String[] contentArr = value.split(",");
//                    long dirSumSize = 0;
//                    for (int i = 0; i < contentArr.length; i++) {
//                        String dirOrFile = contentArr[i].trim();
//                        if (dirOrFile.matches(DIR_RECOGNIZE_REGEX)) {
//                            String dirName = dirOrFile.split("\\s")[1];
//                            String dirPath = key + "/" + dirName;
//                            if (dirPath.matches("[\\/\\/]{2}.*")) {
//                                dirPath = dirPath.substring(1);
//                            }
//                            System.out.println("Trying to get: " + dirPath);
//                            List<String> strings = systemStructureMap.get(dirPath);
//                            System.out.println("Received: " + strings);
//                            convertContentToLongSize(dirPath, strings, systemStructureMap);
//                        } else if (dirOrFile.matches(FILE_RECOGNIZE_REGEX)) {
//                            String[] fileArr = dirOrFile.split("\\s");
//                            dirSumSize = Long.parseLong(dirSumSize + fileArr[0]);
//                        }
//                    }
//                    return dirSumSize;
//                }).sum();
        return sum;
    }

    private static Map<String, List<String>> getSystemStructureMap() {
        List<String> fileInput = getFileInput();
        String currentLocation = "";
        Map<String, List<String>> systemDirLevelsMap = new HashMap<>();
        for (int i = 0; i < fileInput.size(); i++) {
            String commandLine = fileInput.get(i);
            if (commandLine.matches(COMMAND)) {
                if (commandLine.matches(GO_LEVEL_IN_REGEX)) {
                    String[] tempArrCommandLine = commandLine.split("\\s");
                    currentLocation = currentLocation + "/" + tempArrCommandLine[2];
                    if (currentLocation.matches("[\\/\\/]{2}.*")) {
                        currentLocation = currentLocation.substring(1);
                    }
                } else if (commandLine.equals(GO_LEVEL_UP)) {
                    currentLocation = goOutFromCurrentPath(currentLocation);
                    System.out.println("Went out: "+ currentLocation);
                } else if (commandLine.equals(ROOT_DIR)) {
                    currentLocation = "/";
                }
            } else if (commandLine.equals(LIST_DIR_ELEMENTS)) {
                List<String> content = new ArrayList<>();
                String nextContentElement = "";
                System.out.println("Before adding content I am here: " + currentLocation);
                while (true) {
                    i++;
                    nextContentElement = fileInput.get(i);
                    if (i + 1 >= fileInput.size() || nextContentElement.matches(COMMAND)) {
                        if (nextContentElement.matches(COMMAND)) i = i - 1;
                        break;
                    }
                    content.add(nextContentElement);
                }
                systemDirLevelsMap.put(currentLocation, content);
            }
        }
        return systemDirLevelsMap;
    }

    private static String goOutFromCurrentPath(String currentLocation) {
        String[] pathArr = currentLocation.split("/");
        String[] newPath = new String[pathArr.length - 1];
        System.arraycopy(pathArr,0, newPath, 0, pathArr.length-1);
        return Arrays.stream(newPath).collect(Collectors.joining("/"));
    }

    private static List<String> getFileInput() {
        return FileReaderImpl.readEachLinesFromFile("day7-1e.txt");
    }
}
