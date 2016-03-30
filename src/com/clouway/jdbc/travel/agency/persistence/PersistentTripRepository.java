package com.clouway.jdbc.travel.agency.persistence;

import com.clouway.jdbc.ExecutionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentTripRepository implements TripRepository {
    private Connection connection;

    public PersistentTripRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void register(Trip trip) {
        String insertTrip = "INSERT INTO trip VALUES(?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(insertTrip);

            preparedStatement.setLong(1, trip.id);
            preparedStatement.setString(2, trip.egn);
            preparedStatement.setDate(3, trip.arrival);
            preparedStatement.setDate(4, trip.departure);
            preparedStatement.setString(5, trip.destination);

            preparedStatement.execute();
        } catch (SQLException e) {
            throw new ExecutionException("Could not register a trip: " + trip.id);
        } finally {
            closeStatement(preparedStatement);
        }
    }

    @Override
    public Trip getById(long id) {
        String selectTrip = "SELECT * FROM trip WHERE id=?";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet;
        try {
            preparedStatement = connection.prepareStatement(selectTrip);
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();
            resultSet.next();

            long tripId = resultSet.getLong("id");
            String egn = resultSet.getString("egn");
            Date arrival = resultSet.getDate("arrival");
            Date departure = resultSet.getDate("departure");
            String destination = resultSet.getString("city");

            return new Trip(tripId, egn, arrival, departure, destination);
        } catch (SQLException e) {
            throw new ExecutionException("Could not find trip with ID: " + id);
        } finally {
            closeStatement(preparedStatement);
        }
    }

    @Override
    public void update(Trip trip) {
        String updateTrip = "UPDATE trip SET egn=?, arrival=?, departure=?, city=? WHERE id=?";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(updateTrip);

            preparedStatement.setString(1, trip.egn);
            preparedStatement.setDate(2, trip.arrival);
            preparedStatement.setDate(3, trip.departure);
            preparedStatement.setString(4, trip.destination);
            preparedStatement.setLong(5, trip.id);

            if (preparedStatement.executeUpdate() == 0) {
                throw new ExecutionException("could not update user with id: " + trip.id);
            }
        } catch (SQLException e) {
            throw new ExecutionException("Could not update trip [ID:" + trip.id + "]");
        } finally {
            closeStatement(preparedStatement);
        }
    }

    @Override
    public List<Trip> getAll() {
        List<Trip> tripsList = new ArrayList();

        Statement statement = null;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM trip;");

            while (resultSet.next()) {

                long id = resultSet.getLong("id");
                String egn = resultSet.getString("egn");
                Date arrival = resultSet.getDate("arrival");
                Date departure = resultSet.getDate("departure");
                String destination = resultSet.getString("city");

                tripsList.add(new Trip(id, egn, arrival, departure, destination));
            }

            return tripsList;
        } catch (SQLException e) {
            throw new ExecutionException("Could not gather the Trips list");
        } finally {
            closeStatement(statement);
        }
    }

    @Override
    public List<String> citiesByPopularity() {
        String selectCities = "SELECT city FROM trip GROUP BY city ORDER BY count(egn) DESC, city ASC ;";
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(selectCities);
            List<String> cities = new ArrayList<String>();
            while (resultSet.next()) {
                String city = resultSet.getString("city");
                cities.add(city);
            }
            return cities;
        } catch (SQLException e) {
            throw new ExecutionException("Could not load the cities by trip popularity");
        } finally {
            closeStatement(statement);
        }
    }

    @Override
    public List<Client> clientTripsOverlapBetween(Date startDate, Date endDate, String city) {
        String subQuery = "Select people.*, trip.arrival, trip.departure, trip.city from trip inner" +
                " join people on trip.egn=people.egn where arrival<'" + endDate + "' and departure>'" + startDate + "' and city ='" + city + "'";

        String query = "select * from (" + subQuery + ") as a inner join (" + subQuery + ") as b on a.city= b.city where "
                + " a.egn!=b.egn and a.arrival<b.departure and a.departure>b.arrival;";

        Statement statement = null;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            List<Client> peopleTripsOverlap = new ArrayList();

            while (resultSet.next()) {

                String name = resultSet.getString("name");
                String egn = resultSet.getString("egn");
                int age = resultSet.getInt("age");
                String email = resultSet.getString("email");
                Client client = new Client(name, egn, age, email);

                if (!peopleTripsOverlap.contains(client)) {
                    peopleTripsOverlap.add(client);
                }
            }

            return peopleTripsOverlap;
        } catch (SQLException e) {
            throw new ExecutionException("Could not load the people whose trips overlap");
        } finally {
            closeStatement(statement);
        }
    }

    private void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
