package ru.otus.sessionmanager;

import org.hibernate.Session;

public interface DatabaseSession {
    Session getHibernateSession();
}
