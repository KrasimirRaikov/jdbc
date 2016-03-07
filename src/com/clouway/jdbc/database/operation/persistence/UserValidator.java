package com.clouway.jdbc.database.operation.persistence;

import com.clouway.jdbc.database.operation.persistence.user.User;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class UserValidator implements Validator {

    public UserValidator() {
    }

    @Override
    public boolean isValid(User user) {
        if (user.id == null || user.name == null || user.name.equals("") || user.egn == null) {
            return false;
        }

        return true;
    }

}
