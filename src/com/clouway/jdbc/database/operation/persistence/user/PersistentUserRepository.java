package com.clouway.jdbc.database.operation.persistence.user;

import com.clouway.jdbc.ExecutionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentUserRepository implements UserRepository {
    Connection connection;

    public PersistentUserRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void register(User user) {
        String sqlStatement = "INSERT INTO users VALUES(?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, user.id.value);
            preparedStatement.setString(2, user.name);
            preparedStatement.setString(3, user.surname);
            preparedStatement.setString(4, user.egn.value);
            preparedStatement.setInt(5, user.age);
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new ExecutionException("Could not register user with id: " + user.id);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public User findBy(ID id) {
        String selectById = "SELECT * FROM users WHERE id=?;";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(selectById);
            preparedStatement.setLong(1, id.value);
            preparedStatement.execute();
            resultSet = preparedStatement.executeQuery();

            resultSet.next();
            long userId = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String surname = resultSet.getString("surname");
            String egn = resultSet.getString("egn");
            int age = resultSet.getInt("age");
            return new User(new ID(userId), name, surname, new EGN(egn), age);
        } catch (SQLException e) {
            throw new ExecutionException("Could not find user with such id: " + id.value);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public User findBy(EGN egn) {
        String sqlStatement = "SELECT * FROM users WHERE egn=?;";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, egn.value);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            long userId = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String surname = resultSet.getString("surname");
            String egnReturned = resultSet.getString("egn");
            int age = resultSet.getInt("age");
            return new User(new ID(userId), name, surname, new EGN(egnReturned), age);
        } catch (SQLException e) {
            throw new ExecutionException("No users with such EGN: " + egn.value);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @Override
    public void update(User user) {
        String sqlStatement = "UPDATE users SET name=?, surname=?, egn=?, age=? WHERE id=?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, user.name);
            preparedStatement.setString(2, user.surname);
            preparedStatement.setString(3, user.egn.value);
            preparedStatement.setInt(4, user.age);
            preparedStatement.setLong(5, user.id.value);
            if (preparedStatement.executeUpdate() == 0) {
                throw new ExecutionException("could not update user with id: " + user.id);
            }
        } catch (SQLException e) {
            throw new ExecutionException("could not update user with id: " + user.id);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void delete(ID id) {
        String deleteById = "DELETE FROM users WHERE id=?;";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(deleteById);
            preparedStatement.setLong(1, id.value);

            preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new ExecutionException("Could not delete user with id: " + id.value);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
