package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.otus.cache.HwCache;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.hibernate.AbstractHibernateTest;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Демо работы с hibernate (с абстракциями) должно ")
@ExtendWith(MockitoExtension.class)
public class WithAbstractionsTest extends AbstractHibernateTest {

    @Mock(stubOnly = true)
    private HwCache<String, User> dummyCache;

    private DBServiceUser dbServiceUser;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        dbServiceUser = new DbServiceUserImpl(userDao, dummyCache);
    }

    @Test
    @DisplayName(" корректно сохранять пользователя")
    void shouldCorrectSaveUser() {
        User user = TestDataHelper.getDefaultUser();
        long id = dbServiceUser.saveUser(user);
        User loadedUser = loadUser(id);

        assertThat(loadedUser).isNotNull().usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    @DisplayName(" корректно загружать пользователя")
    void shouldLoadCorrectUser() {
        User savedUser = TestDataHelper.getDefaultUser();
        saveUser(savedUser);

        Optional<User> user = dbServiceUser.getUser(savedUser.getId());
        assertThat(user).isPresent().get().usingRecursiveComparison().isEqualTo(savedUser);
    }

    @Test
    @DisplayName(" корректно изменять ранее сохраненного пользователя")
    void shouldCorrectUpdateSavedUser() {
        User user = TestDataHelper.getDefaultUser();
        saveUser(user);

        user.setName(TestDataHelper.TEST_USER_ANOTHER_NAME);
        user.getPhones().add(new PhoneDataSet(0, "Another phone"));
        long id = dbServiceUser.saveUser(user);
        User loadedUser = loadUser(id);

        assertThat(loadedUser.getName()).isEqualTo(user.getName());
        assertThat(loadedUser.getAddress()).isEqualTo(user.getAddress());
        assertThat(loadedUser.getPhones().size()).isEqualTo(user.getPhones().size());
        for (int i = 0; i < loadedUser.getPhones().size(); i++) {
            assertThat(loadedUser.getPhones().get(i).getNumber()).isEqualTo(user.getPhones().get(i).getNumber());
        }
    }
}
