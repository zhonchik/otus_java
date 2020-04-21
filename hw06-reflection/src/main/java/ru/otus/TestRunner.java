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

    public enum Status {
        SUCCESS,
        FAILED;
    }

    public class MethodTestResult {
        private String methodName;
        private Status status;
        private Exception exception;

        public MethodTestResult(String methodName, Status status, Exception exception) {
            this.methodName = methodName;
            this.status = status;
            this.exception = exception;
        }

        public String getMethodName() {
            return methodName;
        }

        public Status getStatus() {
            return status;
        }

        public Exception getException() {
            return exception;
        }
    }

    public class TestResult {
        private String className;
        private List<MethodTestResult> methodTestResults;

        public TestResult(String className, List<MethodTestResult> methodTestResults) {
            this.className = className;
            this.methodTestResults = methodTestResults;
        }

        public String getClassName() {
            return className;
        }

        public List<MethodTestResult> getMethodTestResults() {
            return methodTestResults;
        }
    }

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

    public TestResult run() {
        List<MethodTestResult> methodResults = new ArrayList();

        for (Method testMethod: testMethods) {
            String testName = testMethod.getName();
            try {
                Object object = clazz.getConstructor().newInstance();
                try {
                    ReflectionHelper.invokeMethods(object, beforeMethods);
                    testMethod.invoke(object);
                }
                finally {
                    ReflectionHelper.invokeMethods(object, afterMethods);
                }
                methodResults.add(new MethodTestResult(testName, Status.SUCCESS, null));
            } catch (Exception e) {
                methodResults.add(new MethodTestResult(testName, Status.FAILED, e));
            }
        }
        return new TestResult(clazz.getSimpleName(), methodResults);
    }
}
