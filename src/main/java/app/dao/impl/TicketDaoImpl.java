package app.dao.impl;

import app.config.HibernateUtils;
import app.dao.TicketDao;
import app.entity.Ticket;
import app.exception.DaoException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class TicketDaoImpl implements TicketDao {

    @Override
    public Ticket create(Ticket entity) {
        Transaction transaction = null;
        try (Session s = HibernateUtils.getInstance().getSessionFactory().openSession()) {
            transaction = s.beginTransaction();
            s.persist(entity);
            transaction.commit();
            return entity;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new DaoException("Failed to create Ticket", e);
        }
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        try (Session s = HibernateUtils.getInstance().getSessionFactory().openSession()) {
            return Optional.ofNullable(s.get(Ticket.class, id));
        } catch (Exception e) {
            throw new DaoException("Failed to find Ticket by id=" + id, e);
        }
    }

    @Override
    public List<Ticket> findAll() {
        try (Session s = HibernateUtils.getInstance().getSessionFactory().openSession()) {
            return s.createQuery("from Ticket", Ticket.class).list();
        } catch (Exception e) {
            throw new DaoException("Failed to load all Tickets", e);
        }
    }

    @Override
    public List<Ticket> findAllWithJoins() {
        try (Session s = HibernateUtils.getInstance().getSessionFactory().openSession()) {
            return s.createQuery(
                    "select distinct t from Ticket t " +
                            "join fetch t.client " +
                            "join fetch t.from " +
                            "join fetch t.to", Ticket.class
            ).list();
        } catch (Exception e) {
            throw new DaoException("Failed to load Tickets with joins", e);
        }
    }

    @Override
    public Ticket update(Ticket entity) {
        Transaction transaction = null;
        try (Session s = HibernateUtils.getInstance().getSessionFactory().openSession()) {
            transaction = s.beginTransaction();
            Ticket merged = s.merge(entity);
            transaction.commit();
            return merged;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new DaoException("Failed to update Ticket id=" + entity.getId(), e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Transaction transaction = null;
        try (Session s = HibernateUtils.getInstance().getSessionFactory().openSession()) {
            transaction = s.beginTransaction();
            Ticket found = s.get(Ticket.class, id);
            if (found != null) s.remove(found);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new DaoException("Failed to delete Ticket id=" + id, e);
        }
    }
}
