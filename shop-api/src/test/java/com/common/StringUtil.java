package com.common;

import java.util.List;

public class StringUtil {

    public static <T> void listPrint(List<T> list) {
        for (T t : list) {
            System.out.println(t.toString());
        }
    }

}
