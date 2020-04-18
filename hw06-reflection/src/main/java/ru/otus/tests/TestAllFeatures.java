package ru.otus.tests;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

@ru.otus.annotations.Test
public class TestAllFeatures {

    @Before
    public void SetUp1() {
        System.out.println("called SetUp1");
    }

    @Before
    public void SetUp2() {
        System.out.println("called SetUp2");
    }

    @After
    public void TearDown1() {
        System.out.println("called TearDown1");
    }

    @After
    public void TearDown2() {
        System.out.println("called TearDown2");
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
