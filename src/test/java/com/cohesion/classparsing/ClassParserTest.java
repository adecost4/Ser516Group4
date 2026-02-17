package com.cohesion.classparsing;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

public class ClassParserTest {
    private final ClassParser parser = new ClassParser();
    File footballClass = new File("src/main/java/com/sample/football/FootballTeam.java");

    @Test
    public void testCountClassMethods() {

        ClassOrInterfaceDeclaration cls = null;
        try {
            cls = StaticJavaParser.parse(footballClass).getClassByName("FootballTeam").orElseThrow(() -> new RuntimeException("Class not found"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int count = parser.countClassMethods(cls);
        assert count == 12 : "Expected 12 methods and constructors, but got " + count;
    }
}
