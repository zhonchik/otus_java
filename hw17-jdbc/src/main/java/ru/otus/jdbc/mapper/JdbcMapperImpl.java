package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

public class JdbcMapperImpl<T> implements JdbcMapper<T> {
    private final SessionManagerJdbc sessionManager;
    private final DbExecutor<T> executor;
    private final EntityClassMetaData<T> entityClassMetaData;
    private final EntitySQLMetaData entitySQLMetaData;
    private static final Logger logger = LoggerFactory.getLogger(JdbcMapperImpl.class);

    public JdbcMapperImpl(Class<T> clazz, SessionManagerJdbc sessionManager, DbExecutor<T> executor) throws NoSuchMethodException, NoSuchFieldException {
        this.sessionManager = sessionManager;
        this.executor = executor;
        this.entityClassMetaData = new EntityClassMetadataImpl<>(clazz);
        this.entitySQLMetaData = new EntitySQLMetaDataImpl(entityClassMetaData);
    }

    @Override
    public void insert(T objectData) {
        try {
            var connection = sessionManager.getCurrentSession().getConnection();
            var id = executor.executeInsert(connection, entitySQLMetaData.getInsertSql(), getValues(objectData));
            connection.commit();
            entityClassMetaData.getIdField().set(objectData, id);
            logger.info("{} inserted into table", objectData);
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(T objectData) {
        try {
            var values = getValues(objectData);
            values.add(entityClassMetaData.getIdField().get(objectData));
            var connection = sessionManager.getCurrentSession().getConnection();
            executor.executeInsert(connection, entitySQLMetaData.getUpdateSql(), values);
            connection.commit();
            logger.info("{} updated", objectData);
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertOrUpdate(T objectData) {
        try {
            if (findById(entityClassMetaData.getIdField().getLong(objectData)) == null) {
                insert(objectData);
            } else {
                update(objectData);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public T findById(long id) {
        logger.info("Finding object with id = {}", id);
        logger.info("Executing '{}'", entitySQLMetaData.getSelectByIdSql());
        try {
            var connection = sessionManager.getCurrentSession().getConnection();
            var object = executor
                    .executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), id, this::rsHandler)
                    .orElse(null);
            if (object != null) {
                entityClassMetaData.getIdField().set(object, id);
            }
            return object;
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<Object> getValues(T objectData) {
        var values = new ArrayList<>();
        for (var field : entityClassMetaData.getFieldsWithoutId()) {
            try {
                values.add(field.get(objectData));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return values;
    }

    private T rsHandler(ResultSet resultSet) {
        try {
            if (!resultSet.next()) {
                logger.info("No results in resultSet");
                return null;
            }
            var object = entityClassMetaData.getConstructor().newInstance();
            for (var field: entityClassMetaData.getFieldsWithoutId()) {
                field.set(object, resultSet.getObject(field.getName()));
            }
            return object;

        } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
