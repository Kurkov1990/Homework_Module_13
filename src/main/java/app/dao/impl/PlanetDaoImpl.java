package app.dao.impl;

import app.config.HibernateUtils;
import app.dao.PlanetDao;
import app.entity.Planet;
import app.exception.DaoException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class PlanetDaoImpl implements PlanetDao {

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
    public Planet create(Planet entity) {
        return executeInTransaction(s -> {
            s.persist(entity);
            return entity;
        }, "Failed to create Planet");
    }

    @Override
    public Optional<Planet> findById(String id) {
        return executeRead(s -> Optional.ofNullable(s.find(Planet.class, id)),
                "Failed to find Planet by id=" + id);
    }

    @Override
    public List<Planet> findAll() {
        return executeRead(s -> s.createQuery("from Planet", Planet.class).list(),
                "Failed to load all Planets");
    }

    @Override
    public Planet update(Planet entity) {
        return executeInTransaction(s -> s.merge(entity),
                "Failed to update Planet id=" + entity.getId());
    }

    @Override
    public void deleteById(String id) {
        executeInTransaction(s -> {
            Planet found = s.find(Planet.class, id);
            if (found != null) s.remove(found);
            return null;
        }, "Failed to delete Planet id=" + id);
    }
}
