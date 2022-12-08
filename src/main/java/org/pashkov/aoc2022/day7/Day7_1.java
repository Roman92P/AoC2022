package org.pashkov.aoc2022.day7;

import java.io.*;
import java.util.*;

/**
 * Roman Pashkov created on 07.12.2022 inside the package - org.pashkov.aoc2022.day7
 */
public class Day7_1 {
    private static final String COMMAND = "\\$.*";
    private static final String ROOT_DIR = "$ cd /";
    private static final String LIST_DIR_ELEMENTS = "$ ls";
    private static final String GO_LEVEL_IN_REGEX = "\\$ cd\\s[a-z]+";
    private static final String GO_LEVEL_UP = "$ cd ..";
    private static final String DIR_RECOGNIZE_REGEX = "dir\\s[a-z]+";
    private static final String FILE_RECOGNIZE_REGEX = "\\d+\\s[a-z](\\.[a-z]{3})*";

    public static void main(String[] args) {

        FileSystemRootElement fileSystemStructure = createFileSystemStructure();

        printFileSystemStructure(fileSystemStructure);

    }

    private static void printFileSystemStructure(FileSystemRootElement fileSystemStructure) {
        Set<FileSystemDirectory> rootDirectory = fileSystemStructure.getDirectories();
        FileSystemDirectory rootDir = rootDirectory.stream().findFirst().get();
        System.out.println(rootDir.getDirectories());
        System.out.println(rootDir.getDirectoryFiles());
        printDirectoryContent(rootDir);
    }

    private static void printDirectoryContent(FileSystemDirectory directoryToBePrinted) {
        Set<FileSystemDirectory> dirs = directoryToBePrinted.getDirectories();
        Set<FileSystemFile> files = directoryToBePrinted.getDirectoryFiles();
        files.stream().forEach(FileSystemFile::toString);
    }

