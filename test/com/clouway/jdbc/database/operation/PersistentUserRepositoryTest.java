package com.clouway.jdbc.database.operation;

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

        userRepository.registerUser(john);
        User returnedUser = userRepository.getUser(1);
        assertThat(returnedUser, is(equalTo(john)));
    }

    @Test
    public void insertTwoUsers() throws SQLException {
        User mark = new User(1, "Mark", "Zukerberg", "1234", 30);
        User willy = new User(2, "Willy", "Ban", "763", 31);

        userRepository.registerUser(mark);
        userRepository.registerUser(willy);
        User markReturned = userRepository.getUser(1);
        User willyReturned = userRepository.getUser(2);
        assertThat(markReturned, is(equalTo(mark)));
        assertThat(willyReturned, is(equalTo(willy)));
    }

    @Test
    public void selectByEgn() throws SQLException {
        User mark = new User(1, "Mark", "Zukerberg", "1234", 30);
        userRepository.registerUser(mark);
        User markReturned = userRepository.getByEgn("1234");
        assertThat(markReturned, is(equalTo(mark)));
    }

    @Test
    public void updateUser() throws SQLException {
        User lucia = new User(1, "Lucia", "Kalucio", "324589", 25);
        userRepository.registerUser(lucia);
        String newSurname = "Topoli";
        User updatedLucia = new User(lucia.id, lucia.name, newSurname, lucia.egn, lucia.age);
        userRepository.updateUser(updatedLucia);
        User luciaReturned = userRepository.getUser(1);
        assertThat(luciaReturned.surname, is(equalTo(newSurname)));
    }

    @Test(expected = NoSuchElementException.class)
    public void deleteUser() throws SQLException {
        User john = new User(1, "John", "Selivan", "9012122440", 26);
        userRepository.registerUser(john);
        userRepository.deleteUser(1);

        userRepository.getUser(1);
    }

    @Test
    public void tableExists() throws SQLException {
        boolean usersTableExists = userRepository.tableExists("users");
        assertThat(usersTableExists, is(equalTo(true)));
    }

    @Test
    public void tableDoesNotExists() throws SQLException {
        boolean clientsDosntExist = userRepository.tableExists("clients");
        assertThat(clientsDosntExist, is(equalTo(false)));
    }

    @Test
    public void renameTable() throws SQLException {
        userRepository.renameTable("users", "renamed_users");
        boolean renamed = userRepository.tableExists("renamed_users");

        userRepository.renameTable("renamed_users", "users");
        boolean returnedOldName = userRepository.tableExists("users");

        assertThat(renamed, is(equalTo(true)));
        assertThat(returnedOldName, is(equalTo(true)));
    }

    @Test
    public void destroyTable() throws SQLException {
        userRepository.destroyTable("users");
        boolean tableDestroyed = !userRepository.tableExists("users");
        userRepository.createRepository();
        assertThat(tableDestroyed, is(equalTo(true)));
    }

}
