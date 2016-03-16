package com.clouway.jdbc.database.operation.persistence;

import com.clouway.jdbc.database.operation.persistence.user.User;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class UserValidator implements Validator {
    private int egnLength;

    public UserValidator(int egnLength) {
        this.egnLength = egnLength;
    }

    @Override
    public boolean isValid(User user) {
        return isValidId(user.id)&&isValidName(user.name)&&isValidLastName(user.lastName)&&
                isValidEgn(user.egn);
    }

    private boolean isValidId(Long id) {
        return id!=null;
    }

    private boolean isValidName(String name) {
        return name!=null&&!name.equals("");
    }

    private boolean isValidLastName(String lastName) {
        return lastName!=null&&!lastName.equals("");
    }

    private boolean isValidEgn(String egn) {
        return egn!=null&&egnOnlyDigits(egn)&&egn.length()==egnLength;
    }

    private boolean egnOnlyDigits(String egn) {
        return egn.matches("[0-9]+");
    }
}
