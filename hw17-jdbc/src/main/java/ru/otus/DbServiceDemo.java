package ru.otus;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.model.Account;
import ru.otus.jdbc.dao.UserDaoJdbc;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.h2.DataSourceH2;
import ru.otus.core.model.User;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.mapper.JdbcMapperImpl;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

/**
 * @author sergey
 * created on 03.02.19.
 */
public class DbServiceDemo {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceDemo.class);

    public static void main(String[] args) throws Exception {
        var dataSource = new DataSourceH2();
        var demo = new DbServiceDemo();

        demo.createTables(dataSource);

        var sessionManager = new SessionManagerJdbc(dataSource);
        DbExecutorImpl<User> userDbExecutor = new DbExecutorImpl<>();
        JdbcMapper<User> userMapper = new JdbcMapperImpl<>(User.class, sessionManager, userDbExecutor);
        var userDao = new UserDaoJdbc(sessionManager, userMapper);

        var dbServiceUser = new DbServiceUserImpl(userDao);
        var user = new User(0, "Some User", 3);
        dbServiceUser.saveUser(user);
        Optional<User> userOptional = dbServiceUser.getUser(user.getId());

        userOptional.ifPresentOrElse(
                crUser -> logger.info("created {}", crUser),
                () -> logger.info("user was not created")
        );

        DbExecutorImpl<Account> accountDbExecutor = new DbExecutorImpl<>();
        JdbcMapper<Account> accountMapper = new JdbcMapperImpl<>(Account.class, sessionManager, accountDbExecutor);

        sessionManager.beginSession();
        var account = new Account(0, "Some Account", new BigDecimal(7));
        accountMapper.insertOrUpdate(account);
        logger.info("{} inserted into table", account);
        Account accountFound = accountMapper.findById(account.getNo());

        logger.info("Got from db {}", accountFound);
        sessionManager.commitSession();
    }

    private void createTables(DataSource dataSource) throws SQLException {
        try (var connection = dataSource.getConnection();
             var pst = connection.prepareStatement(
                     "create table user(" +
                            "id bigint(20) NOT NULL auto_increment," +
                            "name varchar(255)," +
                            "age int(3))"
        )) {
            pst.executeUpdate();
        }
        System.out.println("table created");

        try (var connection = dataSource.getConnection();
             var pst = connection.prepareStatement(
                     "create table account(" +
                            "no bigint(20) NOT NULL auto_increment," +
                            "type varchar(255)," +
                            "rest number)"
        )) {
            pst.executeUpdate();
        }
        System.out.println("table created");
    }
}
