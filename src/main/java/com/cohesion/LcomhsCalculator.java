package com.cohesion;

import java.util.Map;
import java.util.Set;

public class LcomhsCalculator {

    /**
     * Computes LCOMHS using:
     * LCOMHS = ( M - (sum(MF_i) / F) ) / (M - 1)
     *
     * Inputs expected from:
     *  - US-2: M (methodsCount), F (fieldsCount)
     *  - US-3: sumOfFieldToMethods map (field -> methods that access it)
     */
    public static double computeLcomhs(int methodsCount,
                                       int fieldsCount,
                                        double sumOfFieldToMethods) {

        // Acceptance criteria: handle edge cases safely
        if (fieldsCount <= 0 || methodsCount <= 1) {
            return 0.0;
        }

        double avg = sumOfFieldToMethods / (double) fieldsCount;
        double lcomhs = (methodsCount - avg) / (methodsCount - 1.0);

        // Defensive clamp to [0, 1]
        if (lcomhs < 0) lcomhs = 0;
        if (lcomhs > 1) lcomhs = 1;

        return lcomhs;
    }
}