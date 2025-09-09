package app.service;


import app.entity.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    Client create(String name);

    Optional<Client> get(Long id);

    List<Client> list();

    Client rename(Long id, String newName);

    void delete(Long id);
}
