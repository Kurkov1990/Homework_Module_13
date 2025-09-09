package app.config;

import app.entity.Client;
import app.entity.Planet;
import app.entity.Ticket;
import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.InputStream;
import java.util.Properties;

public class HibernateUtils {

    private static final HibernateUtils INSTANCE = new HibernateUtils();
    private SessionFactory sessionFactory;

    private HibernateUtils() {
        Properties props = loadHibernateProps();

        String url = props.getProperty("hibernate.connection.url");
        String user = props.getProperty("hibernate.connection.username");
        String pass = props.getProperty("hibernate.connection.password");

        Flyway.configure()
                .dataSource(url, user, pass)
                .locations("classpath:db/migration")
                .load()
                .migrate();

        this.sessionFactory = new Configuration()
                .addAnnotatedClass(Client.class)
                .addAnnotatedClass(Planet.class)
                .addAnnotatedClass(Ticket.class)
                .buildSessionFactory();
    }

    private Properties loadHibernateProps() {
        Properties p = new Properties();
        try (InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("hibernate.properties")) {
            if (in != null) p.load(in);
        } catch (Exception ignored) {
        }
        return p;
    }

    public static HibernateUtils getInstance() {
        return INSTANCE;
    }

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    public void closeSessionFactory() {
        if (this.sessionFactory != null) {
            this.sessionFactory.close();
        }
    }
}
