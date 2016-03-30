package com.clouway.jdbc.customer.history.persistence;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class Customer {
    public final long id;
    public final String name;
    public final String lastName;
    public final String egn;

    public Customer(long id, String name, String lastName, String egn) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.egn = egn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (id != customer.id) return false;
        if (name != null ? !name.equals(customer.name) : customer.name != null) return false;
        if (lastName != null ? !lastName.equals(customer.lastName) : customer.lastName != null) return false;
        return !(egn != null ? !egn.equals(customer.egn) : customer.egn != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (egn != null ? egn.hashCode() : 0);
        return result;
    }
}
