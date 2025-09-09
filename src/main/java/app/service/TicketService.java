package app.service;

import app.entity.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    Ticket create(Long clientId, String fromPlanetId, String toPlanetId);
    Optional<Ticket> get(Long id);
    List<Ticket> list();
    List<Ticket> listWithJoins();
    Ticket changeRoute(Long ticketId, String newFromPlanetId, String newToPlanetId);
    void delete(Long id);
}
