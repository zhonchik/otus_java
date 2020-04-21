package ru.otus.ReflectionHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectionHelper {
    private ReflectionHelper() {
    }

    public static <T extends Annotation> void updateMethods(Class<T> annotation, Method method, List<Method> methods) {
        if (method.isAnnotationPresent(annotation)) {
            methods.add(method);
        }
    }

    public static void invokeMethods(Object object, List<Method> methods) throws Exception {
        for (Method m: methods) {
            m.invoke(object);
        }
    }
}
