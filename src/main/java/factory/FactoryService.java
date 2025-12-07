package factory;

import tickets.Ticket;
import java.util.HashMap;
import java.util.Map;

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

    public Ticket createTicket(String type, Map<String, Object> attributes) {
        TicketFactory factory = ticketFactories.get(type);

        if (factory == null) {
            throw new IllegalArgumentException("Wrong ticket type: " + type);
        }
        return factory.createTicket(attributes, this.id++);
    }
}