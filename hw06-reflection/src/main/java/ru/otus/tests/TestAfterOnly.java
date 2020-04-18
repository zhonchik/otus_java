package ru.otus.tests;

import ru.otus.annotations.After;
import ru.otus.annotations.Test;

@ru.otus.annotations.Test
public class TestAfterOnly {

    @After
    public void TearDown() {
        System.out.println("called TearDown");
    }

    @Test
    public void Test1() {
        System.out.println("called Test1");
    };

    @Test
    public void Test2() {
        System.out.println("called Test2");
    };

    @Test
    public void TestFailed() {
        throw new RuntimeException("OMG! I'm failed");
    };
}
