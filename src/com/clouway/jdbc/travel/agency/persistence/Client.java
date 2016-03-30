package com.clouway.jdbc.travel.agency.persistence;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class Client {
    public final String name;
    public final String egn;
    public final int age;
    public final String email;

    public Client(String name, String egn, int age, String email) {
        this.name = name;
        this.egn = egn;
        this.age = age;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        if (age != client.age) return false;
        if (name != null ? !name.equals(client.name) : client.name != null) return false;
        if (egn != null ? !egn.equals(client.egn) : client.egn != null) return false;
        return !(email != null ? !email.equals(client.email) : client.email != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (egn != null ? egn.hashCode() : 0);
        result = 31 * result + age;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

}
