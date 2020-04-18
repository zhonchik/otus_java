package ru.otus;


public class CheckReflection {

    public static void main(String... args) throws ClassNotFoundException {
        boolean printStackTrace = false;
        new TestRunner("ru.otus.tests.TestAllFeatures").run(printStackTrace);
        new TestRunner("ru.otus.tests.TestBeforeOnly").run(printStackTrace);
        new TestRunner("ru.otus.tests.TestAfterOnly").run(printStackTrace);

        try {
            new TestRunner("ru.otus.tests.TestIsNotTest").run(printStackTrace);
        } catch (IllegalArgumentException e) {}
        try {
            new TestRunner("ru.otus.tests.TestNoTests").run(printStackTrace);
        } catch (IllegalArgumentException e) {}
    }
}
