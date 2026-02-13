package com.cohesion;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
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
}
