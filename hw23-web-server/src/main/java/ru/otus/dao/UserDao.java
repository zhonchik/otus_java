package ru.otus.dao;

import ru.otus.model.User;
import ru.otus.sessionmanager.SessionManager;

import java.util.Optional;

public interface UserDao {

    long insertUser(User user);

    void updateUser(User user);

    void insertOrUpdate(User user);

    SessionManager getSessionManager();

    Optional<User> findById(long id);
}