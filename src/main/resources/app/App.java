package app;

import app.config.HibernateUtils;
import app.dao.impl.ClientDaoImpl;
import app.dao.impl.PlanetDaoImpl;
import app.service.ClientService;
import app.service.PlanetService;
import app.service.impl.ClientServiceImpl;
import app.service.impl.PlanetServiceImpl;
import org.hibernate.Session;

public class App {
    public static void main(String[] args) {
        Session session = HibernateUtils.getInstance().getSessionFactory().openSession();

        ClientService clientService = new ClientServiceImpl(new ClientDaoImpl());
        PlanetService planetService = new PlanetServiceImpl(new PlanetDaoImpl());

        printClients("INITIAL CLIENTS", clientService);
        printPlanets("INITIAL PLANETS", planetService);

        System.out.println("\n=== CLIENT CRUD ===");
        var c = clientService.create("New Client");
        System.out.println("[CREATE] id=" + c.getId() + ", name=" + c.getName());
        printClients("AFTER CREATE", clientService);

        clientService.rename(c.getId(), "Updated Client");
        System.out.println("[UPDATE] id=" + c.getId() + " renamed to: " +
                clientService.get(c.getId()).map(x -> x.getName()).orElse("?"));
        printClients("AFTER UPDATE", clientService);

        clientService.delete(c.getId());
        System.out.println("[DELETE] id=" + c.getId());
        printClients("AFTER DELETE", clientService);

        System.out.println("\n=== PLANET CRUD ===");
        planetService.create("PLUTO", "Pluto");
        System.out.println("[CREATE] PLUTO -> Pluto");
        printPlanets("AFTER CREATE", planetService);

        planetService.rename("PLUTO", "Pluto (New)");
        System.out.println("[UPDATE] PLUTO renamed to: " +
                planetService.get("PLUTO").map(x -> x.getName()).orElse("?"));
        printPlanets("AFTER UPDATE", planetService);

        planetService.delete("PLUTO");
        System.out.println("[DELETE] PLUTO");
        printPlanets("AFTER DELETE", planetService);

        session.close();
    }

    private static void printClients(String title, ClientService clientService) {
        System.out.println("\n--- " + title + " ---");
        clientService.list().forEach(c ->
                System.out.printf(" id=%d | name=%s%n", c.getId(), c.getName())
        );
        if (clientService.list().isEmpty()) {
            System.out.println(" <no clients>");
        }
    }

    private static void printPlanets(String title, PlanetService planetService) {
        System.out.println("\n--- " + title + " ---");
        planetService.list().forEach(p ->
                System.out.printf(" id=%s | name=%s%n", p.getId(), p.getName())
        );
        if (planetService.list().isEmpty()) {
            System.out.println(" <no planets>");
        }
    }
}
