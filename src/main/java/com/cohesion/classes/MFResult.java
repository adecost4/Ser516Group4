package com.cohesion.classes;

public class MFResult {
    private final String className;
    private final int M;
    private final int F;
    private final int SUMMF;

    public MFResult(String className, int m, int f, int mf) {
        this.className = className;
        this.M = m;
        this.F = f;
        this.SUMMF = mf;
    }

    public String getClassName() {
        return className;
    }

    public int getM() {
        return M;
    }

    public int getF() {
        return F;
    }

    public int getSUMMF() {
        return SUMMF;
    }
}