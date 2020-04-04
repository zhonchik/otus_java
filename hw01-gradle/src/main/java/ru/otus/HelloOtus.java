package ru.otus;

import com.google.common.collect.Maps;
import java.util.HashMap;


public class HelloOtus {
    public static void main(String... args) {
        HashMap<String, String> example = Maps.newHashMap();
        example.put("hello", "otus");
        System.out.println(example);
    }
}
