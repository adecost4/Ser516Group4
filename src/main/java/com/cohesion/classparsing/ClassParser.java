package com.cohesion.classparsing;

public class ClassParser {
    // given a class, count all of the methods (private, public, constructors)
    public int countClassMethods(Class<?> currClass) {
        int methodCount = currClass.getDeclaredMethods().length;
        methodCount += currClass.getMethods().length;
        return methodCount;
    }
}
