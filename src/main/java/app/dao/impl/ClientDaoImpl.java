package app.dao.impl;

import app.config.HibernateUtils;
import app.dao.ClientDao;
import app.entity.Client;
import app.exception.DaoException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class ClientDaoImpl implements ClientDao {

    @Override
    public Client create(Client entity) {
        Transaction transaction = null;
        try (Session s = HibernateUtils.getInstance().getSessionFactory().openSession()) {
            transaction = s.beginTransaction();
            s.persist(entity);
            transaction.commit();
            return entity;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new DaoException("Failed to create Client", e);
        }
    }

    @Override
    public Optional<Client> findById(Long id) {
        try (Session s = HibernateUtils.getInstance().getSessionFactory().openSession()) {
            return Optional.ofNullable(s.get(Client.class, id));
        } catch (Exception e) {
            throw new DaoException("Failed to find Client by id=" + id, e);
        }
    }

    @Override
    public List<Client> findAll() {
        try (Session s = HibernateUtils.getInstance().getSessionFactory().openSession()) {
            return s.createQuery("from Client", Client.class).list();
        } catch (Exception e) {
            throw new DaoException("Failed to load all Clients", e);
        }
    }

    @Override
    public Client update(Client entity) {
        Transaction transaction = null;
        try (Session s = HibernateUtils.getInstance().getSessionFactory().openSession()) {
            transaction = s.beginTransaction();
            Client merged = s.merge(entity);
            transaction.commit();
            return merged;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new DaoException("Failed to update Client id=" + entity.getId(), e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Transaction transaction = null;
        try (Session s = HibernateUtils.getInstance().getSessionFactory().openSession()) {
            transaction = s.beginTransaction();
            Client found = s.get(Client.class, id);
            if (found != null) s.remove(found);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new DaoException("Failed to delete Client id=" + id, e);
        }
    }
}
