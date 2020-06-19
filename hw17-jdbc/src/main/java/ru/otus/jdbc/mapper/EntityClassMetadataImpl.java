package ru.otus.jdbc.mapper;

import ru.otus.annotations.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntityClassMetadataImpl<T> implements EntityClassMetaData<T> {
    private final String name;
    private final Constructor<T> constructor;
    private final Field idField;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;

    EntityClassMetadataImpl(Class<T> clazz) throws NoSuchMethodException, NoSuchFieldException {
        name = clazz.getSimpleName();
        constructor = clazz.getConstructor();
        allFields = new ArrayList<>();
        fieldsWithoutId = new ArrayList<>();
        Field idField = null;
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            allFields.add(field);
            if (field.isAnnotationPresent(Id.class)) {
                idField = field;
            } else {
                fieldsWithoutId.add(field);
            }
        }
        if (idField == null) {
            throw new NoSuchFieldException();
        }
        this.idField = idField;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }
}
