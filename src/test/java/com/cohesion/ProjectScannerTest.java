package com.cohesion;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectScannerTest {

    private final ProjectScanner scanner = new ProjectScanner();

    @Test
    void scanProject_nullPath_throws() {
        assertThrows(IllegalArgumentException.class, () -> scanner.scanProject(null));
    }

    @Test
    void scanProject_emptyPath_throws() {
        assertThrows(IllegalArgumentException.class, () -> scanner.scanProject(""));
    }

    @Test
    void scanProject_pathDoesNotExist_throws() {
        assertThrows(IllegalArgumentException.class, () -> scanner.scanProject("this/path/does/not/exist"));
    }

    @Test
    void scanProject_returnsOnlyJavaFiles(@TempDir Path tempDir) throws Exception {
        Path java1 = tempDir.resolve("A.java");
        Path java2 = tempDir.resolve("b.JAVA");
        Path txt = tempDir.resolve("notes.txt");

        Files.writeString(java1, "class A {}");
        Files.writeString(java2, "class B {}");
        Files.writeString(txt, "not java");

        Path nested = Files.createDirectories(tempDir.resolve("nested"));
        Path java3 = nested.resolve("C.java");
        Files.writeString(java3, "class C {}");

        List<Path> results = scanner.scanProject(tempDir.toString());

        assertEquals(3, results.size());
        assertTrue(results.contains(java1));
        assertTrue(results.contains(java2));
        assertTrue(results.contains(java3));
        assertFalse(results.contains(txt));

        // ensure all end with .java (case-insensitive)
        assertTrue(results.stream().allMatch(p -> p.toString().toLowerCase().endsWith(".java")));
    }

    @Test
    void getClasses_extractsSingleClass(@TempDir Path tempDir) throws Exception {
        Path java1 = tempDir.resolve("One.java");
        Files.writeString(java1, "package x;\npublic class One {}");

        List<String> classes = scanner.getClasses(tempDir.toString());

        assertEquals(List.of("One"), classes);
    }

    @Test
    void getClasses_extractsMultipleClassesFromSameFile(@TempDir Path tempDir) throws Exception {
        Path java1 = tempDir.resolve("Many.java");
        Files.writeString(java1,
                "public class First {}\n" +
                "class Second {}\n" +
                "final class Third {}"
        );

        List<String> classes = scanner.getClasses(tempDir.toString());

        assertTrue(classes.contains("First"));
        assertTrue(classes.contains("Second"));
        assertTrue(classes.contains("Third"));
        assertEquals(3, classes.size());
    }

    @Test
    void getClasses_ignoresNonJavaFiles(@TempDir Path tempDir) throws Exception {
        Files.writeString(tempDir.resolve("Ignore.txt"), "class Fake {}");
        Files.writeString(tempDir.resolve("Real.java"), "class Real {}");

        List<String> classes = scanner.getClasses(tempDir.toString());

        assertEquals(List.of("Real"), classes);
    }

    @Test
    void getClasses_noClasses_returnsEmptyList(@TempDir Path tempDir) throws Exception {
        Files.writeString(
                tempDir.resolve("NoClass.java"),
                "package p;\n" +
                "public interface X {}\n" +
                "public enum E { A, B }\n"
        );

        List<String> classes = scanner.getClasses(tempDir.toString());

        assertTrue(classes.isEmpty());
    }
}