package com.clouway.jdbc.database.operation.persistence;

import com.clouway.jdbc.database.operation.ConnectionManager;
import com.clouway.jdbc.database.operation.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.NoSuchElementException;

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
    public void setUp() throws Exception {
        ConnectionManager connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection("users", "postgres", "clouway.com");
        userRepository = new PersistentUserRepository(connection);
    }

    @After
    public void tearDown() throws SQLException {
        userRepository.clear();
        connection.close();
    }

    @Test
    public void insertUser() throws SQLException {
        User john = new User(1, "John", "Selivan", "9012122440", 26);

        userRepository.register(john);
        User returnedUser = userRepository.findById(1);
        assertThat(returnedUser, is(equalTo(john)));
    }

    @Test
    public void insertTwoUsers() throws SQLException {
        User mark = new User(1, "Mark", "Zukerberg", "1234", 30);
        User willy = new User(2, "Willy", "Ban", "763", 31);

        userRepository.register(mark);
        userRepository.register(willy);
        User markReturned = userRepository.findById(1);
        User willyReturned = userRepository.findById(2);
        assertThat(markReturned, is(equalTo(mark)));
        assertThat(willyReturned, is(equalTo(willy)));
    }

    @Test
    public void selectByEgn() throws SQLException {
        User mark = new User(1, "Mark", "Zukerberg", "1234", 30);
        userRepository.register(mark);
        User markReturned = userRepository.findByEgn("1234");
        assertThat(markReturned, is(equalTo(mark)));
    }

    @Test
    public void updateUser() throws SQLException {
        User lucia = new User(1, "Lucia", "Kalucio", "324589", 25);
        userRepository.register(lucia);
        String newSurname = "Topoli";
        User updatedLucia = new User(lucia.id, lucia.name, newSurname, lucia.egn, lucia.age);
        userRepository.updateUser(updatedLucia);
        User luciaReturned = userRepository.findById(1);
        assertThat(luciaReturned.surname, is(equalTo(newSurname)));
    }

    @Test(expected = NoSuchElementException.class)
    public void deleteUser() throws SQLException {
        User john = new User(1, "John", "Selivan", "9012122440", 26);
        userRepository.register(john);
        userRepository.deleteUser(1);

        userRepository.findById(1);
    }

}
