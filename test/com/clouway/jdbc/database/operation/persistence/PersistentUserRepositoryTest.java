package com.clouway.jdbc.database.operation.persistence;

import com.clouway.jdbc.database.operation.persistence.user.EGN;
import com.clouway.jdbc.database.operation.persistence.user.ID;
import com.clouway.jdbc.database.operation.persistence.user.PersistentUserRepository;
import com.clouway.jdbc.database.operation.persistence.user.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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
        clear();
        connection.close();
    }

    @Test
    public void insertUser() throws SQLException {
        ID johnId = new ID(1);
        EGN johnEgn = new EGN("9012122440");
        User john = new User(johnId, "John", "Selivan", johnEgn, 26);

        userRepository.register(john);
        User returnedUser = userRepository.findBy(johnId);
        assertThat(returnedUser, is(equalTo(john)));
    }

    @Test
    public void insertTwoUsers() throws SQLException {
        ID markId = new ID(1);
        EGN markEgn = new EGN("1234");
        User mark = new User(markId, "Mark", "Zukerberg", markEgn, 30);
        ID willyId = new ID(2);
        EGN willyEgn = new EGN("763");
        User willy = new User(willyId, "Willy", "Ban", willyEgn, 31);

        userRepository.register(mark);
        userRepository.register(willy);
        User markReturned = userRepository.findBy(markId);
        User willyReturned = userRepository.findBy(willyId);
        assertThat(markReturned, is(equalTo(mark)));
        assertThat(willyReturned, is(equalTo(willy)));
    }

    @Test
    public void selectByEgn() throws SQLException {
        ID markID = new ID(1);
        EGN markEgn = new EGN("1234");
        User mark = new User(markID, "Mark", "Zukerberg", markEgn, 30);
        userRepository.register(mark);
        User markReturned = userRepository.findBy(markEgn);
        assertThat(markReturned, is(equalTo(mark)));
    }

    @Test
    public void updateUser() throws SQLException {
        ID luciaId = new ID(1);
        EGN luciaEgn = new EGN("324589");
        User lucia = new User(luciaId, "Lucia", "Kalucio", luciaEgn, 25);
        userRepository.register(lucia);
        String newSurname = "Topoli";
        User updatedLucia = new User(lucia.id, lucia.name, newSurname, lucia.egn, lucia.age);
        userRepository.update(updatedLucia);
        User luciaReturned = userRepository.findBy(luciaId);
        assertThat(luciaReturned.surname, is(equalTo(newSurname)));
    }

    @Test(expected = NoSuchElementException.class)
    public void deleteUser() throws SQLException {
        ID johnId = new ID(1);
        EGN johnEgn = new EGN("9012122440");
        User john = new User(johnId, "John", "Selivan", johnEgn, 26);
        userRepository.register(john);
        userRepository.delete(johnId);

        userRepository.findBy(johnId);
    }

    public void clear() throws SQLException {
        Statement statement = connection.createStatement();

        statement.execute("TRUNCATE TABLE users;");
        statement.close();
    }

}
