package ru.otus.hibernate.dao;

import java.util.Optional;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.core.dao.UserDao;
import ru.otus.core.dao.UserDaoException;
import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

public class UserDaoHibernate implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoHibernate.class);

    private final SessionManagerHibernate sessionManager;

    public UserDaoHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<User> findById(long id) {
        logger.info("Finding user by id: {}", id);
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            return Optional.ofNullable(currentSession.getHibernateSession().find(User.class, id));
        } catch (Exception e) {
            logger.error("Failed to find user", e);
        }
        return Optional.empty();
    }

    @Override
    public long insertUser(User user) {
        logger.info("Inserting user: {}", user);
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.persist(user);
            hibernateSession.flush();
            return user.getId();
        } catch (Exception e) {
            logger.error("Failed to insert user", e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void updateUser(User user) {
        logger.info("Updating user: {}", user);
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.merge(user);
            hibernateSession.flush();
        } catch (Exception e) {
            logger.error("Failed to update user", e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void insertOrUpdate(User user) {
        logger.info("Inserting or updating user: {}", user);
        if (user.getId() > 0) {
            updateUser(user);
        } else {
            insertUser(user);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
