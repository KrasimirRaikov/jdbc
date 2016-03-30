package com.clouway.jdbc.customer.history;

import com.clouway.jdbc.ConnectionManager;
import com.clouway.jdbc.DatabaseTableTool;
import com.clouway.jdbc.ExecutionException;
import com.clouway.jdbc.customer.history.persistence.Customer;
import com.clouway.jdbc.customer.history.persistence.CustomerRecord;
import com.clouway.jdbc.customer.history.persistence.PersistentCustomerHistoryRepository;
import com.clouway.jdbc.customer.history.persistence.PersistentCustomerRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentCustomerHistoryRepositoryTest {
    Connection connection = null;
    PersistentCustomerRepository customerRepository = null;
    PersistentCustomerHistoryRepository customerHistory = null;


    @Before
    public void setUp() {
        ConnectionManager connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection("customer", "postgres", "clouway.com");
        customerRepository = new PersistentCustomerRepository(connection);
        customerHistory = new PersistentCustomerHistoryRepository(connection);
    }

    @After
    public void tearDown() throws SQLException {
        DatabaseTableTool tableTool = new DatabaseTableTool();
        tableTool.clearTable(connection, "customer");
        tableTool.clearTable(connection, "customer_history");
        connection.close();
    }

    @Test
    public void trackUpdateHistory() {
        Customer customer = new Customer(1, "Jack", "Petkovic", "123");
        customerRepository.register(customer);

        Customer customerUpdated = new Customer(1, "Jack", "Paskalev", "9434");
        customerRepository.update(customerUpdated);

        CustomerRecord customerLastUpdate = customerHistory.getLastUpdate(1);
        assertThat(customerLastUpdate.name, is(equalTo(customer.name)));
        assertThat(customerLastUpdate.lastName, is(equalTo(customer.lastName)));
        assertThat(customerLastUpdate.egn, is(equalTo(customer.egn)));
        assertThat(customerRepository.getById(1), is(equalTo(customerUpdated)));
    }

    @Test(expected = ExecutionException.class)
    public void lastUpdateWithoutAnyUpdates() {
        CustomerRecord customerLastUpdate = customerHistory.getLastUpdate(1);
    }

    @Test
    public void multipleUpdatesHistory() {
        Customer customer = new Customer(1, "Jack", "Petkovic", "123");
        customerRepository.register(customer);

        Customer customerFirstUpdate = new Customer(1, "Jack", "Paskalev", "9434");
        customerRepository.update(customerFirstUpdate);

        Customer customerSecondUpdate = new Customer(1, "Jack", "Malkovic", "9434");
        customerRepository.update(customerSecondUpdate);

        List<CustomerRecord> jackHistory = customerHistory.getByCustomerId(1);
        assertThat(jackHistory.size(), is(equalTo(2)));
        assertThat(jackHistory.get(0).lastName, is(equalTo("Petkovic")));
        assertThat(jackHistory.get(1).lastName, is(equalTo("Paskalev")));
    }

    @Test
    public void allCustomersUpdateHistory() {
        Customer customer = new Customer(1, "Jack", "Petkovic", "123");
        Customer secondCustomer = new Customer(2, "Jill", "Malic", "56");
        customerRepository.register(customer);
        customerRepository.register(secondCustomer);


        Customer customerUpdate = new Customer(1, "Jack", "Paskalev", "9434");
        customerRepository.update(customerUpdate);

        Customer secondCustomerUpdate = new Customer(2, "Jill", "Petrovic", "9434");
        customerRepository.update(secondCustomerUpdate);

        List<CustomerRecord> customerRecords = customerHistory.getUpdates();

        assertThat(customerRecords.size(), is(equalTo(2)));
        assertThat(customerRecords.get(0).lastName, is(equalTo("Petkovic")));
        assertThat(customerRecords.get(1).lastName, is(equalTo("Malic")));
    }

}
