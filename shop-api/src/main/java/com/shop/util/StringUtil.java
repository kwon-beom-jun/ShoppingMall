package com.shop.util;

public class StringUtil {

    public static String controllerStartLog(String logMesage) {
        StringBuilder sb = new StringBuilder();
        int sideLength = 7;
        int total = sideLength * 2 + logMesage.length() + 2;
        sb.append("\n");
        sb.append("〓".repeat(Math.max(0, total))).append("\n");
        sb.append("〓".repeat(Math.max(0, sideLength)));
        sb.append("〓 " + logMesage + " 〓");
        sb.append("〓".repeat(Math.max(0, sideLength))).append("\n");
        sb.append("〓".repeat(Math.max(0, total))).append("\n");
        return sb.toString();
    }


}
