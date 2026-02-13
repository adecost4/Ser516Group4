package com.cohesion.classparsing;

public class ClassParser {
    // given a class, count all of the methods (private, public, constructors)
    public int countClassMethods(Class<?> currClass) {
        int methodCount = currClass.getDeclaredMethods().length;
        methodCount += currClass.getConstructors().length;
        return methodCount;
    }

    // given a class, count all of the fields (private, public)
    public int countClassFields(Class<?> currClass) {
        int fieldCount = currClass.getDeclaredFields().length;
        return fieldCount;
    }
}
