package com.clouway.jdbc.database.operation.persistence;

import com.clouway.jdbc.ConnectionManager;
import com.clouway.jdbc.DatabaseTableTool;
import com.clouway.jdbc.ExecutionException;
import com.clouway.jdbc.database.operation.persistence.user.PersistentUserRepository;
import com.clouway.jdbc.database.operation.persistence.user.User;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentUserRepositoryTest {
    private Connection connection;
    private PersistentUserRepository userRepository;

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Mock
    Validator validator;


    @Before
    public void setUp() {
        ConnectionManager connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection("users", "postgres", "clouway.com");
        userRepository = new PersistentUserRepository(connection, validator);
    }

    @After
    public void tearDown() throws SQLException {
        DatabaseTableTool tableTool = new DatabaseTableTool();
        tableTool.clearTable(connection, "users");
        connection.close();
    }

    @Test
    public void insertUser() {
        Long userId = 1L;
        String userEgn = "9012122440";
        User user = new User(userId, "John", "Selivan", userEgn, 26);

        context.checking(new Expectations() {{
            oneOf(validator).isValid(user);
            will(returnValue(true));
        }});

        userRepository.register(user);

        User returnedUser = userRepository.findById(userId);
        assertThat(returnedUser, is(equalTo(user)));
    }

    @Test
    public void insertAnotherUser() {
        User user = new User(1L, "Petar", "Pan","784956", 76);

        context.checking(new Expectations() {{
            oneOf(validator).isValid(user);
            will(returnValue(true));
        }});

        userRepository.register(user);

        User returnedUser = userRepository.findById(1L);
        assertThat(returnedUser, is(equalTo(user)));
    }

    @Test(expected = ExecutionException.class)
    public void insertExistingUser() {
        Long userID = 1L;
        User user = new User(userID, "John", "Selivan", "456", 32);

        User secondUser = new User(userID, "Jill", "Patrik", "34572", 34);
        context.checking(new Expectations() {{
            oneOf(validator).isValid(user);
            will(returnValue(true));
            oneOf(validator).isValid(secondUser);
            will(returnValue(true));
        }});
        userRepository.register(user);

        userRepository.register(secondUser);
    }

    @Test(expected = ExecutionException.class)
    public void insertUserWithoutId() {
        User user = new User(null, "Petar", "Petrov", "5324", 23);

        context.checking(new Expectations() {{
            oneOf(validator).isValid(user);
            will(returnValue(false));
        }});
        userRepository.register(user);
    }

    @Test(expected = ExecutionException.class)
    public void insertUserWithoutEgn() {
        User user = new User(1L, "Petar", "Petrov", null, 23);

        context.checking(new Expectations() {{
            oneOf(validator).isValid(user);
            will(returnValue(false));
        }});
        userRepository.register(user);
    }

    @Test(expected = ExecutionException.class)
    public void insertUserWithoutName() {
        User user = new User(1L, null, "Petrov", "76", 23);
        context.checking(new Expectations() {{
            oneOf(validator).isValid(user);
            will(returnValue(false));
        }});
        userRepository.register(user);
    }

    @Test
    public void findUserByEgn() {
        Long userID = 1L;
        String userEgn = "1234";
        User user = new User(userID, "Mark", "Zukerberg", userEgn, 30);

        context.checking(new Expectations() {{
            oneOf(validator).isValid(user);
            will(returnValue(true));
        }});

        userRepository.register(user);
        User userReturned = userRepository.findByEgn(userEgn);
        assertThat(userReturned, is(equalTo(user)));
    }

    @Test(expected = ExecutionException.class)
    public void findUnregisteredUserById() {
        User user = userRepository.findById(1L);
    }

    @Test
    public void findAnotherUserByEgn() {
    }


    @Test(expected = ExecutionException.class)
    public void findUnregisteredUserByEgn() {
        User user = new User(1L, "Marty", "Milk", "345", 23);
        context.checking(new Expectations() {{
            oneOf(validator).isValid(user);
            will(returnValue(true));
        }});
        userRepository.register(user);
        User userReturned = userRepository.findByEgn("2452445");
    }

    @Test(expected = ExecutionException.class)
    public void findUserWithEmptyEgn() {
        User user = new User(1L, "Pall", "Mall", "2456", 22);
        context.checking(new Expectations() {{
            oneOf(validator).isValid(user);
        }});
        userRepository.register(user);
        userRepository.findByEgn("");
    }

    @Test
    public void updateUser() {
        Long userId = 1L;
        String userEgn = "324589";
        User user = new User(userId, "Lucia", "Kalucio", userEgn, 25);
        String newSurname = "Topoli";
        User updatedUser = new User(user.id, user.name, newSurname, user.egn, user.age);

        context.checking(new Expectations() {{
            oneOf(validator).isValid(user);
            will(returnValue(true));
            oneOf(validator).isValid(updatedUser);
            will(returnValue(true));
        }});
        userRepository.register(user);
        userRepository.update(updatedUser);

        User userActual = userRepository.findById(userId);
        assertThat(userActual.lastName, is(equalTo(newSurname)));
    }

    @Test(expected = ExecutionException.class)
    public void updateUnregisteredUser() {
        User user = new User(1L, "Jack", "Sparrow", "5432345", 40);

        context.checking(new Expectations() {{
            oneOf(validator).isValid(user);
            will(returnValue(true));
        }});

        userRepository.update(user);
    }

    @Test(expected = ExecutionException.class)
    public void updateUserWithoutName() {
        Long userId = 1L;
        User user = new User(userId, "Nikola", "Nikolov", "34", 12);
        User updatedUser = new User(userId, null, "Nikolov", "23", 25);

        context.checking(new Expectations() {{
            oneOf(validator).isValid(user);
            will(returnValue(true));
            oneOf(validator).isValid(updatedUser);
            will(returnValue(false));
        }});
        userRepository.register(user);
        userRepository.update(updatedUser);
    }

    @Test(expected = ExecutionException.class)
    public void updateUserWithoutId() {
        User user = new User(1L, "Kala", "Kalchev", "34", 23);

        User updateUser = new User(null, "Kala", "Kalchev", "45", 3);

        context.checking(new Expectations() {{
            oneOf(validator).isValid(user);
            will(returnValue(true));
            oneOf(validator).isValid(updateUser);
            will(returnValue(false));
        }});
        userRepository.register(user);

        userRepository.update(updateUser);
    }

    @Test(expected = ExecutionException.class)
    public void updateUserWithoutEgn() {
        User user = new User(1L, "Kala", "Kalchev", "34", 23);
        User updateUser = new User(1L, "Kala", "Kalchev", null, 3);

        context.checking(new Expectations() {{
            oneOf(validator).isValid(user);
            will(returnValue(true));
            oneOf(validator).isValid(updateUser);
            will(returnValue(false));
        }});
        userRepository.register(user);
        userRepository.update(updateUser);
    }

    @Test(expected = ExecutionException.class)
    public void deleteUser() {
        Long userId = 1L;
        String userEgn = "9012122440";
        User user = new User(userId, "John", "Selivan", userEgn, 26);
        context.checking(new Expectations() {{
            oneOf(validator).isValid(user);
            will(returnValue(true));
        }});
        userRepository.register(user);
        userRepository.delete(userId);

        userRepository.findById(userId);
    }

    @Test(expected = ExecutionException.class)
    public void deleteUnregisteredUser() {
        userRepository.delete(1L);
    }


}
