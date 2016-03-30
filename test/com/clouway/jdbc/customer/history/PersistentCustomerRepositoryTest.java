package com.clouway.jdbc.customer.history;

import com.clouway.jdbc.ConnectionManager;
import com.clouway.jdbc.DatabaseTableTool;
import com.clouway.jdbc.ExecutionException;
import com.clouway.jdbc.customer.history.persistence.Customer;
import com.clouway.jdbc.customer.history.persistence.PersistentCustomerRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentCustomerRepositoryTest {

    Connection connection = null;
    PersistentCustomerRepository customerRepository = null;


    @Before
    public void setUp() {
        ConnectionManager connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection("customer", "postgres", "clouway.com");
        customerRepository = new PersistentCustomerRepository(connection);
    }

    @After
    public void tearDown() throws SQLException {
        DatabaseTableTool tableTool = new DatabaseTableTool();
        tableTool.clearTable(connection, "customer");
        connection.close();
    }

    @Test
    public void addCustomer() {
        Customer customer = new Customer(1, "John", "Petkovic", "123");
        customerRepository.register(customer);

        assertThat(customerRepository.getById(1), is(equalTo(customer)));
    }

    @Test
    public void addAnotherCustomer() {
        Customer customer = new Customer(1, "John", "Petkovic", "123");
        customerRepository.register(customer);

        Customer anotherCustomer = new Customer(2, "Jack", "Daniels", "654");
        customerRepository.register(anotherCustomer);

        assertThat(customerRepository.getById(1), is(equalTo(customer)));
        assertThat(customerRepository.getById(2), is(equalTo(anotherCustomer)));
    }

    @Test(expected = ExecutionException.class)
    public void addSameCustomer() {
        Customer customer = new Customer(1, "Jimmy", "Carr", "763");
        customerRepository.register(customer);
        customerRepository.register(customer);
    }

    @Test(expected = ExecutionException.class)
    public void addCustomerWithoutName() {
        Customer customer = new Customer(1, null, "Rogers", "624");
        customerRepository.register(customer);
    }

    @Test
    public void updateCustomer() {
        Customer customer = new Customer(1, "Jack", "Petkovic", "123");
        customerRepository.register(customer);

        Customer customerUpdated = new Customer(1, "Jack", "Paskalev", "9434");
        customerRepository.update(customerUpdated);

        assertThat(customerRepository.getById(1), is(equalTo(customerUpdated)));
    }

    @Test(expected = ExecutionException.class)
    public void updateUnregisteredCustomer() {
        Customer customerUpdated = new Customer(1, "Jack", "Paskalev", "9434");
        customerRepository.update(customerUpdated);
    }

    @Test(expected = ExecutionException.class)
    public void updateCustomerWithoutName() {
        Customer customer = new Customer(1, "Jack", "Petkovic", "123");
        customerRepository.register(customer);

        Customer customerUpdated = new Customer(1, null, "Paskalev", "9434");
        customerRepository.update(customerUpdated);
    }

}
