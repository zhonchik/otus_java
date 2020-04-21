package ru.otus;


public class CheckReflection {

    public static void main(String... args) throws ClassNotFoundException {
        TestRunner.displayResults(new TestRunner("ru.otus.tests.TestAllFeatures").run());
        TestRunner.displayResults(new TestRunner("ru.otus.tests.TestBeforeOnly").run());
        TestRunner.displayResults(new TestRunner("ru.otus.tests.TestAfterOnly").run());

        try {
            new TestRunner("ru.otus.tests.TestIsNotTest").run();
        } catch (IllegalArgumentException e) {}
        try {
            new TestRunner("ru.otus.tests.TestNoTests").run();
        } catch (IllegalArgumentException e) {}
    }
}
