package ru.otus;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class JsonWriter {
    private final String itemDelimiter = ",";
    private final String pairDelimiter = ":";
    private final int maxDepth = 1000;

    private final static Map<Class<?>, Function<Object, Stream<String>>> typeMappers = new HashMap<>() {{
        put(boolean.class, (o) -> IntStream
                .range(0, ((boolean[]) o).length)
                .mapToObj(i -> ((boolean[]) o)[i])
                .map(String::valueOf)
        );
        put(char.class, (o) -> IntStream
                .range(0, ((char[]) o).length)
                .mapToObj(i -> ((char[]) o)[i])
                .map(String::valueOf)
        );
        put(byte.class, (o) -> IntStream
                .range(0, ((byte[]) o).length)
                .mapToObj(i -> ((byte[]) o)[i])
                .map(String::valueOf)
        );
        put(short.class, (o) -> IntStream
                .range(0, ((short[]) o).length)
                .mapToObj(i -> ((short[]) o)[i])
                .map(String::valueOf)
        );
        put(int.class, (o) -> Arrays
                .stream((int[]) o)
                .boxed()
                .map(String::valueOf)
        );
        put(long.class, (o) -> Arrays
                .stream((long[]) o)
                .boxed()
                .map(String::valueOf)
        );
        put(float.class, (o) -> IntStream
                .range(0, ((float[]) o).length)
                .mapToObj(i -> ((float[]) o)[i])
                .map(String::valueOf)
        );
        put(double.class, (o) -> Arrays
                .stream((double[]) o)
                .boxed()
                .map(String::valueOf)
        );
    }};

    private final static Set<Class<?>> wrappers = Set.of(
            Boolean.class,
            Character.class,
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class
    );

    private String fromObject(Object object, Set<Integer> nodePath) {
        if (object == null || nodePath.size() > maxDepth) {
            return "null";
        }

        var clazz = object.getClass();

        if (typeMappers.containsKey(clazz) || wrappers.contains(clazz)) {
            return String.valueOf(object);
        }

        if (clazz == String.class || clazz.isEnum()) {
            return String.format("\"%s\"", object);
        }

        Integer identityHashCode = System.identityHashCode(object);

        if (nodePath.contains(identityHashCode)) {
            return "null";
        }

        nodePath.add(identityHashCode);
        Stream<String> stream = null;

        if (clazz.isArray()) {
            var mapper = typeMappers.get(clazz.componentType());
            if (mapper == null) {
                stream = Arrays.stream(((Object[]) object), 0, ((Object[]) object).length)
                        .map(String::valueOf);
            } else {
                stream = mapper.apply(object);
            }

        } else if ((object instanceof List) || (object instanceof Set)) {
            stream = ((Collection<Object>) object)
                    .stream()
                    .map(o -> fromObject(o, nodePath));
        }
        if (stream != null) {
            var string = stream.collect(Collectors.joining(itemDelimiter));
            nodePath.remove(identityHashCode);
            return String.format("[%s]", string);
        }

        if (object instanceof Map) {
            stream = ((Map<Object, Object>) object).entrySet()
                    .stream()
                    .map(p -> String.format(
                            "\"%s\"%s%s",
                            fromObject(p.getKey(), nodePath),
                            pairDelimiter,
                            fromObject(p.getValue(), nodePath)
                    ));
        } else {
            stream = Arrays.stream(clazz.getDeclaredFields()).map(f -> {
                try {
                    f.setAccessible(true);
                    return String.format("\"%s\":%s", f.getName(), fromObject(f.get(object), nodePath));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return null;
            });

        }
        var string = stream.collect(Collectors.joining(itemDelimiter));
        nodePath.remove(identityHashCode);
        return String.format("{%s}", string);
    }

    public String toJson(Object object) {
        return fromObject(object, new TreeSet<>());
    }
}
