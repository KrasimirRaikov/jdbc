package com.clouway.jdbc.database.operation.persistence;

import com.clouway.jdbc.database.operation.persistence.user.User;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public interface Validator {
    boolean isValid(User user);
}
