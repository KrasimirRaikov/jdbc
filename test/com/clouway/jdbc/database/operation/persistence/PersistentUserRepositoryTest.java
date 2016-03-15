package com.clouway.jdbc.database.operation.persistence;

import com.clouway.jdbc.ConnectionManager;
import com.clouway.jdbc.DatabaseTableTool;
import com.clouway.jdbc.ExecutionException;
import com.clouway.jdbc.database.operation.persistence.user.EGN;
import com.clouway.jdbc.database.operation.persistence.user.ID;
import com.clouway.jdbc.database.operation.persistence.user.PersistentUserRepository;
import com.clouway.jdbc.database.operation.persistence.user.User;
import org.junit.After;
import org.junit.Before;
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
    Connection connection = null;
    PersistentUserRepository userRepository = null;


    @Before
    public void setUp() {
        ConnectionManager connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection("users", "postgres", "clouway.com");
        userRepository = new PersistentUserRepository(connection);
    }

    @After
    public void tearDown() throws SQLException {
        DatabaseTableTool tableTool = new DatabaseTableTool();
        tableTool.clearTable(connection, "users");
        connection.close();
    }

    @Test
    public void insertUser() {
        ID userId = new ID(1);
        EGN userEgn = new EGN("9012122440");
        User user = new User(userId, "John", "Selivan", userEgn, 26);

        userRepository.register(user);
        User returnedUser = userRepository.findBy(userId);
        assertThat(returnedUser, is(equalTo(user)));
    }

    @Test
    public void insertAnotherUsers() {
        ID userId = new ID(1);
        EGN userEgn = new EGN("1234");
        User user = new User(userId, "Mark", "Zukerberg", userEgn, 30);
        ID secondUserId = new ID(2);
        EGN secondUserEgn = new EGN("763");
        User secondUser = new User(secondUserId, "Willy", "Ban", secondUserEgn, 31);

        userRepository.register(user);
        userRepository.register(secondUser);
        User userActual = userRepository.findBy(userId);
        User secondUserActual = userRepository.findBy(secondUserId);
        assertThat(userActual, is(equalTo(user)));
        assertThat(secondUserActual, is(equalTo(secondUser)));
    }

    @Test(expected = ExecutionException.class)
    public void insertExistingUser() {
        ID userID = new ID(1);
        User user = new User(userID, "John", "Selivan", new EGN("456"), 32);
        userRepository.register(user);
        User secondUser = new User(userID, "Jill", "Patrik", new EGN("34572"), 34);
        userRepository.register(secondUser);
    }

    @Test(expected = NullPointerException.class)
    public void insertUserWithoutId() {
        User user = new User(null, "Petar", "Petrov", new EGN("5324"), 23);
        userRepository.register(user);
    }

    @Test(expected = NullPointerException.class)
    public void insertUserWithoutEgn() {
        User user = new User(new ID(1), "Petar", "Petrov", null, 23);
        userRepository.register(user);
    }

    @Test(expected = ExecutionException.class)
    public void insertUserWithoutName() {
        User user = new User(new ID(1), null, "Petrov", new EGN("76"), 23);
        userRepository.register(user);
    }

    @Test
    public void findUserByEgn() {
        ID userID = new ID(1);
        EGN userEgn = new EGN("1234");
        User user = new User(userID, "Mark", "Zukerberg", userEgn, 30);
        userRepository.register(user);
        User userReturned = userRepository.findBy(userEgn);
        assertThat(userReturned, is(equalTo(user)));
    }

    @Test(expected = ExecutionException.class)
    public void findUnregisteredUserById() {
        User user = userRepository.findBy(new ID(1));
    }

    @Test
    public void findAnotherUserByEgn() {
    }


    @Test(expected = ExecutionException.class)
    public void findUnregisteredUserByEgn() {
        User user = userRepository.findBy(new EGN("2452445"));
    }

    @Test
    public void updateUser() {
        ID userId = new ID(1);
        EGN userEgn = new EGN("324589");
        User user = new User(userId, "Lucia", "Kalucio", userEgn, 25);
        userRepository.register(user);
        String newSurname = "Topoli";
        User updatedUser = new User(user.id, user.name, newSurname, user.egn, user.age);
        userRepository.update(updatedUser);
        User userActual = userRepository.findBy(userId);
        assertThat(userActual.surname, is(equalTo(newSurname)));
    }

    @Test(expected = ExecutionException.class)
    public void updateUnregisteredUser() {
        ID userId = new ID(1);
        EGN userEgn = new EGN("5432345");
        User user = new User(userId, "Jack", "Sparrow", userEgn, 40);
        userRepository.update(user);
    }

    @Test(expected = ExecutionException.class)
    public void updateUserWithoutName() {
        ID userId = new ID(1);
        User user = new User(userId, "Nikola", "Nikolov", new EGN("34"), 12);
        userRepository.register(user);
        User updatedUser = new User(userId, null, "Nikolov", new EGN("23"), 25);
        userRepository.update(updatedUser);
    }

    @Test(expected = NullPointerException.class)
    public void updateUserWithoutId() {
        User user = new User(new ID(1), "Kala", "Kalchev", new EGN("34"), 23);
        userRepository.register(user);
        User updateUser = new User(null, "Kala", "Kalchev", new EGN("45"), 3);
        userRepository.update(updateUser);
    }

    @Test(expected = NullPointerException.class)
    public void updateUserWithoutEgn() {
        User user = new User(new ID(1), "Kala", "Kalchev", new EGN("34"), 23);
        userRepository.register(user);
        User updateUser = new User(new ID(1), "Kala", "Kalchev", null, 3);
        userRepository.update(updateUser);
    }

    @Test(expected = ExecutionException.class)
    public void deleteUser() {
        ID userId = new ID(1);
        EGN userEgn = new EGN("9012122440");
        User user = new User(userId, "John", "Selivan", userEgn, 26);
        userRepository.register(user);
        userRepository.delete(userId);

        userRepository.findBy(userId);
    }

    @Test(expected = ExecutionException.class)
    public void deleteUnregisteredUser() {
        userRepository.delete(new ID(1));
    }


}
