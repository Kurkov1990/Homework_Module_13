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
        Long cid = requireClientId(clientId);
        String fromId = requirePlanetId(fromPlanetId);
        String toId = requirePlanetId(toPlanetId);

        Client client = clientDao.findById(cid)
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + cid));
        Planet from = planetDao.findById(fromId)
                .orElseThrow(() -> new IllegalArgumentException("From planet not found: " + fromId));
        Planet to = planetDao.findById(toId)
                .orElseThrow(() -> new IllegalArgumentException("To planet not found: " + toId));

        if (from.getId().equals(to.getId())) {
            throw new IllegalArgumentException("From and To planets must differ");
        }

        Ticket t = new Ticket();
        t.setClient(client);
        t.setFrom(from);
        t.setTo(to);

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

        String fromId = requirePlanetId(newFromPlanetId);
        String toId = requirePlanetId(newToPlanetId);

        Planet newFrom = planetDao.findById(fromId)
                .orElseThrow(() -> new IllegalArgumentException("From planet not found: " + fromId));
        Planet newTo = planetDao.findById(toId)
                .orElseThrow(() -> new IllegalArgumentException("To planet not found: " + toId));

        if (newFrom.getId().equals(newTo.getId())) {
            throw new IllegalArgumentException("From and To planets must differ");
        }

        t.setFrom(newFrom);
        t.setTo(newTo);
        return ticketDao.update(t);
    }

    @Override
    public void delete(Long id) {
        ticketDao.deleteById(id);
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
}
