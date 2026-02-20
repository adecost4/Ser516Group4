package com.cohesion.classes;

public class MFResult {
    private final String packageName;
    private final String className;
    private final int M;
    private final int F;
    private final int SUMMF;

    public MFResult(String packageName, String className, int m, int f, int mf) {
        this.packageName = packageName;
        this.className = className;
        this.M = m;
        this.F = f;
        this.SUMMF = mf;
    }
    public String getPackageName() {
        return packageName;
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