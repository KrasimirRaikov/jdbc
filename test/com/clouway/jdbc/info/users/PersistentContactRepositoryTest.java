package com.clouway.jdbc.info.users;

import com.clouway.jdbc.ConnectionProvider;
import com.clouway.jdbc.DatabaseTableTool;
import com.clouway.jdbc.ExecutionException;
import com.clouway.jdbc.info.users.persistence.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.clouway.jdbc.ConnectionProvider.getConnection;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentContactRepositoryTest {
    private PersistentUserRepository userRepository;
    private PersistentAddressRepository addressRepository;
    private PersistentContactRepository contactRepository;


    @Before
    public void setUp() {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        userRepository = new PersistentUserRepository(connectionPool);
        addressRepository = new PersistentAddressRepository(connectionPool);
        contactRepository = new PersistentContactRepository(connectionPool);
        User user = new User(1L, "Kaloian");
        Address address = new Address(1L, "Berlin", "bull str.");

        userRepository.register(user);
        addressRepository.register(address);
    }

    @After
    public void tearDown() throws SQLException {
        DatabaseTableTool tableTool = new DatabaseTableTool();
        Connection connection = getConnection("user_info", "postgres", "clouway.com");
        tableTool.clearTable(connection, "users");
        tableTool.clearTable(connection, "contact");
        tableTool.clearTable(connection, "address");

    }

    @Test
    public void addContact() {
        contactRepository.register(1L, 1L, 1L);

        Contact userContactExpected = new Contact(1L, "Kaloian", "Berlin", "bull str.");

        Contact userContactActual = contactRepository.findById(1L);
        assertThat(userContactActual, is(equalTo(userContactExpected)));
    }

    @Test
    public void addAnotherContact() {
        Address userAddress = new Address(2L, "paris", "what str.");
        addressRepository.register(userAddress);

        contactRepository.register(1L, 1L, 2L);

        Contact userContactExpected = new Contact(1L, "Kaloian", "paris", "what str.");
        Contact userContactActual = contactRepository.findById(1L);
        assertThat(userContactActual, is(equalTo(userContactExpected)));
    }

    @Test
    public void getContactList() {
        Address userSecondAddress = new Address(2L, "mondo", "ben str.");
        addressRepository.register(userSecondAddress);
        contactRepository.register(1L, 1L, 1L);
        contactRepository.register(2L, 1L, 2L);

        List<Contact> expectedContacts = new ArrayList<>();
        expectedContacts.add(new Contact(1L, "Kaloian", "Berlin", "bull str."));
        expectedContacts.add(new Contact(2L, "Kaloian", "mondo", "ben str."));

        List<Contact> actualContacts = contactRepository.findAll();

        assertThat(actualContacts, is(equalTo(expectedContacts)));

    }

    @Test(expected = ExecutionException.class)
    public void getUnexistingContact() {
        contactRepository.findById(1L);
    }

    @Test(expected = ExecutionException.class)
    public void addExistingContact() {
        contactRepository.register(1L, 1L, 1L);

        User secondUser = new User(1, "Veronika");
        userRepository.register(secondUser);

        contactRepository.register(1L, 2L, 1L);
    }

}
