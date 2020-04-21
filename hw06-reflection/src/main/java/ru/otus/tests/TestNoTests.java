package ru.otus.tests;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;

@ru.otus.annotations.Test
public class TestNoTests {

    @Before
    public void SetUp() {
        System.out.println("called SetUp");
    }

    @After
    public void TearDown() {
        System.out.println("called TearDown");
    }

    public void Test1() {
        System.out.println("called Test1");
    };

    public void Test2() {
        System.out.println("called Test2");
    };

    public void TestFailed() {
        throw new RuntimeException("OMG! I'm failed");
    };
}
