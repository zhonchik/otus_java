package ru.otus;

public class Benchmark {

    void run() throws InterruptedException {
        Object[] outerArray = new Object[100000];
        for (int idx = 0; idx < outerArray.length; idx++) {
            Object[] array = new Object[10000];
            for (int i = 0; i < array.length; i++) {
                array[i] = new String(new char[0]);
            }
            outerArray[idx] = array;
            if (idx > 0 && idx % 2 == 0) {
                outerArray[idx - 1] = "";
            }
            if (idx > 0 && idx % 1000 == 0) {
                System.out.printf("%s items processed%n", idx);
            }
            Thread.sleep(6);
        }
    }
}
