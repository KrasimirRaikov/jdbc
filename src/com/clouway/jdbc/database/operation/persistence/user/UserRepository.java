package com.clouway.jdbc.database.operation.persistence.user;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public interface UserRepository {
    void register(User user);

    User findById(Long id);

    User findByEgn(String egn);

    void update(User user);

    void delete(Long id);
}
