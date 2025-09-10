package app.dao.impl;

import app.config.HibernateUtils;
import app.dao.ClientDao;
import app.entity.Client;
import app.exception.DaoException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ClientDaoImpl implements ClientDao {

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
    public Client create(Client entity) {
        return executeInTransaction(s -> {
            s.persist(entity);
            return entity;
        }, "Failed to create Client");
    }

    @Override
    public Optional<Client> findById(Long id) {
        return executeRead(s -> Optional.ofNullable(s.find(Client.class, id)),
                "Failed to find Client by id=" + id);
    }

    @Override
    public List<Client> findAll() {
        return executeRead(s -> s.createQuery("from Client", Client.class).list(),
                "Failed to load all Clients");
    }

    @Override
    public Client update(Client entity) {
        return executeInTransaction(s -> s.merge(entity),
                "Failed to update Client id=" + entity.getId());
    }

    @Override
    public void deleteById(Long id) {
        executeInTransaction(s -> {
            Client found = s.find(Client.class, id);
            if (found != null) s.remove(found);
            return null;
        }, "Failed to delete Client id=" + id);
    }
}
