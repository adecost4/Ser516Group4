package com.cohesion.classparsing;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

public class ClassParser {
    public int countClassMethods(ClassOrInterfaceDeclaration cls) {
        return cls.getMethods().size() + cls.getConstructors().size();
    }
}
