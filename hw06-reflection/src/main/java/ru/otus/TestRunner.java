package ru.otus;

import ru.otus.ReflectionHelper.ReflectionHelper;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class TestRunner {
    private Class<?> clazz;
    private List<Method> beforeMethods = new ArrayList();
    private List<Method> afterMethods = new ArrayList();
    private List<Method> testMethods = new ArrayList();

    public TestRunner(String testClass) throws ClassNotFoundException, IllegalArgumentException {
        clazz = Class.forName(testClass);
        if (!clazz.isAnnotationPresent(Test.class)) {
            throw new IllegalArgumentException(String.format("%s has no @Test annotation", clazz));
        }

        for (Method method: clazz.getMethods()) {
            ReflectionHelper.updateMethods(Before.class, method, beforeMethods);
            ReflectionHelper.updateMethods(After.class, method, afterMethods);
            ReflectionHelper.updateMethods(Test.class, method, testMethods);
        }
        if (testMethods.isEmpty()) {
            throw new IllegalArgumentException(String.format("%s has no methods with @Test annotation", clazz));
        }
    }

    public void run(boolean printStackTrace) {

        System.out.printf("Running %d tests for: %s%n", testMethods.size(), clazz.getSimpleName());

        int successCount = 0;
        int failedCount = 0;
        for (Method testMethod: testMethods) {
            String testName = testMethod.getName();
            try {
                Object object = clazz.getConstructor().newInstance();
                ReflectionHelper.invokeMethods(object, beforeMethods);
                testMethod.invoke(object);
                ReflectionHelper.invokeMethods(object, afterMethods);
                System.out.printf("%-20s OK%n", testName);
                successCount ++;
            } catch (Exception e) {
                if (printStackTrace) {
                    e.printStackTrace();
                }
                System.out.printf("%-20s FAILED%n", testName);
                failedCount++;
            }
        }

        System.out.printf(
                "Tests finished, success: %d, failed: %d, total: %d%n",
                successCount,
                failedCount,
                successCount + failedCount
        );
    }

}
