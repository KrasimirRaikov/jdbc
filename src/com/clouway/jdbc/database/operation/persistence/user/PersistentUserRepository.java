package com.clouway.jdbc.database.operation.persistence.user;

import com.clouway.jdbc.ExecutionException;
import com.clouway.jdbc.database.operation.persistence.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentUserRepository implements UserRepository {
    private Connection connection;
    private Validator validator;

    public PersistentUserRepository(Connection connection, Validator validator) {
        this.connection = connection;
        this.validator = validator;
    }

    @Override
    public void register(User user) {
        if (!validator.isValid(user)) {
            throw new ExecutionException("unvalid user");
        }
        String sqlStatement = "INSERT INTO users VALUES(?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, user.id);
            preparedStatement.setString(2, user.name);
            preparedStatement.setString(3, user.lastName);
            preparedStatement.setString(4, user.egn);
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
    public User findBy(Long id) {
        String selectById = "SELECT * FROM users WHERE id=?;";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(selectById);
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
            resultSet = preparedStatement.executeQuery();

            resultSet.next();
            long userId = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String surname = resultSet.getString("lastName");
            String egn = resultSet.getString("egn");
            int age = resultSet.getInt("age");
            return new User(userId, name, surname, egn, age);
        } catch (SQLException e) {
            throw new ExecutionException("Could not find user with such id: " + id);
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
    public User findBy(String egn) {
        String sqlStatement = "SELECT * FROM users WHERE egn=?;";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, egn);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            long userId = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String surname = resultSet.getString("lastName");
            String egnReturned = resultSet.getString("egn");
            int age = resultSet.getInt("age");
            return new User(userId, name, surname, egnReturned, age);
        } catch (SQLException e) {
            throw new ExecutionException("No users with such EGN: " + egn);
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
        if (!validator.isValid(user)) {
            throw new ExecutionException("unvalid user");
        }
        String sqlStatement = "UPDATE users SET name=?, surname=?, egn=?, age=? WHERE id=?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, user.name);
            preparedStatement.setString(2, user.lastName);
            preparedStatement.setString(3, user.egn);
            preparedStatement.setInt(4, user.age);
            preparedStatement.setLong(5, user.id);
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
    public void delete(Long id) {
        String deleteById = "DELETE FROM users WHERE id=?;";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(deleteById);
            preparedStatement.setLong(1, id);

            preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new ExecutionException("Could not delete user with id: " + id);
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
