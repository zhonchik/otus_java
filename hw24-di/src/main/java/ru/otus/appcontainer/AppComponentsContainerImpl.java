package ru.otus.appcontainer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    @Data
    private static class ComponentInfo {
        private final int order;
        private final String name;
        private final Method method;
    }

    private final Map<String, Object> appComponentsByName = new HashMap<>();
    private final Map<Class<?>, Object> appComponentsByType = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws Exception {
        processConfig(initialConfigClass);
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponentsByType.get(componentClass);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }

    private void processConfig(Class<?> configClass) throws Exception {
        checkConfigClass(configClass);
        var componentsInfo = getComponentsInfo(configClass);
        createComponents(configClass, componentsInfo);
    }

    private List<ComponentInfo> getComponentsInfo(Class<?> configClass) {
        var componentInfoList = new ArrayList<ComponentInfo>();
        for (var method : configClass.getMethods()) {
            if (!method.isAnnotationPresent(AppComponent.class)) {
                continue;
            }
            var annotation = method.getAnnotation(AppComponent.class);
            var order = annotation.order();
            var name = annotation.name();
            componentInfoList.add(new ComponentInfo(order, name, method));
        }
        componentInfoList.sort(Comparator.comparingInt(ComponentInfo::getOrder));
        return componentInfoList;
    }

    private void createComponents(Class<?> configClass, List<ComponentInfo> componentsInfo) throws Exception {
        var configInstance = configClass.getConstructor().newInstance();
        for (var componentInfo : componentsInfo) {
            var method = componentInfo.getMethod();
            var name = componentInfo.getName();
            var params = new ArrayList<>();

            for (var param: method.getParameters()) {
                var paramType = param.getType();
                var component = appComponentsByType.get(paramType);
                if (component == null) {
                    throw new Exception(String.format("No suitable component found for %s", paramType));
                }
                params.add(component);
            }

            Object component;

            if (params.isEmpty()) {
                component = method.invoke(configInstance);
            }
            else {
                Object[] pa = params.toArray();
                component = method.invoke(configInstance, pa);
            }
            appComponentsByName.put(name, component);
            appComponentsByType.put(method.getReturnType(), component);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }
}
