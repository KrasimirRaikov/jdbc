package com.clouway.jdbc.travel.agency.persistence;

import com.clouway.jdbc.ExecutionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentClientRepository implements ClientRepository {

    private Connection connection;

    public PersistentClientRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void register(Client client) {
        String insertSql = "INSERT INTO people VALUES(?, ?, ?, ?);";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(insertSql);

            preparedStatement.setString(1, client.name);
            preparedStatement.setString(2, client.egn);
            preparedStatement.setInt(3, client.age);
            preparedStatement.setString(4, client.email);

            preparedStatement.execute();
        } catch (SQLException e) {
            throw new ExecutionException("Could not register client");
        } finally {
            closeStatement(preparedStatement);
        }
    }

    @Override
    public Client getByEgn(String egn) {
        String selectUser = "SELECT * FROM people WHERE egn=?;";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet;

        try {
            preparedStatement = connection.prepareStatement(selectUser);
            preparedStatement.setString(1, egn);

            resultSet = preparedStatement.executeQuery();
            resultSet.next();

            String name = resultSet.getString("name");
            String egnReturned = resultSet.getString("egn");
            int age = resultSet.getInt("age");
            String email = resultSet.getString("email");

            return new Client(name, egnReturned, age, email);
        } catch (SQLException e) {
            throw new ExecutionException("Could not find client with such egn: " + egn);
        } finally {
            closeStatement(preparedStatement);
        }
    }

    @Override
    public void update(Client client) {
        String updatePeople = "UPDATE people SET name=?, egn=?, age=?, email=? WHERE egn=?;";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(updatePeople);

            preparedStatement.setString(1, client.name);
            preparedStatement.setString(2, client.egn);
            preparedStatement.setInt(3, client.age);
            preparedStatement.setString(4, client.email);
            preparedStatement.setString(5, client.egn);

            if (preparedStatement.executeUpdate() == 0) {
                throw new ExecutionException("could not update user with id: " + client.egn);
            }
        } catch (SQLException e) {
            throw new ExecutionException("Could not update client with egn " + client.egn);
        } finally {
            closeStatement(preparedStatement);
        }
    }

    @Override
    public List<Client> getAll() {
        return getWithNameBeggining("");
    }

    @Override
    public List<Client> getWithNameBeggining(String prefix) {
        String clientsLike = "SELECT * FROM people WHERE name LIKE '" + prefix + "%';";

        Statement statement = null;
        ResultSet resultSet;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(clientsLike);

            List<Client> clientList = new ArrayList();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String egn = resultSet.getString("egn");
                int age = resultSet.getInt("age");
                String email = resultSet.getString("email");

                clientList.add(new Client(name, egn, age, email));
            }
            return clientList;
        } catch (SQLException e) {
            throw new ExecutionException("Could not find clients whose names begin with: " + prefix);
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
