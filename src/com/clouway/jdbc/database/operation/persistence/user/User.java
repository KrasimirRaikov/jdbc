package com.clouway.jdbc.database.operation.persistence.user;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class User {
    public final Long id;
    public final String name;
    public final String lastName;
    public final String egn;
    public final int age;

    public User(Long id, String name, String lastName, String egn, int age) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.egn = egn;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (age != user.age) return false;
        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) return false;
        return !(egn != null ? !egn.equals(user.egn) : user.egn != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (egn != null ? egn.hashCode() : 0);
        result = 31 * result + age;
        return result;
    }
}
