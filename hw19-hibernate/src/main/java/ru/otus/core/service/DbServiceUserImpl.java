package ru.otus.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

public class DbServiceUserImpl implements DBServiceUser {
    private static Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

    private final UserDao userDao;

    public DbServiceUserImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public long saveUser(User user) {
        logger.info("Saving user {}", user);
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                userDao.insertOrUpdate(user);
                long id = user.getId();
                sessionManager.commitSession();
                user.setId(id);
                logger.info("User saved with id={}", id);
                return id;
            } catch (Exception e) {
                logger.error("Failed to save user", e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }


    @Override
    public Optional<User> getUser(long id) {
        logger.info("Getting user with id={}", id);
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var user = userDao.findById(id);
                logger.info("Got user: {}", user);
                return user;
            } catch (Exception e) {
                logger.error("Failed to get user", e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }
}
