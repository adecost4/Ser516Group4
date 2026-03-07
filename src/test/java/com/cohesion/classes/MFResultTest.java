package com.cohesion.classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MFResultTest {

    @Test
    void constructorAndGetters_returnCorrectValues() {
        MFResult result = new MFResult(
                "com.example.package",
                "MyClass",
                10,
                5,
                20
        );

        assertEquals("com.example.package", result.getPackageName());
        assertEquals("MyClass", result.getClassName());
        assertEquals(10, result.getM());
        assertEquals(5, result.getF());
        assertEquals(20, result.getSUMMF());
    }

    @Test
    void handlesZeroValuesCorrectly() {
        MFResult result = new MFResult(
                "pkg",
                "EmptyClass",
                0,
                0,
                0
        );

        assertEquals(0, result.getM());
        assertEquals(0, result.getF());
        assertEquals(0, result.getSUMMF());
    }

    @Test
    void handlesNegativeValuesCorrectly() {
        MFResult result = new MFResult(
                "pkg",
                "NegativeClass",
                -1,
                -2,
                -3
        );

        assertEquals(-1, result.getM());
        assertEquals(-2, result.getF());
        assertEquals(-3, result.getSUMMF());
    }

    @Test
    void allowsNullPackageAndClassName() {
        MFResult result = new MFResult(
                null,
                null,
                1,
                2,
                3
        );

        assertNull(result.getPackageName());
        assertNull(result.getClassName());
    }
}