package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final String selectAllSql;
    private final String selectByIdSql;
    private final String insertSql;
    private final String updateSql;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        var tableName = entityClassMetaData.getName();
        var fieldNamesWithoutId = entityClassMetaData
                .getFieldsWithoutId()
                .stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));
        var placeholders = String.join(
                ", ",
                Collections.nCopies(entityClassMetaData.getFieldsWithoutId().size(), "?")
        );
        var idFieldName = entityClassMetaData.getIdField().getName();
        selectAllSql = String.format("select %s from %s", fieldNamesWithoutId, tableName);
        selectByIdSql = String.format("%s where %s = ?", selectAllSql, idFieldName);
        insertSql = String.format(
                "insert into %s(%s) values (%s)",
                tableName,
                fieldNamesWithoutId,
                placeholders
        );
        fieldNamesWithoutId = entityClassMetaData
                .getFieldsWithoutId()
                .stream()
                .map(f -> String.format("%s = ?", f.getName()))
                .collect(Collectors.joining(", "));
        updateSql = String.format("update tn set %s where %s = ?", fieldNamesWithoutId, idFieldName);
    }

    @Override
    public String getSelectAllSql() {
        return selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        return selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        return insertSql;
    }

    @Override
    public String getUpdateSql() {
        return updateSql;
    }
}
