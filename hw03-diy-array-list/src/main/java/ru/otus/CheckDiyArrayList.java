package ru.otus;

import java.util.Collections;


public class CheckDiyArrayList {

    public static void main(String... args) {

        int listSize = 30;

        DIYarrayList<Integer> src = new DIYarrayList<>();
        DIYarrayList<Integer> dst = new DIYarrayList<>(listSize);

        for (int i = 0; i < listSize; i++) {
            src.add((int) (Math.random() * listSize));
        }

        Collections.fill(dst, 0);
        System.out.println(dst);

        Collections.addAll(dst, 3, 9, 5);
        System.out.println(dst);

        Collections.copy(dst, src);
        System.out.println(dst);

        Collections.sort(dst, Collections.reverseOrder());
        System.out.println(dst);
    }
}
