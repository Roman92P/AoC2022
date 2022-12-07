package org.pashkov.aoc2022.day7;

import java.io.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

        FileSystemRootElement fileSystemRootElement = new FileSystemRootElement();
        String currentPath = "";
        String gotCommand = "";
        try (InputStreamReader inputStream = getInputStreamFromFile()) {
            try (BufferedReader br = new BufferedReader(inputStream)) {
                while (br.ready()) {
                    String inputLine = gotCommand;
                    if (inputLine.equals("")) {
                        inputLine = br.readLine();
                    }
                    if (ROOT_DIR.equals(inputLine)) {
                        currentPath = currentPath + "/";
                        FileSystemDirectory rootDir = new FileSystemDirectory("/");
                        Set<FileSystemDirectory> directories = fileSystemRootElement.getDirectories();
                        directories.add(rootDir);
                        fileSystemRootElement.setDirectories(directories);
                    }
                    if (LIST_DIR_ELEMENTS.equals(inputLine) || gotCommand.equals(LIST_DIR_ELEMENTS)) {
                        while (true) {
                            String lineAfterLs = br.readLine();
                            if (lineAfterLs.matches(COMMAND)) {
                                gotCommand = lineAfterLs;
                                break;
                            }
                            FileSystemDirectory fileSystemDirectory = getWorkingDirectory(currentPath, fileSystemRootElement);
                            if (lineAfterLs.matches(DIR_RECOGNIZE_REGEX)) {
                                FileSystemDirectory newDir = new FileSystemDirectory(lineAfterLs.split("\\s")[1]);
                                Set<FileSystemDirectory> directories = fileSystemDirectory.getDirectories();
                                directories.add(newDir);
                                fileSystemDirectory.setDirectories(directories);
                            } else if (lineAfterLs.matches(FILE_RECOGNIZE_REGEX)) {
                                String inputFileSize = lineAfterLs.split("\\s")[0];
                                String inputFileName = lineAfterLs.split("\\s")[1];
                                FileSystemFile fileSystemFile = new FileSystemFile();
                                fileSystemFile.setFileName(inputFileName);
                                fileSystemFile.setSize(Long.parseLong(inputFileSize));
                                Set<FileSystemFile> directoryFiles = fileSystemDirectory.getDirectoryFiles();
                                directoryFiles.add(fileSystemFile);
                                fileSystemDirectory.setDirectoryFiles(directoryFiles);
                            }
                        }
                    } else if (inputLine.matches(GO_LEVEL_IN_REGEX) || gotCommand.equals(GO_LEVEL_IN_REGEX)) {
                        if (currentPath.equals("/")) {
                            currentPath = currentPath + inputLine.split("\\s")[1];
                        } else {
                            currentPath = currentPath + "/" + inputLine.split("\\s")[1];
                        }
                    } else if (GO_LEVEL_UP.equals(inputLine) || gotCommand.equals(GO_LEVEL_UP)) {
                        if (!currentPath.equals("/")) {
                            currentPath = currentPath.substring(0, currentPath.length() - 1);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(fileSystemRootElement);
    }

    private static FileSystemDirectory getWorkingDirectory(String currentPath, FileSystemRootElement fileSystemRootElement) {
        return fileSystemRootElement.getDirectories()
                .stream()
                .filter(fileSystemDirectory -> fileSystemDirectory.getDirName().equals(currentPath.substring(currentPath.length()-1)))
                .findFirst()
                .get();
    }

    private static InputStreamReader getInputStreamFromFile() throws FileNotFoundException {
        File file = new File("day7-1e.txt");
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

}


