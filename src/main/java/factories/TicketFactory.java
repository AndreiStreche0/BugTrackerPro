package factories;

import tickets.Ticket;

import java.util.Map;

/**
 * interface that contains methods needed for the ticket factory
 */
public interface TicketFactory {
    /**
     * @param attributes ticket attributes
     * @param id id of the ticket
     * @param timestamp ticket creation time
     * @return the new ticket
     */
    Ticket createTicket(Map<String, Object> attributes, int id, String timestamp);
}
