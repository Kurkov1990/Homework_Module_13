package app.service.impl;

import app.dao.PlanetDao;
import app.entity.Planet;
import app.service.PlanetService;

import java.util.List;
import java.util.Optional;

public class PlanetServiceImpl implements PlanetService {

    private final PlanetDao planetDao;

    public PlanetServiceImpl(PlanetDao planetDao) {
        this.planetDao = planetDao;
    }

    @Override
    public Planet create(String id, String name) {
        String validId = normalizeId(id, 50);
        String validName = normalizeName(name, 500);

        if (planetDao.findById(validId).isPresent()) {
            throw new IllegalArgumentException("Planet already exists: " + validId);
        }

        Planet p = new Planet();
        p.setId(validId);
        p.setName(validName);
        return planetDao.create(p);
    }

    @Override
    public Optional<Planet> get(String id) {
        return planetDao.findById(id);
    }

    @Override
    public List<Planet> list() {
        return planetDao.findAll();
    }

    @Override
    public Planet rename(String id, String newName) {
        String validName = normalizeName(newName, 500);
        Planet p = planetDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Planet not found: " + id));
        p.setName(validName);
        return planetDao.update(p);
    }

    @Override
    public void delete(String id) {
        planetDao.deleteById(id);
    }

    private String normalizeId(String id, int maxLen) {
        if (id == null) {
            throw new IllegalArgumentException("Planet id must not be null");
        }
        String trimmed = id.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Planet id must not be empty");
        }
        if (trimmed.length() > maxLen) {
            throw new IllegalArgumentException("Planet id length must be <= " + maxLen);
        }
        return trimmed;
    }

    private String normalizeName(String name, int maxLen) {
        if (name == null) {
            throw new IllegalArgumentException("Planet name must not be null");
        }
        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Planet name must not be empty");
        }
        if (trimmed.length() > maxLen) {
            throw new IllegalArgumentException("Planet name length must be <= " + maxLen);
        }
        return trimmed;
    }
}
