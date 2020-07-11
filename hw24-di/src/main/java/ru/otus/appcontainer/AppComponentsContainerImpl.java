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

    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws Exception {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) throws Exception {
        checkConfigClass(configClass);
        var componentsInfo = getComponentsInfo(configClass);
        createComponents(configClass, componentsInfo);
    }

    private List<ComponentInfo> getComponentsInfo(Class<?> configClass) {
        var componentsInfo = new ArrayList<ComponentInfo>();
        for (var method : configClass.getMethods()) {
            if (!method.isAnnotationPresent(AppComponent.class)) {
                continue;
            }
            var annotation = method.getAnnotation(AppComponent.class);
            var order = annotation.order();
            var name = annotation.name();
            componentsInfo.add(new ComponentInfo(order, name, method));
        }
        componentsInfo.sort(Comparator.comparingInt(ComponentInfo::getOrder));
        return componentsInfo;
    }

    private void createComponents(Class<?> configClass, List<ComponentInfo> componentsInfo) throws Exception {
        var config = configClass.getConstructor().newInstance();
        for (var componentInfo : componentsInfo) {
            var method = componentInfo.getMethod();
            var name = componentInfo.getName();
            var params = new ArrayList<>();

            for (var param: method.getParameters()) {
                var paramTypeName = param.getType().getTypeName();
                var component = appComponentsByName.get(paramTypeName);
                if (component == null) {
                    throw new Exception(String.format("No suitable component found for %s", paramTypeName));
                }
                params.add(component);
            }

            Object component;

            if (params.size() == 0) {
                component = method.invoke(config);
            }
            else {
                Object[] pa = params.toArray();
                component = method.invoke(config, pa);
            }
            appComponentsByName.put(name, component);
            appComponentsByName.put(method.getReturnType().getName(), component);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponentsByName.get(componentClass.getName());
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
