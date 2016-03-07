package com.clouway.jdbc.database.operation.persistence;

import com.clouway.jdbc.database.operation.persistence.user.User;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class UserValidatorTest {
    private Validator userValidator;

    @Before
    public void setUp() throws Exception {
        userValidator = new UserValidator();
    }

    @Test
    public void validateUser() {
        User user = new User(1L, "Silvestur", "Stalone", "6541345634", 54);

        assertThat(userValidator.isValid(user), is(equalTo(true)));
    }

    @Test
    public void validateUserWithoutId() {
        User user = new User(null, "Silvestur", "Stalone", "6541345634", 54);

        assertThat(userValidator.isValid(user), is(equalTo(false)));
    }

    @Test
    public void validateUserWithoutName() {
        User user = new User(1L, null, "Ert", "2345678925", 21);

        assertThat(userValidator.isValid(user), is(equalTo(false)));
    }

    @Test
    public void validateUserWithNameWithoutLetters() {
        User user = new User(1L, "", "Ert", "2345678925", 21);

        assertThat(userValidator.isValid(user), is(equalTo(false)));
    }

    @Test
    public void withoutEgn() {
        User user = new User(1L, "Ben", "Big", null, 12);
        assertThat(userValidator.isValid(user), is(equalTo(false)));
    }
}
