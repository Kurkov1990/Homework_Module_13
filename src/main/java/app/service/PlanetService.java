package app.service;

import app.entity.Planet;

import java.util.List;
import java.util.Optional;

public interface PlanetService {
    Planet create(String id, String name);

    Optional<Planet> get(String id);

    List<Planet> list();

    Planet rename(String id, String newName);

    void delete(String id);
}
