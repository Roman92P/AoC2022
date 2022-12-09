package org.pashkov.aoc2022.day7;

import org.pashkov.aoc2022.util.FileReaderImpl;

import java.util.*;
import java.util.stream.Collectors;

public class Day7_2 {
    private static final String COMMAND = "\\$ cd.*";
    private static final String ROOT_DIR = "$ cd /";
    private static final String LIST_DIR_ELEMENTS = "$ ls";
    private static final String GO_LEVEL_IN_REGEX = "\\$ cd\\s[a-z]+";
    private static final String GO_LEVEL_UP = "$ cd ..";
    private static final long MEMORY_SIZE = 70000000;
    private static final long NEEDED_MEMORY_FOR_UPDATE = 30000000;


    public static void main(String[] args) {

        Map<String, List<String>> systemStructureMap = getSystemStructureMap();

        LinkedHashMap<String, List<String>> collect = systemStructureMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        List<Long> dirSums = systemStructureMap.keySet()
                .stream()
                .mapToLong(strings -> calculateDirSizeSums(strings, systemStructureMap))
                .boxed()
                .sorted()
                .collect(Collectors.toList());

        System.out.println("Size of outermost directory: " + dirSums.get(dirSums.size()-1));

        long usedSpace = dirSums.get(dirSums.size()-1);
        long memDifferance = MEMORY_SIZE - usedSpace;

        System.out.println("You need to free: " + memDifferance);

        List<Long> dirsToBeDeleted = dirSums.stream()
                .filter(aLong -> memDifferance + aLong >= NEEDED_MEMORY_FOR_UPDATE)
                .sorted()
                .collect(Collectors.toList());

        System.out.println("Please remove dir with this size: " + dirsToBeDeleted.get(0));
    }
    private static Long calculateDirSizeSums(String s, Map<String, List<String>> systemStructureMap) {
        List<String> dirContent = systemStructureMap.get(s);
        long dirSum = 0;
        for (String fileOrDir : dirContent) {
            String str = fileOrDir.trim();
            String[] tempArr = str.split("\\s");
            try {
                long l = Long.parseLong(tempArr[0]);
                dirSum += l;
            } catch (IllegalArgumentException e) {
                String pathToFind = "";
                if (s.equals("/")) {
                    pathToFind = "/" + tempArr[1];
                } else {
                    pathToFind = s + "/" + tempArr[1];
                }
                dirSum += calculateDirSizeSums(pathToFind, systemStructureMap);
            }
        }
        return dirSum;
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
                } else if (commandLine.equals(ROOT_DIR)) {
                    currentLocation = "/";
                }
            } else if (commandLine.equals(LIST_DIR_ELEMENTS)) {
                List<String> content = new ArrayList<>();
                String nextContentElement = "";
                while (true) {
                    i++;
                    nextContentElement = fileInput.get(i);
                    content.add(nextContentElement);

                    if (i + 1 >= fileInput.size() || nextContentElement.matches(COMMAND)) {
                        if (nextContentElement.matches(COMMAND)) {
                            i = i - 1;
                            content = content.subList(0, content.size() - 1);
                        }
                        break;
                    }
                }
                systemDirLevelsMap.put(currentLocation, content);
            }
        }
        return systemDirLevelsMap;
    }

    private static String goOutFromCurrentPath(String currentLocation) {
        String[] pathArr = currentLocation.split("/");
        String[] newPath = new String[pathArr.length - 1];
        System.arraycopy(pathArr, 0, newPath, 0, pathArr.length - 1);
        return Arrays.stream(newPath).collect(Collectors.joining("/"));
    }

    private static List<String> getFileInput() {
        return FileReaderImpl.readEachLinesFromFile("day7-1.txt");
    }
}
