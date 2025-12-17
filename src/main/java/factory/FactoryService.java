package factory;

import tickets.Ticket;
import java.util.HashMap;
import java.util.Map;

/**
 * class to create the factories and manage the factory design pattern
 */
public class FactoryService {

    private static final FactoryService INSTANCE = new FactoryService();
    private int id;
    private final Map<String, TicketFactory> ticketFactories;

    private FactoryService() {
        ticketFactories = new HashMap<>();
        id = 0;
        ticketFactories.put("BUG", new BugFactory());
        ticketFactories.put("FEATURE_REQUEST", new FeatureRequestFactory());
        ticketFactories.put("UI_FEEDBACK", new UIFeedbackFactory());
    }

    public static FactoryService getInstance() {
        return INSTANCE;
    }
    public void reset() {
        this.id = 0;
    }

    /**
     * creates a new ticket
     * @param type the ticket type
     * @param attributes the ticket attributes
     * @param timestamp ticket creation time
     * @return the new ticket
     */
    public Ticket createTicket(final String type, final Map<String, Object> attributes,
                               final String timestamp) {
        TicketFactory factory = ticketFactories.get(type);

        if (factory == null) {
            throw new IllegalArgumentException("Wrong ticket type: " + type);
        }
        return factory.createTicket(attributes, this.id++, timestamp);
    }
}
