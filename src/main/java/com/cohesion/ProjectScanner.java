package com.cohesion;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProjectScanner {

    public List<Path> scanProject(String projectPath) {
        Path root = Paths.get(projectPath);

        if (!Files.exists(root)) {
            throw new IllegalArgumentException(
                "Path does not exist: " + root.toAbsolutePath()
            );
        }

        try (Stream<Path> paths = Files.walk(root)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException(
                "Error scanning project: " + e.getMessage(), e
            );
        }
    }

    public List<String> getClasses(String projectPath) {
        List<Path> javaFiles = scanProject(projectPath);
        List<String> classes = new ArrayList<>();

        Pattern classPattern = Pattern.compile("\\bclass\\s+(\\w+)");

        for (Path file : javaFiles) {
            try {
                String content = Files.readString(file);
                Matcher matcher = classPattern.matcher(content);

                while (matcher.find()) {
                    String className = matcher.group(1);
                    classes.add(className);
                }
            } catch (IOException e) {
                throw new RuntimeException("Error reading file: " + file, e);
            }
        }
        return classes;
    }
}
