package app.dao.impl;

import app.config.HibernateUtils;
import app.dao.PlanetDao;
import app.entity.Planet;
import app.exception.DaoException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class PlanetDaoImpl implements PlanetDao {

    @Override
    public Planet create(Planet entity) {
        Transaction transaction = null;
        try (Session s = HibernateUtils.getInstance().getSessionFactory().openSession()) {
            transaction = s.beginTransaction();
            s.persist(entity);
            transaction.commit();
            return entity;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new DaoException("Failed to create Planet", e);
        }
    }

    @Override
    public Optional<Planet> findById(String id) {
        try (Session s = HibernateUtils.getInstance().getSessionFactory().openSession()) {
            return Optional.ofNullable(s.get(Planet.class, id));
        } catch (Exception e) {
            throw new DaoException("Failed to find Planet by id=" + id, e);
        }
    }

    @Override
    public List<Planet> findAll() {
        try (Session s = HibernateUtils.getInstance().getSessionFactory().openSession()) {
            return s.createQuery("from Planet", Planet.class).list();
        } catch (Exception e) {
            throw new DaoException("Failed to load all Planets", e);
        }
    }

    @Override
    public Planet update(Planet entity) {
        Transaction transaction = null;
        try (Session s = HibernateUtils.getInstance().getSessionFactory().openSession()) {
            transaction = s.beginTransaction();
            Planet merged = s.merge(entity);
            transaction.commit();
            return merged;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new DaoException("Failed to update Planet id=" + entity.getId(), e);
        }
    }

    @Override
    public void deleteById(String id) {
        Transaction transaction = null;
        try (Session s = HibernateUtils.getInstance().getSessionFactory().openSession()) {
            transaction = s.beginTransaction();
            Planet found = s.get(Planet.class, id);
            if (found != null) s.remove(found);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new DaoException("Failed to delete Planet id=" + id, e);
        }
    }
}
