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
        ID johnId = new ID(1);
        EGN johnEgn = new EGN("9012122440");
        User john = new User(johnId, "John", "Selivan", johnEgn, 26);

        userRepository.register(john);
        User returnedUser = userRepository.findBy(johnId);
        assertThat(returnedUser, is(equalTo(john)));
    }

    @Test
    public void insertAnotherUsers() {
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
        ID markID = new ID(1);
        EGN markEgn = new EGN("1234");
        User mark = new User(markID, "Mark", "Zukerberg", markEgn, 30);
        userRepository.register(mark);
        User markReturned = userRepository.findBy(markEgn);
        assertThat(markReturned, is(equalTo(mark)));
    }

    @Test(expected = ExecutionException.class)
    public void findUnregisteredUserById() {
        User john = userRepository.findBy(new ID(1));
    }

    @Test
    public void findAnotherUserByEgn() {
    }


    @Test(expected = ExecutionException.class)
    public void findUnregisteredUserByEgn() {
        User john = userRepository.findBy(new EGN("2452445"));
    }

    @Test
    public void updateUser() {
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

    @Test(expected = ExecutionException.class)
    public void updateUnregisteredUser() {
        ID jackId = new ID(1);
        EGN jackEgn = new EGN("5432345");
        User jack = new User(jackId, "Jack", "Sparrow", jackEgn, 40);
        userRepository.update(jack);
    }

    @Test(expected = ExecutionException.class)
    public void updateUserWithoutName() {
        ID nikolasId = new ID(1);
        User nikola = new User(nikolasId, "Nikola", "Nikolov", new EGN("34"), 12);
        userRepository.register(nikola);
        User updatedNikola = new User(nikolasId, null, "Nikolov", new EGN("23"), 25);
        userRepository.update(updatedNikola);
    }

    @Test(expected = ExecutionException.class)
    public void deleteUser() {
        ID johnId = new ID(1);
        EGN johnEgn = new EGN("9012122440");
        User john = new User(johnId, "John", "Selivan", johnEgn, 26);
        userRepository.register(john);
        userRepository.delete(johnId);

        userRepository.findBy(johnId);
    }

    @Test(expected = ExecutionException.class)
    public void deleteUnregisteredUser() {
        userRepository.delete(new ID(1));
    }


}
