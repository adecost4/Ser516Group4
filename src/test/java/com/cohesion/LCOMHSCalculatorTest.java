package com.cohesion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LCOMHSCalculatorTest {

    @Test
    void computeLcomhs_normalCase() {

        double result = LCOMHSCalculator.computeLcomhs(5, 2, 6);

        assertEquals(0.5, result, 0.0001);
    }

    @Test
    void computeLcomhs_fieldsCountZero_returnsZero() {
        double result = LCOMHSCalculator.computeLcomhs(5, 0, 10);
        assertEquals(0.0, result);
    }

    @Test
    void computeLcomhs_methodsCountOne_returnsZero() {
        double result = LCOMHSCalculator.computeLcomhs(1, 3, 5);
        assertEquals(0.0, result);
    }

    @Test
    void computeLcomhs_methodsCountZero_returnsZero() {
        double result = LCOMHSCalculator.computeLcomhs(0, 3, 5);
        assertEquals(0.0, result);
    }

    @Test
    void computeLcomhs_resultLessThanZero_clampedToZero() {

        double result = LCOMHSCalculator.computeLcomhs(3, 1, 10);

        assertEquals(0.0, result);
    }

    @Test
    void computeLcomhs_resultGreaterThanOne_clampedToOne() {

        double result = LCOMHSCalculator.computeLcomhs(5, 2, 0);

        assertEquals(1.0, result);
    }

    @Test
    void computeLcomhs_boundaryCase_exactlyZero() {
        double result = LCOMHSCalculator.computeLcomhs(4, 2, 8);
        assertEquals(0.0, result);
    }

    @Test
    void computeLcomhs_boundaryCase_exactlyOne() {

        double result = LCOMHSCalculator.computeLcomhs(4, 2, 2);

        assertEquals(1.0, result);
    }
}
