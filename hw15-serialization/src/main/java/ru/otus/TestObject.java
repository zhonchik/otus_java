package ru.otus;

import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
public class TestObject {
    private String stringField;
    private int intField;
    private double doubleField;
    private boolean booleanField;
    private EnumTestObject enumField;
    private boolean[] booleanArrayField;
    private double[] doubleArrayField;
    private Set<String> stringSetField;
    private Map<Integer, Boolean> intBoolMapField;
    private TestObject cyclicReference;
    private Object nullField;
}
