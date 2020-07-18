package ru.otus;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.cache.HwListener;
import ru.otus.cache.MyCache;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.ArrayList;
import java.util.Optional;

public class CacheDemo {
    private static final Logger logger = LoggerFactory.getLogger(CacheDemo.class);

    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(
                "hibernate.cfg.xml",
                User.class,
                PhoneDataSet.class,
                AddressDataSet.class
        );

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);

        var cache = new MyCache<String, User>();
        HwListener<String, User> listener = new HwListener<>() {
            @Override
            public void notify(String key, User value, String action) {
                logger.info("action: {}, key: {}, value: {}", action, key, value);
            }
        };
        cache.addListener(listener);


        DBServiceUser dbServiceUser = new DbServiceUserImpl(userDao, cache);

        logger.info("Creating user");
        var phones = new ArrayList<PhoneDataSet>();
        phones.add(new PhoneDataSet(0, "Some phone number"));
        var user = new User(
                0,
                "Some name",
                new AddressDataSet(0, "Some street"),
                phones
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
