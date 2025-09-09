package app.dao;

import app.entity.Ticket;

import java.util.List;

public interface TicketDao extends GenericDao<Ticket, Long> {
    List<Ticket> findAllWithJoins();
}
