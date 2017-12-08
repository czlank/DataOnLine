package com.dataonline.util;

public class LineNo {
    public static int getLineNumber() {
        return Thread.currentThread().getStackTrace()[2].getLineNumber();
    }

    public static String getFileName() {
        return Thread.currentThread().getStackTrace()[2].getFileName();
    }
}
