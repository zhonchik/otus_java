package ru.otus.hibernate.dao;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.core.model.User;
import ru.otus.hibernate.AbstractHibernateTest;
import ru.otus.TestDataHelper;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Dao для работы с пользователями должно ")
class UserDaoHibernateTest extends AbstractHibernateTest {

    private SessionManagerHibernate sessionManagerHibernate;
    private UserDaoHibernate userDaoHibernate;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        sessionManagerHibernate = new SessionManagerHibernate(sessionFactory);
        userDaoHibernate = new UserDaoHibernate(sessionManagerHibernate);
    }

    @Test
    @DisplayName(" корректно загружать пользователя по заданному id")
    void shouldFindCorrectUserById() {
        User user = TestDataHelper.getDefaultUser();
        saveUser(user);

        assertThat(user.getId()).isGreaterThan(0);

        sessionManagerHibernate.beginSession();
        Optional<User> mayBeUser = userDaoHibernate.findById(user.getId());
        sessionManagerHibernate.commitSession();

        assertThat(mayBeUser).isPresent().get().usingRecursiveComparison().isEqualTo(user);
    }

    @DisplayName(" корректно сохранять пользователя")
    @Test
    void shouldCorrectSaveUser() {
        User user = TestDataHelper.getDefaultUser();
        sessionManagerHibernate.beginSession();
        userDaoHibernate.insertOrUpdate(user);
        long id = user.getId();
        sessionManagerHibernate.commitSession();

        assertThat(id).isGreaterThan(0);

        User actualUser = loadUser(id);
        assertThat(actualUser).isNotNull().hasFieldOrPropertyWithValue(TestDataHelper.TEST_USER_NAME_COLUMN, user.getName());

        user.setName(TestDataHelper.TEST_USER_ANOTHER_NAME);
        sessionManagerHibernate.beginSession();
        userDaoHibernate.insertOrUpdate(user);
        long newId = user.getId();
        sessionManagerHibernate.commitSession();

        assertThat(newId).isGreaterThan(0).isEqualTo(id);
        actualUser = loadUser(newId);
        assertThat(actualUser).isNotNull().hasFieldOrPropertyWithValue(TestDataHelper.TEST_USER_NAME_COLUMN, user.getName());
    }

    @DisplayName(" возвращать менеджер сессий")
    @Test
    void getSessionManager() {
        assertThat(userDaoHibernate.getSessionManager()).isNotNull().isEqualTo(sessionManagerHibernate);
    }

}
