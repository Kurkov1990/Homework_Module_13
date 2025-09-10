package app.service.impl;

import app.dao.ClientDao;
import app.dao.PlanetDao;
import app.dao.TicketDao;
import app.entity.Client;
import app.entity.Planet;
import app.entity.Ticket;
import app.service.TicketService;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TicketServiceImpl implements TicketService {

    private final TicketDao ticketDao;
    private final ClientDao clientDao;
    private final PlanetDao planetDao;

    public TicketServiceImpl(TicketDao ticketDao, ClientDao clientDao, PlanetDao planetDao) {
        this.ticketDao = ticketDao;
        this.clientDao = clientDao;
        this.planetDao = planetDao;
    }

    @Override
    public Ticket create(Long clientId, String fromPlanetId, String toPlanetId) {
        Client client = requireClient(clientId);
        Ticket t = new Ticket();
        t.setClient(client);
        resolveAndApplyRoute(t, fromPlanetId, toPlanetId);
        return ticketDao.create(t);
    }

    @Override
    public Optional<Ticket> get(Long id) {
        return ticketDao.findById(id);
    }

    @Override
    public List<Ticket> list() {
        return ticketDao.findAll();
    }

    @Override
    public List<Ticket> listWithJoins() {
        return ticketDao.findAllWithJoins();
    }

    @Override
    public Ticket changeRoute(Long ticketId, String newFromPlanetId, String newToPlanetId) {
        Ticket t = ticketDao.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found: " + ticketId));
        resolveAndApplyRoute(t, newFromPlanetId, newToPlanetId);
        return ticketDao.update(t);
    }

    @Override
    public void delete(Long id) {
        ticketDao.deleteById(id);
    }

    private void resolveAndApplyRoute(Ticket t, String fromPlanetId, String toPlanetId) {
        Planet from = requirePlanet(fromPlanetId);
        Planet to = requirePlanet(toPlanetId);
        if (from.getId().equals(to.getId())) {
            throw new IllegalArgumentException("From and To planets must differ");
        }
        t.setFrom(from);
        t.setTo(to);
    }

    private Client requireClient(Long id) {
        Long cid = requireClientId(id);
        return requireEntity(clientDao::findById, cid, "Client not found: " + cid);
    }

    private Planet requirePlanet(String id) {
        String pid = requirePlanetId(id);
        return requireEntity(planetDao::findById, pid, "Planet not found: " + pid);
    }

    private Long requireClientId(Long id) {
        if (id == null) throw new IllegalArgumentException("Client id must not be null");
        return id;
    }

    private String requirePlanetId(String id) {
        if (id == null) throw new IllegalArgumentException("Planet id must not be null");
        String trimmed = id.trim();
        if (trimmed.isEmpty()) throw new IllegalArgumentException("Planet id must not be empty");
        return trimmed;
    }

    private static <ID, E> E requireEntity(Function<ID, Optional<E>> finder, ID id, String notFoundMessage) {
        return finder.apply(id).orElseThrow(() -> new IllegalArgumentException(notFoundMessage));
    }
}
