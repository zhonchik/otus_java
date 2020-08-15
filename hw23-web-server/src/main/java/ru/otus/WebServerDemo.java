package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.cache.HwListener;
import ru.otus.cache.MyCache;
import ru.otus.dao.UserDao;
import ru.otus.model.AddressDataSet;
import ru.otus.model.PhoneDataSet;
import ru.otus.model.User;
import ru.otus.services.DBServiceUser;
import ru.otus.services.DbServiceUserImpl;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.server.UsersWebServer;
import ru.otus.server.UsersWebServerWithFilterBasedSecurity;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;
import ru.otus.services.UserAuthService;
import ru.otus.services.UserAuthServiceImpl;

import java.util.ArrayList;

public class WebServerDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    private static final Logger logger = LoggerFactory.getLogger(WebServerDemo.class);

    public static void main(String[] args) throws Exception {
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

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(userDao);

        UsersWebServer usersWebServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, dbServiceUser, gson, templateProcessor);

        for (int i = 0; i < 10; i++) {
            logger.info("Creating user");
            var phones = new ArrayList<PhoneDataSet>();
            phones.add(new PhoneDataSet(0, String.format("phone%d", i)));
            var user = new User(
                    0,
                    String.format("userName%d", i),
                    new AddressDataSet(0, String.format("address%d", i)),
                    phones
            );
            dbServiceUser.saveUser(user);
        }

        usersWebServer.start();
        usersWebServer.join();
    }
}
