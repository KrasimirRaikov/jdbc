package com.clouway.jdbc.travel.agency.persistence;

import java.util.List;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public interface ClientRepository {
    void register(Client client);

    Client getByEgn(String egn);

    void update(Client client);

    List<Client> getAll();

    List<Client> getWithNameBeggining(String nameBeginning);
}
