package com.cohesion.classparsing;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

public class ClassParser {
    /*
    TODO: Task 17 extract methods + fields for each class
    both methods require a ClassOrInterfaceDeclaration as input, which can be found using StaticJavaParser.parse(file).getClassByName("ClassName").orElseThrow(() -> new RuntimeException("Class not found"))
    you can also do something along the lines of CompilationUnit cu = StaticJavaParser.parse(file); cu.findAll(ClassOrInterfaceDeclaration.class).forEach(cls -> { // do something with each class });
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
}
