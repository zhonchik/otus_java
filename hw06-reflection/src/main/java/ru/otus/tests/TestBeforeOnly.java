package ru.otus.tests;

import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

@ru.otus.annotations.Test
public class TestBeforeOnly {

    @Before
    public void SetUp() {
        System.out.println("called SetUp");
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
