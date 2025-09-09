package app;

import app.config.HibernateUtils;
import app.dao.impl.ClientDaoImpl;
import app.dao.impl.PlanetDaoImpl;
import app.dao.impl.TicketDaoImpl;
import app.service.ClientService;
import app.service.PlanetService;
import app.service.TicketService;
import app.service.impl.ClientServiceImpl;
import app.service.impl.PlanetServiceImpl;
import app.service.impl.TicketServiceImpl;
import org.hibernate.Session;

public class App {
    public static void main(String[] args) {
        Session session = HibernateUtils.getInstance().getSessionFactory().openSession();

        ClientService clientService = new ClientServiceImpl(new ClientDaoImpl());
        PlanetService planetService = new PlanetServiceImpl(new PlanetDaoImpl());
        TicketService ticketService = new TicketServiceImpl(
                new TicketDaoImpl(),
                new ClientDaoImpl(),
                new PlanetDaoImpl()
        );

        printClients("INITIAL CLIENTS", clientService);
        printPlanets("INITIAL PLANETS", planetService);
        printTickets("INITIAL TICKETS", ticketService);

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

        System.out.println("\n=== TICKET CRUD ===");
        var alice = clientService.create("Alice App");
        System.out.println("[CLIENT] Alice id=" + alice.getId());

        var t1 = ticketService.create(alice.getId(), "EARTH", "MARS");
        System.out.println("[TICKET CREATE] id=" + t1.getId());
        printTickets("AFTER TICKET CREATE", ticketService);

        t1 = ticketService.changeRoute(t1.getId(), "MARS", "VENUS");
        System.out.println("[TICKET UPDATE ROUTE] id=" + t1.getId());
        printTickets("AFTER TICKET ROUTE UPDATE", ticketService);

        try {
            ticketService.create(999_999L, "EARTH", "MARS");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        try {
            ticketService.create(alice.getId(), "EARTH", "NOPE");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        try {
            ticketService.create(null, "EARTH", "MARS");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        try {
            ticketService.create(alice.getId(), null, "MARS");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        try {
            ticketService.create(alice.getId(), "MARS", null);
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        try {
            ticketService.create(alice.getId(), "MARS", "MARS");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }

        ticketService.delete(t1.getId());
        System.out.println("[TICKET DELETE] id=" + t1.getId());
        printTickets("AFTER TICKET DELETE", ticketService);

        clientService.delete(alice.getId());
        printClients("AFTER CLEANUP (ALICE REMOVED)", clientService);

        session.close();
    }

    private static void printClients(String title, ClientService clientService) {
        System.out.println("\n--- " + title + " ---");
        var list = clientService.list();
        list.forEach(c ->
                System.out.printf(" id=%d | name=%s%n", c.getId(), c.getName())
        );
        if (list.isEmpty()) {
            System.out.println(" <no clients>");
        }
    }

    private static void printPlanets(String title, PlanetService planetService) {
        System.out.println("\n--- " + title + " ---");
        var list = planetService.list();
        list.forEach(p ->
                System.out.printf(" id=%s | name=%s%n", p.getId(), p.getName())
        );
        if (list.isEmpty()) {
            System.out.println(" <no planets>");
        }
    }

    private static void printTickets(String title, TicketService ticketService) {
        System.out.println("\n--- " + title + " ---");
        var list = ticketService.listWithJoins();
        list.forEach(t ->
                System.out.printf(
                        " id=%d | clientId=%d | from=%s | to=%s%n",
                        t.getId(),
                        t.getClient().getId(),
                        t.getFrom().getId(),
                        t.getTo().getId()
                )
        );
        if (list.isEmpty()) {
            System.out.println(" <no tickets>");
        }
    }
}
