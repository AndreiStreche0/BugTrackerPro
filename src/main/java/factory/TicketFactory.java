package factory;

import tickets.Ticket;

import java.util.Map;

public interface TicketFactory {
    Ticket createTicket(Map<String, Object> attributes, int id);
}
