package com.clouway.jdbc.travel.agency;

import com.clouway.jdbc.ConnectionManager;
import com.clouway.jdbc.DatabaseTableTool;
import com.clouway.jdbc.ExecutionException;
import com.clouway.jdbc.travel.agency.persistence.Client;
import com.clouway.jdbc.travel.agency.persistence.ClientRepository;
import com.clouway.jdbc.travel.agency.persistence.PersistentClientRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentClientRepositoryTest {
    ClientRepository clientRepository;
    Connection connection;

    @Before
    public void setUp() {
        ConnectionManager connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection("travel_agency", "postgres", "clouway.com");
        clientRepository = new PersistentClientRepository(connection);
    }

    @After
    public void tearDown() throws SQLException {
        DatabaseTableTool tableTool = new DatabaseTableTool();
        tableTool.clearTable(connection, "people");
        connection.close();
    }

    @Test
    public void addClient() {
        Client client = new Client("John", "1", 25, "john@johm.com");
        clientRepository.register(client);
        Client johnReturned = clientRepository.getByEgn("1");

        assertThat(johnReturned, is(equalTo(client)));
    }

    @Test
    public void addAnotherClient() {
        Client client = new Client("Meliko", "1", 45, "trwe");
        clientRepository.register(client);

        Client secondClient = new Client("Fireboy", "2", 75, "rtebv");
        clientRepository.register(secondClient);

        Client clientActual = clientRepository.getByEgn("1");
        Client secondClientActual = clientRepository.getByEgn("2");

        assertThat(clientActual, is(equalTo(client)));
        assertThat(secondClientActual, is(equalTo(secondClient)));
    }

    @Test(expected = ExecutionException.class)
    public void addClientWithoutName() {
        Client client = new Client(null, "1", 23, "ert");
        clientRepository.register(client);
    }

    @Test(expected = ExecutionException.class)
    public void addClientWithoutEgn() {
        Client client = new Client("John", null, 23, "ert");
        clientRepository.register(client);
    }

    @Test
    public void updateClientInfo() {
        Client client = new Client("Mark", "123", 20, "mark@a.d");
        clientRepository.register(client);

        String newEmail = "mark@gmail.com";
        Client clientUpdated = new Client(client.name, client.egn, 21, newEmail);
        clientRepository.update(clientUpdated);

        Client markUpdatedReturned = clientRepository.getByEgn("123");

        assertThat(markUpdatedReturned.email, is(equalTo(newEmail)));
    }

    @Test(expected = ExecutionException.class)
    public void updateUnregisteredClient() {
        Client client = new Client("Ivan", "6323", 24, "werty");
        clientRepository.update(client);
    }

    @Test(expected = ExecutionException.class)
    public void updateClientWithoutName() {
        Client client = new Client("Mark", "123", 20, "mark@a.d");
        clientRepository.register(client);

        String newEmail = "mark@gmail.com";
        Client clientUpdated = new Client(null, client.egn, 21, newEmail);
        clientRepository.update(clientUpdated);
    }

    @Test(expected = ExecutionException.class)
    public void updateClientWithoutEgn() {
        Client client = new Client("Mark", "123", 20, "mark@a.d");
        clientRepository.register(client);

        String newEmail = "mark@gmail.com";
        Client clientUpdated = new Client(client.name, null, 21, newEmail);
        clientRepository.update(clientUpdated);
    }

    @Test
    public void clientList() {
        Client client = new Client("Linch", "1", 20, "linch@a.bg");
        Client secondClient = new Client("Pako", "2", 27, "pako@a.bg");
        Client thirdClient = new Client("Zoro", "3", 50, "zoro@zoro.zoro");

        List<Client> expectedClients = new ArrayList<Client>();
        expectedClients.add(client);
        expectedClients.add(secondClient);
        expectedClients.add(thirdClient);

        pretendRegisteredClientsAre(client, secondClient, thirdClient);

        assertThat(clientRepository.getAll(), is(equalTo(expectedClients)));
    }

    @Test
    public void emptyClientList() {
        List<Client> expectedClients = new ArrayList<Client>();
        assertThat(clientRepository.getAll(), is(equalTo(expectedClients)));
    }

    @Test
    public void clientListNamesBeginning() {
        Client client = new Client("Kaloian", "1", 12, "kalata@k.k");
        Client secondClient = new Client("Krasimir", "2", 22, "k@k.k");
        Client thirdClient = new Client("Petar", "3", 40, "p@k.k");

        List<Client> peopleWithK = new ArrayList<Client>();
        peopleWithK.add(client);
        peopleWithK.add(secondClient);

        pretendRegisteredClientsAre(client, secondClient, thirdClient);

        assertThat(clientRepository.getWithNameBeggining("K"), is(equalTo(peopleWithK)));
    }

    @Test
    public void emptyListPeoplesNamesBeginningWith() {
        List<Client> expectedClients = new ArrayList<Client>();

        assertThat(clientRepository.getWithNameBeggining("S"), is(equalTo(expectedClients)));
    }

    private void pretendRegisteredClientsAre(Client... client) {
        for (Client client1 : client) {
            clientRepository.register(client1);
        }
    }
}
