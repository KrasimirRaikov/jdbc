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
        userValidator = new UserValidator(10);
    }

    @Test
    public void validUser() {
        User user = new User(1L, "Silvestur", "Stalone", "6541345634", 54);

        assertThat(userValidator.isValid(user), is(equalTo(true)));
    }

    @Test
    public void idNotValid() {
        User user = new User(null, "Silvestur", "Stalone", "6541345634", 54);

        assertThat(userValidator.isValid(user), is(equalTo(false)));
    }

    @Test
    public void withoutName() {
        User user = new User(1L, null, "Ert", "2345678925", 21);

        assertThat(userValidator.isValid(user), is(equalTo(false)));
    }

    @Test
    public void emptyName() {
        User user = new User(1L, "", "Ert", "2345678925", 21);

        assertThat(userValidator.isValid(user), is(equalTo(false)));
    }

    @Test
    public void withoutLastName() {
        User user = new User(1L, "Ben", null, "2345", 12);
        assertThat(userValidator.isValid(user), is(equalTo(false)));
    }

    @Test
    public void emptyLastName() {
        User user = new User(1L, "Ben", "", "2345678925", 21);

        assertThat(userValidator.isValid(user), is(equalTo(false)));
    }

    @Test
    public void withoutEgn() {
        User user = new User(1L, "Ben", "Big", null, 12);
        assertThat(userValidator.isValid(user), is(equalTo(false)));
    }

    @Test
    public void egnNotOnlyDigits() {
        User user = new User(1L, "Ben", "Big", "12k1254651", 12);
        assertThat(userValidator.isValid(user), is(equalTo(false)));
    }

    @Test
    public void egnLessDigits() {
        User user = new User(1L, "Men", "Gentleman", "123456789", 32);
        assertThat(userValidator.isValid(user), is(equalTo(false)));
    }

    @Test
    public void egnMoreDigits() {
        User user = new User(1L, "Men", "Gentleman", "12345678999", 32);
        assertThat(userValidator.isValid(user), is(equalTo(false)));
    }
}
