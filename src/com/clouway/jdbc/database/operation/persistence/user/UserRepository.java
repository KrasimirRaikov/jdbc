package com.clouway.jdbc.database.operation.persistence.user;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public interface UserRepository {
    void register(User user);

    User findBy(ID id);

    User findBy(EGN egn);

    void update(User user);

    void delete(ID id);
}
