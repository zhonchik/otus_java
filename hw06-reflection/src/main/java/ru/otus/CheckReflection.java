package ru.otus;


import ru.otus.ConsoleDisplayResults.ConsoleDisplayResults;


public class CheckReflection {

    public static void main(String... args) throws ClassNotFoundException {
        ConsoleDisplayResults.display(new TestRunner("ru.otus.tests.TestAllFeatures").run());
        ConsoleDisplayResults.display(new TestRunner("ru.otus.tests.TestBeforeOnly").run());
        ConsoleDisplayResults.display(new TestRunner("ru.otus.tests.TestAfterOnly").run());

        try {
            new TestRunner("ru.otus.tests.TestIsNotTest").run();
        } catch (IllegalArgumentException e) {}
        try {
            new TestRunner("ru.otus.tests.TestNoTests").run();
        } catch (IllegalArgumentException e) {}
    }
}
