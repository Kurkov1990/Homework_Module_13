package app.service.impl;

import app.dao.ClientDao;
import app.entity.Client;
import app.service.ClientService;

import java.util.List;
import java.util.Optional;


public class ClientServiceImpl implements ClientService {

    private final ClientDao clientDao;

    public ClientServiceImpl(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @Override
    public Client create(String name) {
        String normalized = normalizeName(name, 200);
        Client c = new Client();
        c.setName(normalized);
        return clientDao.create(c);
    }

    @Override
    public Optional<Client> get(Long id) {
        return clientDao.findById(id);
    }

    @Override
    public List<Client> list() {
        return clientDao.findAll();
    }

    @Override
    public Client rename(Long id, String newName) {
        String normalized = normalizeName(newName, 200);
        Client c = clientDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + id));
        c.setName(normalized);
        return clientDao.update(c);
    }

    @Override
    public void delete(Long id) {
        clientDao.deleteById(id);
    }

    private String normalizeName(String name, int maxLen) {
        if (name == null) {
            throw new IllegalArgumentException("Name must not be null");
        }
        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Name must not be empty");
        }
        if (trimmed.length() > maxLen) {
            throw new IllegalArgumentException("Name length must be <= " + maxLen);
        }
        return trimmed;
    }
}