    private static FileSystemRootElement createFileSystemStructure() {
        FileSystemRootElement fileSystemRootElement = new FileSystemRootElement();
        String currentPath = "";
        String gotCommand = "";
        int stepCount = 0;
        try (InputStreamReader inputStream = getInputStreamFromFile()) {
            try (BufferedReader br = new BufferedReader(inputStream)) {
                while (br.ready()) {
                    stepCount++;
                    String inputLine = br.readLine();
                    System.out.println("Here " + inputLine + " " + gotCommand);
                    if (inputLine.equals(ROOT_DIR)) {
                        FileSystemDirectory fileSystemDirectory = new FileSystemDirectory("/");
                        fileSystemRootElement.setDirectories(Set.of(fileSystemDirectory));
                        continue;
                    }
                    if (inputLine.matches(GO_LEVEL_IN_REGEX) || gotCommand.equals(GO_LEVEL_IN_REGEX)) {
                        System.out.println("Going level in");
                        if (currentPath.equals("/")) {
                            currentPath = currentPath + inputLine.split("\\s")[2];
                        } else {
                            currentPath = currentPath + "/" + inputLine.split("\\s")[2];
                        }
                    }
                    if (GO_LEVEL_UP.equals(inputLine) || gotCommand.equals(GO_LEVEL_UP)) {
                        System.out.println("Going level out");
                        if (!currentPath.equals("/")) {
                            if (currentPath.substring(0, currentPath.length() - 2).isEmpty()) {
                                currentPath = "/";
                            } else {
                                currentPath = currentPath.substring(0, currentPath.length() - 2);
                            }
                        }
                    }
                    if (inputLine.equals(LIST_DIR_ELEMENTS) || currentPath.equals("/")) {
                        List<String> filesAndDirsToAdd = new ArrayList<>();
                        while (true) {
                            String lineAfterList = br.readLine();
                            if (lineAfterList.equals(GO_LEVEL_UP) || lineAfterList.matches(GO_LEVEL_IN_REGEX)) {
                                gotCommand = lineAfterList;
                                break;
                            }
                            filesAndDirsToAdd.add(lineAfterList);
                        }
                        System.out.println(filesAndDirsToAdd);
                        addContentToCurrentDirectory(fileSystemRootElement, filesAndDirsToAdd, currentPath);
                    }
                    if (inputLine.isEmpty()) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(fileSystemRootElement);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        return fileSystemRootElement;
    }

    private static void addContentToCurrentDirectory(FileSystemRootElement fileSystemRootElement, List<String> filesAndDirsToAdd, String currentPath) {
        FileSystemDirectory directory = null;
        if (currentPath.equals("/") || currentPath.isEmpty()) {
           FileSystemDirectory rootDir = new FileSystemDirectory("/");
           fileSystemRootElement.setDirectories(Set.of(rootDir));
        } else {
            directory = getWorkingDirectory(currentPath, fileSystemRootElement);
        }
        System.out.println("To what directory to add: " + directory + " if current path is: " + currentPath);
        for (String fileOrDirectory : filesAndDirsToAdd) {
            if (fileOrDirectory.matches(DIR_RECOGNIZE_REGEX)) {
                Set<FileSystemDirectory> directories = null;
                if (Optional.ofNullable(directory.getDirectories()).isEmpty()) {
                    directories = new HashSet<>();
                } else {
                    directories = directory.getDirectories();
                }
                FileSystemDirectory directory1 = new FileSystemDirectory(fileOrDirectory.substring(fileOrDirectory.length() - 1));
                directories.add(directory1);
                directory.setDirectories(directories);
                System.out.println("saved dir");
            } else if (fileOrDirectory.matches(FILE_RECOGNIZE_REGEX)) {
                Set<FileSystemFile> files = directory.getDirectoryFiles();
                if (files == null) {
                    files = new HashSet<>();
                }
                String[] fileConstructorArr = fileOrDirectory.split("\\s");
                FileSystemFile file = new FileSystemFile(fileConstructorArr[1], Long.parseLong(fileConstructorArr[0]));
                files.add(file);
                directory.setDirectoryFiles(files);
            }
        }
    }


    private static FileSystemDirectory getWorkingDirectory(String currentPath, FileSystemRootElement fileSystemRootElement) {
        System.out.println("Get work dir for current path: " + currentPath);
        Optional<FileSystemDirectory> dir = fileSystemRootElement.getDirectories()
                .stream()
                .filter(fileSystemDirectory -> fileSystemDirectory.getDirName().equals(currentPath.substring(currentPath.length() - 1)))
                .findFirst();
//        if (dir.isEmpty()) {
//            FileSystemDirectory fileSystemDirectory = new FileSystemDirectory(currentPath.substring(currentPath.length() - 1));
//            FileSystemDirectory parentDirectory = getWorkingDirectory(currentPath.substring(currentPath.length() - 2, currentPath.length() - 1), fileSystemRootElement);
//            Set<FileSystemDirectory> parentDirs = parentDirectory.getDirectories();
//            parentDirs.add(fileSystemDirectory);
//            return fileSystemDirectory;
//        }

        return dir.get();
    }

    private static InputStreamReader getInputStreamFromFile() throws FileNotFoundException {
        File file = new File("AoC2022/day7-1e.txt");
        return new InputStreamReader(new FileInputStream(file));
    }

    //inner helper classes
    static class FileSystemFile {
        public FileSystemFile(String fileName, long size) {
            this.fileName = fileName;
            this.size = size;
        }

        private String fileName;
        private long size;

        public FileSystemFile() {

        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        @Override
        public String toString() {
            return String.format("    - %s (file, size=%d)", this.fileName, this.size);
        }
    }

    static class FileSystemDirectory {

        public FileSystemDirectory(Set<FileSystemFile> directoryFiles, Set<FileSystemDirectory> directories) {
            this.directoryFiles = directoryFiles;
            this.directories = directories;
        }

        public FileSystemDirectory(Set<FileSystemFile> directoryFiles) {
            this.directoryFiles = directoryFiles;
        }

        public FileSystemDirectory() {
        }

        public FileSystemDirectory(String dirName, Set<FileSystemFile> directoryFiles, Set<FileSystemDirectory> directories) {
            this.dirName = dirName;
            this.directoryFiles = directoryFiles;
            this.directories = directories;
        }

        public FileSystemDirectory(String dirName) {
            this.dirName = dirName;
        }

        private String dirName;
        private Set<FileSystemFile> directoryFiles = new HashSet<>();

        private Set<FileSystemDirectory> directories = new HashSet<>();

        public Set<FileSystemFile> getDirectoryFiles() {
            return directoryFiles;
        }

        public void setDirectoryFiles(Set<FileSystemFile> directoryFiles) {
            this.directoryFiles = directoryFiles;
        }

        public Set<FileSystemDirectory> getDirectories() {
            return Objects.requireNonNullElseGet(this.directories, HashSet::new);
        }

        public void setDirectories(Set<FileSystemDirectory> directories) {
            this.directories = directories;
        }

        public String getDirName() {
            return dirName;
        }

        public void setDirName(String dirName) {
            this.dirName = dirName;
        }

        @Override
        public String toString() {
            return "  - " + this.dirName + " " + "(dir)";
        }
    }

    static class FileSystemRootElement {
        private Set<FileSystemDirectory> directories = new HashSet<>();
        private Set<FileSystemFile> files = new HashSet<>();

        public FileSystemRootElement() {
        }

        public FileSystemRootElement(Set<FileSystemDirectory> directories, Set<FileSystemFile> files) {
            this.directories = directories;
            this.files = files;
        }

        public Set<FileSystemDirectory> getDirectories() {
            return directories;
        }

        public void setDirectories(Set<FileSystemDirectory> directories) {
            this.directories = directories;
        }

        public Set<FileSystemFile> getFiles() {
            return files;
        }

        public void setFiles(Set<FileSystemFile> files) {
            this.files = files;
        }

        @Override
        public String toString() {
            return " - / (dir)";
        }
    }

    //    private static FileSystemRootElement createFileSystemStructure() {
//        FileSystemRootElement fileSystemRootElement = new FileSystemRootElement();
//        String currentPath = "";
//        String gotCommand = "";
//        int stepCount = 0;
//        try (InputStreamReader inputStream = getInputStreamFromFile()) {
//            try (BufferedReader br = new BufferedReader(inputStream)) {
//                while (br.ready()) {
//                    stepCount++;
//                    System.out.println("Step count is: " + stepCount);
//                    String inputLine = gotCommand;
//                    if (inputLine.equals("")) {
//                        inputLine = br.readLine();
//                    }
//                    System.out.println("Current input line is: " + inputLine);
//                    System.out.println("Current directory is: " + currentPath);
//                    if (ROOT_DIR.equals(inputLine)) {
//                        System.out.println("Adding root path");
//                        currentPath = currentPath + "/";
//                        FileSystemDirectory rootDir = new FileSystemDirectory("/");
//                        Set<FileSystemDirectory> directories = fileSystemRootElement.getDirectories();
//                        directories.add(rootDir);
//                        fileSystemRootElement.setDirectories(directories);
//                    }
//                    if (LIST_DIR_ELEMENTS.equals(inputLine) || gotCommand.equals(LIST_DIR_ELEMENTS)) {
//                        System.out.println("Listing dir content");
//                        while (true) {
//                            String lineAfterLs = br.readLine();
//                            if (lineAfterLs.matches(COMMAND)) {
//                                System.out.println("Was command");
//                                gotCommand = lineAfterLs;
//                                break;
//                            }
//                            FileSystemDirectory fileSystemDirectory = getWorkingDirectory(currentPath, fileSystemRootElement);
//                            if (lineAfterLs.matches(DIR_RECOGNIZE_REGEX)) {
//                                FileSystemDirectory newDir = new FileSystemDirectory(lineAfterLs.split("\\s")[1]);
//                                Set<FileSystemDirectory> directories = fileSystemDirectory.getDirectories();
//                                directories.add(newDir);
//                                fileSystemDirectory.setDirectories(directories);
//                                break;
//                            } else if (lineAfterLs.matches(FILE_RECOGNIZE_REGEX)) {
//                                String inputFileSize = lineAfterLs.split("\\s")[0];
//                                String inputFileName = lineAfterLs.split("\\s")[1];
//                                FileSystemFile fileSystemFile = new FileSystemFile();
//                                fileSystemFile.setFileName(inputFileName);
//                                fileSystemFile.setSize(Long.parseLong(inputFileSize));
//                                Set<FileSystemFile> directoryFiles = fileSystemDirectory.getDirectoryFiles();
//                                directoryFiles.add(fileSystemFile);
//                                fileSystemDirectory.setDirectoryFiles(directoryFiles);
//                                break;
//                            }
//                        }
//                    } else if (inputLine.matches(GO_LEVEL_IN_REGEX) || gotCommand.equals(GO_LEVEL_IN_REGEX)) {
//                        System.out.println("Going level in");
//                        if (currentPath.equals("/")) {
//                            currentPath = currentPath + inputLine.split("\\s")[2];
//                        } else {
//                            currentPath = currentPath + "/" + inputLine.split("\\s")[2];
//                        }
//                    } else if (GO_LEVEL_UP.equals(inputLine) || gotCommand.equals(GO_LEVEL_UP)) {
//                        System.out.println("Going level out");
//                        if (!currentPath.equals("/")) {
//                            if (currentPath.substring(0, currentPath.length() - 2).isEmpty()) {
//                                currentPath = "/";
//                            } else {
//                                currentPath = currentPath.substring(0, currentPath.length() - 2);
//                            }
//                        }
//                       System.out.println("Current path after going level out is: " + currentPath);
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        System.out.println(fileSystemRootElement);
//        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//        return fileSystemRootElement;
//    }

}