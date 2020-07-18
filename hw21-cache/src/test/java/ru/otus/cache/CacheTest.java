package ru.otus.cache;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.otus.TestDataHelper;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CacheTest {
    private HwCache<String, User> cache;
    @Mock
    private HwCache<String, User> dummyCache;
    private DBServiceUser dbServiceNoCache;
    private DBServiceUser dbServiceWithCache;

    @BeforeEach
    void setUp() {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(
                "hibernate-test.cfg.xml",
                User.class,
                PhoneDataSet.class,
                AddressDataSet.class
        );

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);

        cache = new MyCache<>();
        dbServiceNoCache = new DbServiceUserImpl(userDao, dummyCache);
        dbServiceWithCache = new DbServiceUserImpl(userDao, cache);
    }

    @Test
    void shouldSaveAndGetObjects() {
        var key = "test_key";
        var user = TestDataHelper.getDefaultUser();
        user.setId(5);
        cache.put(key, user);
        var cacheUser = cache.get(key);
        assertThat(cacheUser).isNotNull();
        assertThat(cacheUser.getId()).isEqualTo(user.getId());
    }

    @Test
    void shouldWorkFaster() {
        var user = TestDataHelper.getDefaultUser();
        var userId = dbServiceNoCache.saveUser(user);
        var repeatCount = 1000;

        var start = System.currentTimeMillis();
        for (int i = 0; i < repeatCount; i++) {
            dbServiceNoCache.getUser(userId);
        }
        var durationNoCache = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        for (int i = 0; i < repeatCount; i++) {
            dbServiceWithCache.getUser(userId);
        }
        var durationWithCache = System.currentTimeMillis() - start;

        assertThat(durationWithCache).isLessThan(durationNoCache);
    }

    @Test
    void shouldClearOldItems() {
        var user = TestDataHelper.getDefaultUser();
        for (int i = 0; i < 1000000; i++) {
            cache.put(Integer.toString(i), user);
        }
        assertThat(cache.get("0")).isNull();
    }
}
