package com.clouway.jdbc.info.users;

import com.clouway.jdbc.ConnectionProvider;
import com.clouway.jdbc.DatabaseTableTool;
import com.clouway.jdbc.ExecutionException;
import com.clouway.jdbc.info.users.persistence.ConnectionPool;
import com.clouway.jdbc.info.users.persistence.PersistentUserRepository;
import com.clouway.jdbc.info.users.persistence.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.clouway.jdbc.ConnectionProvider.getConnection;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentUserRepositoryTest {
    private PersistentUserRepository userRepository;

    @Before
    public void setUp() {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        userRepository = new PersistentUserRepository(connectionPool);
    }

    @After
    public void tearDown() throws SQLException {
        Connection connection = getConnection("user_info", "postgres", "clouway.com");
        DatabaseTableTool tableTool = new DatabaseTableTool();
        tableTool.clearTable(connection, "users");
        connection.close();
    }

    @Test
    public void addUser() {
        User user = new User(1, "Sisi");

        userRepository.register(user);

        User sisiReturned = userRepository.findById(user.id);
        assertThat(sisiReturned.id, is(equalTo(user.id)));
        assertThat(sisiReturned.name, is(equalTo(user.name)));
    }

    @Test
    public void addSecondUser() {
        User user = new User(1, "Sisi");
        userRepository.register(user);

        User secondUser = new User(2, "Simona");
        userRepository.register(secondUser);

        User userActual = userRepository.findById(user.id);
        User secondUserActual = userRepository.findById(secondUser.id);
        assertThat(userActual.id, is(equalTo(user.id)));
        assertThat(userActual.name, is(equalTo(user.name)));
        assertThat(secondUserActual.id, is(equalTo(secondUser.id)));
        assertThat(secondUserActual.name, is(equalTo(secondUser.name)));
    }

    @Test
    public void getUsersList() {
        pretendRegisteredUsersAre(new User(1, "Marko"), new User(2, "Petar"));

        List<User> users = userRepository.findAll();

        assertThat(users.get(0).id, is(equalTo(1L)));
        assertThat(users.get(0).name, is(equalTo("Marko")));
        assertThat(users.get(1).id, is(equalTo(2L)));
        assertThat(users.get(1).name, is(equalTo("Petar")));
    }

    @Test(expected = ExecutionException.class)
    public void addUserWithTakenId() {
        User user = new User(1, "Pavel");

        userRepository.register(user);
        User user2 = new User(1, "Margaret");
        userRepository.register(user2);
    }

    @Test(expected = ExecutionException.class)
    public void getUserByUnregisteredId() {
        userRepository.findById(1);
    }

    private void pretendRegisteredUsersAre(User... users) {
        for (User user : users) {
            userRepository.register(user);
        }
    }
}
