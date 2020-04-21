package ru.otus.ConsoleDisplayResults;


import ru.otus.TestRunner;


public class ConsoleDisplayResults {

    public static void display(TestRunner.TestResult result) {
        System.out.printf("Test results for: %s%n", result.getClassName());

        for (TestRunner.MethodTestResult methodResult: result.getMethodTestResults()) {
            switch (methodResult.getStatus()) {
                case SUCCESS: {
                    System.out.printf("%-20s OK%n", methodResult.getMethodName());
                    break;
                }
                case FAILED: {
                    String exception = methodResult.getException().getCause().getClass().getSimpleName();
                    System.out.printf("%-20s FAILED WITH EXCEPTION %s%n", methodResult.getMethodName(), exception);
                    break;
                }
            }
        }

        int successCount = result.getSuccessCount();
        int failedCount = result.getFailedCount();
        System.out.printf(
                "Tests finished, success: %d, failed: %d, total: %d%n",
                successCount,
                failedCount,
                successCount + failedCount
        );
    }
}
