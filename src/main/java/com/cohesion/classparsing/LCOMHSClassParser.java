package com.cohesion.classparsing;

import java.io.File;
import java.util.List;

import com.cohesion.classes.MFResult;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

public class LCOMHSClassParser {
    /*
     * TODO: Task 17 extract methods + fields for each class
     * both methods require a ClassOrInterfaceDeclaration as input, which can be
     * found using
     * StaticJavaParser.parse(file).getClassByName("ClassName").orElseThrow(() ->
     * new RuntimeException("Class not found"))
     * you can also do something along the lines of CompilationUnit cu =
     * StaticJavaParser.parse(file);
     * cu.findAll(ClassOrInterfaceDeclaration.class).forEach(cls -> { // do
     * something with each class });
     */

    // count class methods and constructors
    public int countClassMethods(ClassOrInterfaceDeclaration cls) {
        return cls.getMethods().size() + cls.getConstructors().size();
    }

    // count class instance fields
    public int contCountClassInstanceFields(ClassOrInterfaceDeclaration cls) {
        return (int) cls.getFields().stream()
                .filter(field -> !field.isStatic())
                .count();
    }


    //task 17
    public List<MFResult> getMFForFile(File file) {
        try {
            CompilationUnit cu = StaticJavaParser.parse(file);

            return cu.findAll(ClassOrInterfaceDeclaration.class)
                    .stream()
                    .filter(cls -> !cls.isInterface()) 
                    .map(cls -> {
                        int m = countClassMethods(cls); // Task 15
                        int f = contCountClassInstanceFields(cls); // Task 16
                        return new MFResult(cls.getNameAsString(), m, f);
                    })
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException("Error parsing file: " + file, e);
        }
    }
}
