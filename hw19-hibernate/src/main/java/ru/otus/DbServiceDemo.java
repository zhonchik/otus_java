package ru.otus;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.core.dao.UserDao;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Collections;
import java.util.Optional;

public class DbServiceDemo {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceDemo.class);

    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(
                "hibernate.cfg.xml",
                User.class,
                PhoneDataSet.class,
                AddressDataSet.class
        );

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        DBServiceUser dbServiceUser = new DbServiceUserImpl(userDao);

        logger.info("Creating user");
        var user = new User(
                0,
                "Some name",
                new AddressDataSet(0, "Some street"),
                Collections.singletonList(new PhoneDataSet(0, "Some phone number"))
        );
        dbServiceUser.saveUser(user);
        Optional<User> userOptional = dbServiceUser.getUser(user.getId());

        if (userOptional.isPresent()) {
            var u = userOptional.get();
            logger.info("User created: {}", u);

            logger.info("Updating user");
            u.setName("Another name");
            u.getPhones().add(new PhoneDataSet(0, "Another phone number"));
            dbServiceUser.saveUser(u);
            Optional<User> mayBeUpdatedUser = dbServiceUser.getUser(u.getId());
            logger.info("User updated: {}", mayBeUpdatedUser);
        }
    }
}
