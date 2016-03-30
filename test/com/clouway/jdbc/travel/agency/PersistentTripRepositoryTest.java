package com.clouway.jdbc.travel.agency;


import com.clouway.jdbc.ConnectionManager;
import com.clouway.jdbc.DatabaseTableTool;
import com.clouway.jdbc.ExecutionException;
import com.clouway.jdbc.travel.agency.persistence.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;


/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentTripRepositoryTest {
    ClientRepository clientRepository;
    TripRepository tripRepository;
    Connection connection;

    @Before
    public void setUp() {
        ConnectionManager connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection("travel_agency", "postgres", "clouway.com");
        clientRepository = new PersistentClientRepository(connection);
        tripRepository = new PersistentTripRepository(connection);
    }

    @After
    public void tearDown() throws SQLException {
        DatabaseTableTool tableTool = new DatabaseTableTool();
        tableTool.clearTable(connection, "people");
        tableTool.clearTable(connection, "trip");
        connection.close();
    }

    @Test
    public void addTrip() {
        Client client = new Client("Micky", "2", 31, "asfd@a.erg");
        clientRepository.register(client);

        Trip trip = new Trip(1, "2", Date.valueOf("2015-3-5"), Date.valueOf("2015-3-15"), "Rom");
        tripRepository.register(trip);

        Trip tripActual = tripRepository.getById(1);

        assertThat(tripActual, is(equalTo(trip)));
    }

    @Test
    public void addAnotherTrip() {
        Client client = new Client("Micky", "2", 31, "asfd@a.erg");
        clientRepository.register(client);

        Trip trip = new Trip(1, "2", Date.valueOf("2015-3-5"), Date.valueOf("2015-3-15"), "Rom");
        tripRepository.register(trip);

        Trip anotherTrip = new Trip(2, "2", Date.valueOf("2016-5-5"), Date.valueOf("2015-6-6"), "Paris");
        tripRepository.register(anotherTrip);

        Trip tripActual = tripRepository.getById(1);
        Trip anotherTripActual = tripRepository.getById(2);

        assertThat(tripActual, is(equalTo(trip)));
        assertThat(anotherTripActual, is(equalTo(anotherTrip)));
    }

    @Test(expected = ExecutionException.class)
    public void addTripWithoutEgn() {
        Client client = new Client("Jane", "1", 21, "ert@as.d");
        clientRepository.register(client);

        Trip trip = new Trip(1, null, Date.valueOf("2015-3-5"), Date.valueOf("2015-3-15"), "Rom");
        tripRepository.register(trip);
    }

    @Test(expected = ExecutionException.class)
    public void addTripWithoutArrival() {
        Client client = new Client("Jane", "1", 21, "ert@as.d");
        clientRepository.register(client);

        Trip trip = new Trip(1, "1", null, Date.valueOf("2015-3-15"), "Rom");
        tripRepository.register(trip);
    }

    @Test(expected = ExecutionException.class)
    public void addTripWithoutDeparture() {
        Client client = new Client("Jane", "1", 21, "ert@as.d");
        clientRepository.register(client);

        Trip trip = new Trip(1, "1", Date.valueOf("2015-3-15"), null, "Rom");
        tripRepository.register(trip);
    }

    @Test
    public void updateTripInfo() {
        Client client = new Client("Bob", "5443", 15, "q@a.we");
        clientRepository.register(client);


        Trip trip = new Trip(1, "5443", Date.valueOf("2015-10-20"), Date.valueOf("2015-11-1"), "Rom");
        tripRepository.register(trip);

        Trip tripUpdated = new Trip(trip.id, trip.egn, Date.valueOf("2015-10-19"), trip.departure, trip.destination);
        tripRepository.update(tripUpdated);

        Trip tripActual = tripRepository.getById(1);

        assertThat(tripActual.arrival, is(equalTo(Date.valueOf("2015-10-19"))));
    }

    @Test(expected = ExecutionException.class)
    public void updateUnregisteredTrip() {
        Client client = new Client("Bob", "1", 15, "q@a.we");
        clientRepository.register(client);
        Trip tripUpdated = new Trip(1, "1", Date.valueOf("2015-10-19"), Date.valueOf("2015-11-1"), "Rom");
        tripRepository.update(tripUpdated);
    }

    @Test
    public void getTripList() {
        Client client = new Client("Malcho", "1", 32, "malcho@a.a");
        Client secondClient = new Client("Mirela", "2", 20, "mirela@m.m");
        registerToRepository(client, secondClient);

        Trip trip = new Trip(1, "1", Date.valueOf("2012-1-12"), Date.valueOf("2012-1-22"), "Las Vegas");
        Trip secondTrip = new Trip(2, "2", Date.valueOf("2014-11-21"), Date.valueOf("2014-11-22"), "Paris");

        List<Trip> trips = new ArrayList<Trip>();
        trips.add(trip);
        trips.add(secondTrip);

        registerToRepository(trip, secondTrip);

        assertThat(tripRepository.getAll(), is(equalTo(trips)));
    }

    @Test
    public void peopleListTripOverlapOnRange() {
        Client client = new Client("Rosen", "1", 50, "rosen@r.bg");
        Client secondClient = new Client("Bilqn", "2", 30, "bilqn@b.bg");
        Client thirdClient = new Client("Mihail", "3", 35, "mishkata@m.bg");

        registerToRepository(client, secondClient, thirdClient);

        Trip clientTrip = new Trip(1, "1", Date.valueOf("2016-7-3"), Date.valueOf("2016-7-9"), "Rom");
        Trip secondClientTrip = new Trip(2, "2", Date.valueOf("2016-7-7"), Date.valueOf("2016-7-15"), "Rom");
        Trip thirdClientTrip = new Trip(3, "3", Date.valueOf("2016-7-16"), Date.valueOf("2016-7-22"), "Rom");

        registerToRepository(clientTrip, secondClientTrip, thirdClientTrip);

        List<Client> expected = new ArrayList<Client>();
        expected.add(client);
        expected.add(secondClient);

        assertThat(tripRepository.clientTripsOverlapBetween(Date.valueOf("2016-7-4"), Date.valueOf("2016-7-20"), "Rom"), is(equalTo(expected)));
    }

    @Test
    public void anotherCityExcluded() {
        Client client = new Client("Rosen", "1", 50, "rosen@r.bg");
        Client secondClient = new Client("Bilqn", "2", 30, "bilqn@b.bg");
        Client thirdClient = new Client("Mihail", "3", 35, "mishkata@m.bg");
        Client forthClient = new Client("Ivan", "4", 21, "ivan@asdf.dfv");

        registerToRepository(client, secondClient, thirdClient, forthClient);

        Trip clientTrip = new Trip(1, "1", Date.valueOf("2016-7-3"), Date.valueOf("2016-7-9"), "Rom");
        Trip secondClientTrip = new Trip(2, "2", Date.valueOf("2016-7-7"), Date.valueOf("2016-7-15"), "Rom");
        Trip thirdClientTrip = new Trip(3, "3", Date.valueOf("2016-7-16"), Date.valueOf("2016-7-22"), "Rom");
        Trip forthClientTrip = new Trip(4, "4", Date.valueOf("2016-7-5"), Date.valueOf("2016-7-10"), "Paris");

        registerToRepository(clientTrip, secondClientTrip, thirdClientTrip, forthClientTrip);


        List<Client> expected = new ArrayList<Client>();
        expected.add(client);
        expected.add(secondClient);

        assertThat(tripRepository.clientTripsOverlapBetween(Date.valueOf("2016-7-4"), Date.valueOf("2016-7-20"), "Rom"), is(equalTo(expected)));
    }

    @Test
    public void twoPeopleOverlap() {
        Client client = new Client("Ivan", "1", 21, "ivan@asd.com");
        Client secondClient = new Client("Petar", "2", 32, "petar@asd.com");
        Client thirdClient = new Client("Petko", "3", 18, "petko@asd.com");
        registerToRepository(client, secondClient, thirdClient);


        Trip clientTrip = new Trip(1, "1", Date.valueOf("2015-05-10"), Date.valueOf("2015-05-21"), "Sofia");
        Trip secondClientTrip = new Trip(2, "2", Date.valueOf("2015-05-10"), Date.valueOf("2015-05-21"), "Sofia");
        Trip thirdClientTrip = new Trip(3, "3", Date.valueOf("2015-06-10"), Date.valueOf("2015-06-21"), "Plovdiv");
        registerToRepository(clientTrip, secondClientTrip, thirdClientTrip);

        List<Client> people = tripRepository.clientTripsOverlapBetween(Date.valueOf("2015-05-10"), Date.valueOf("2016-5-21"), "Sofia");

        assertThat(people.size(), is(2));
        assertThat(people.get(0).name, is("Ivan"));
        assertThat(people.get(1).name, is("Petar"));
    }

    @Test
    public void oneIsNotInRangeAndNotInCity() {
        registerToRepository(new Client("Ivan", "1", 21, "ivan@asd.com"),
                new Client("Petar", "2", 32, "petar@asd.com"),
                new Client("Petko", "3", 18, "petko@asd.com"));

        registerToRepository(new Trip(1, "1", Date.valueOf("2015-05-12"), Date.valueOf("2015-05-25"), "Sofia"),
                new Trip(2, "2", Date.valueOf("2015-05-10"), Date.valueOf("2015-05-21"), "Sofia"),
                new Trip(3, "3", Date.valueOf("2015-06-10"), Date.valueOf("2015-06-21"), "Plovdiv"));

        List<Client> people = tripRepository.clientTripsOverlapBetween(Date.valueOf("2015-05-1"), Date.valueOf("2016-5-30"), "Sofia");

        assertThat(people.size(), is(2));
        assertThat(people.get(0).name, is("Ivan"));
    }

    @Test
    public void threeOutOfSixInRangeAndCity() {
        registerToRepository(new Client("Ivan", "1", 21, "ivan@asd.com"),
                new Client("Petar", "2", 32, "petar@asd.com"),
                new Client("Petko", "3", 18, "petko@asd.com"),
                new Client("Dimitar", "4", 20, "asdqweqwe@asd.com"),
                new Client("Tihomir", "5", 27, "Tihomir@asd.com"),
                new Client("Panayot", "6", 34, "jiji@asd.com"));

        registerToRepository(new Trip(1, "1", Date.valueOf("2015-05-01"), Date.valueOf("2015-05-05"), "Sofia"),
                new Trip(2, "2", Date.valueOf("2015-05-10"), Date.valueOf("2015-05-21"), "Sofia"),
                new Trip(3, "3", Date.valueOf("2015-05-10"), Date.valueOf("2015-05-21"), "Plovdiv"),
                new Trip(4, "4", Date.valueOf("2015-05-15"), Date.valueOf("2015-05-28"), "Sofia"),
                new Trip(5, "5", Date.valueOf("2015-05-06"), Date.valueOf("2015-05-29"), "Sofia"),
                new Trip(6, "6", Date.valueOf("2015-05-31"), Date.valueOf("2015-06-10"), "Sofia"));

        List<Client> people = tripRepository.clientTripsOverlapBetween(Date.valueOf("2015-05-06"), Date.valueOf("2015-05-30"), "Sofia");

        assertThat(people.size(), is(3));
        assertThat(people.get(0).name, is("Petar"));
        assertThat(people.get(1).name, is("Dimitar"));
    }

    @Test
    public void twoInRange() {
        registerToRepository(new Client("Ivan", "1", 21, "ivan@asd.com"),
                new Client("Petar", "2", 32, "petar@asd.com"),
                new Client("Petko", "3", 18, "petko@asd.com"));

        registerToRepository(new Trip(1, "1", Date.valueOf("2015-05-01"), Date.valueOf("2015-05-06"), "Sofia"),
                new Trip(2, "2", Date.valueOf("2015-05-04"), Date.valueOf("2015-05-20"), "Sofia"),
                new Trip(3, "3", Date.valueOf("2015-05-12"), Date.valueOf("2015-05-28"), "Sofia"));


        List<Client> people = tripRepository.clientTripsOverlapBetween(Date.valueOf("2015-05-10"), Date.valueOf("2015-05-20"), "Sofia");

        assertThat(people.size(), is(2));
    }

    @Test
    public void oneInRaneButOutsideCityParameter() {
        registerToRepository(new Client("Ivan", "1", 21, "ivan@asd.com"),
                new Client("Petar", "2", 32, "petar@asd.com"), new Client("Petko", "3", 18, "petko@asd.com"));

        registerToRepository(new Trip(1, "1", Date.valueOf("2014-02-07"), Date.valueOf("2014-02-12"), "Burgas"),
                new Trip(2, "2", Date.valueOf("2014-02-08"), Date.valueOf("2014-02-14"), "Sofia"),
                new Trip(3, "3", Date.valueOf("2014-02-10"), Date.valueOf("2014-02-16"), "Burgas"));

        List<Client> people = tripRepository.clientTripsOverlapBetween(Date.valueOf("2014-02-10"), Date.valueOf("2014-02-20"), "Burgas");

        assertThat(people.size(), is(2));
    }

    @Test
    public void sortCitiesByVisitation() {
        Client client = new Client("Kristian", "1", 23, "kiko@k.k");
        Client secondClient = new Client("Kircho", "2", 43, "as@as.as");
        Client thirdClient = new Client("Petko", "3", 27, "pepo@p.p");

        registerToRepository(client, secondClient, thirdClient);

        Trip clientTrip = new Trip(1, "1", Date.valueOf("2016-7-3"), Date.valueOf("2016-7-9"), "Sofia");
        Trip secondClientTrip = new Trip(2, "2", Date.valueOf("2016-7-7"), Date.valueOf("2016-7-15"), "Sofia");

        Trip thirdClientTrip = new Trip(3, "3", Date.valueOf("2016-7-16"), Date.valueOf("2016-7-22"), "Plovdiv");

        registerToRepository(clientTrip, secondClientTrip, thirdClientTrip);

        List<String> cities = new ArrayList<String>();
        cities.add("Sofia");
        cities.add("Plovdiv");


        assertThat(tripRepository.citiesByPopularity(), is(equalTo(cities)));
    }

    @Test
    public void sortCitiesByNameWhenSameVisitation() {
        Client client = new Client("Kaloian", "1", 12, "as@sd.d");
        Client secondClient = new Client("George", "2", 34, "asd@w.d");
        Client thirdClient = new Client("Mirela", "3", 65, "wer.t");

        registerToRepository(client, secondClient, thirdClient);

        Trip clientTrip = new Trip(1, "1", Date.valueOf("2015-5-15"), Date.valueOf("2015-6-15"), "Varna");
        Trip secondClientTrip = new Trip(2, "2", Date.valueOf("2015-4-12"), Date.valueOf("2015-4-20"), "Varna");
        Trip thirdClientTrip = new Trip(3, "3", Date.valueOf("2015-4-12"), Date.valueOf("2015-4-20"), "Burgas");
        Trip forthClientTrip = new Trip(4, "3", Date.valueOf("2015-4-12"), Date.valueOf("2015-4-20"), "Burgas");

        registerToRepository(clientTrip, secondClientTrip, thirdClientTrip, forthClientTrip);

        List<String> cities = new ArrayList<String>();
        cities.add("Burgas");
        cities.add("Varna");

        assertThat(tripRepository.citiesByPopularity(), is(equalTo(cities)));
    }

    private void registerToRepository(Client... people) {
        for (Client client : people) {
            clientRepository.register(client);
        }
    }

    private void registerToRepository(Trip... trips) {
        for (Trip trip : trips) {
            tripRepository.register(trip);
        }
    }
}
