package ru.otus;

import ru.otus.annotations.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class LoggedProxy {

    private LoggedProxy() {
    }

    static LoggedInterface createLogged() {
        InvocationHandler handler = new LoggedInvocationHandler(new LoggedImpl());
        return (LoggedInterface) Proxy.newProxyInstance(
            LoggedProxy.class.getClassLoader(),
            new Class<?>[]{LoggedInterface.class},
            handler
        );
    }

    static class LoggedInvocationHandler implements InvocationHandler {
        private final LoggedInterface logged;
        private final Set<Method> methodsToLog = new HashSet<>();

        LoggedInvocationHandler(LoggedInterface logged) {
            this.logged = logged;

            var clazz = LoggedInterface.class;
            for (Method method: clazz.getMethods()) {
                if (method.isAnnotationPresent(Log.class)) {
                    System.out.println(method);
                    methodsToLog.add(method);
                }
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (methodsToLog.contains(method)) {
                System.out.printf("executed method: %s, params=%s%n", method.getName(), Arrays.toString(args));
            }
            return method.invoke(logged, args);
        }

        @Override
        public String toString() {
            return String.format("InvocationHandler{Logged=%s}", logged);
        }
    }
}
