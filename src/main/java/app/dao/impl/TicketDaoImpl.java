package app.dao.impl;

import app.config.HibernateUtils;
import app.dao.TicketDao;
import app.entity.Ticket;
import app.exception.DaoException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TicketDaoImpl implements TicketDao {

    private <T> T executeRead(Function<Session, T> work, String errorMessage) {
        try (Session s = HibernateUtils.getInstance().getSessionFactory().openSession()) {
            return work.apply(s);
        } catch (Exception e) {
            throw new DaoException(errorMessage, e);
        }
    }

    private <T> T executeInTransaction(Function<Session, T> work, String errorMessage) {
        Transaction tx = null;
        try (Session s = HibernateUtils.getInstance().getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            T result = work.apply(s);
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new DaoException(errorMessage, e);
        }
    }

    @Override
    public Ticket create(Ticket entity) {
        return executeInTransaction(s -> {
            s.persist(entity);
            return entity;
        }, "Failed to create Ticket");
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        return executeRead(s -> Optional.ofNullable(s.find(Ticket.class, id)),
                "Failed to find Ticket by id=" + id);
    }

    @Override
    public List<Ticket> findAll() {
        return executeRead(s -> s.createQuery("from Ticket", Ticket.class).list(),
                "Failed to load all Tickets");
    }

    @Override
    public List<Ticket> findAllWithJoins() {
        return executeRead(s -> s.createQuery(
                "select distinct t from Ticket t " +
                        "join fetch t.client " +
                        "join fetch t.from " +
                        "join fetch t.to", Ticket.class
        ).list(), "Failed to load Tickets with joins");
    }

    @Override
    public Ticket update(Ticket entity) {
        return executeInTransaction(s -> s.merge(entity),
                "Failed to update Ticket id=" + entity.getId());
    }

    @Override
    public void deleteById(Long id) {
        executeInTransaction(s -> {
            Ticket found = s.find(Ticket.class, id);
            if (found != null) s.remove(found);
            return null;
        }, "Failed to delete Ticket id=" + id);
    }
}
