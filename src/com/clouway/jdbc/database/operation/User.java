package com.clouway.jdbc.database.operation;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class User {
    public final int id;
    public final String name;
    public final String surname;
    public final String egn;
    public final int age;

    public User(int id, String name, String surname, String egn, int age) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.egn = egn;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", egn='" + egn + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (age != user.age) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (surname != null ? !surname.equals(user.surname) : user.surname != null) return false;
        return !(egn != null ? !egn.equals(user.egn) : user.egn != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (egn != null ? egn.hashCode() : 0);
        result = 31 * result + age;
        return result;
    }
}