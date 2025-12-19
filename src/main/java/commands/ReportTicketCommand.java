package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.BusinessPriority;
import enums.TicketType;
import enums.UserRole;
import factories.FactoryService;
import processing.AppStorage;
import tickets.Ticket;
import users.User;

import java.util.Map;

/**
 * command used for reporting a ticket
 */
public final class ReportTicketCommand extends Command {
    private final Map<String, Object> ticketParameters;
    private final FactoryService ticketService;
    private final ObjectMapper mapper;
    private final AppStorage appStorage;
    public ReportTicketCommand(final String username, final String timestamp,
                               final Map<String, Object> params) {
        super("reportTicket", username, timestamp);
        this.ticketParameters = params;
        this.ticketService = FactoryService.getInstance();
        this.mapper = new ObjectMapper();
        this.appStorage = AppStorage.getInstance();
    }
    /**
     * creates error node for json output
     * @param message the string used for the node
     * @return the ObjectNode output
     */
    private ObjectNode createErrorNode(final String message) {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "reportTicket");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        output.put("error", message);
        return output;
    }
    /**
     * reports a ticket
     * @param correctPhase says if the command can be done in this phase
     * @return the output node
     */
    public ObjectNode execute(final boolean correctPhase) {
        ObjectNode output = mapper.createObjectNode();
        if (super.getUsername() == null || appStorage.findUser(super.getUsername()) == null) {
            return createErrorNode("The user " + super.getUsername() + " does not exist.");
        }
        User currentUser = appStorage.findUser(super.getUsername());
        if (currentUser.getRole() != UserRole.REPORTER) {
            return createErrorNode("The user does not have permission to execute this command:"
                    + " required role Reporter; user role " + currentUser.getRole());
        }
        if (!correctPhase) {
            return createErrorNode("Tickets can only be reported during testing phases.");
        }
        String reporterName = (String) ticketParameters.get("reportedBy");

        if (reporterName != null && reporterName.isEmpty()
                && !((String) ticketParameters.get("type")).equals("BUG")) {
            return createErrorNode("Anonymous reports are only allowed for tickets of type"
                    + " BUG.");
        }
        Ticket newTicket = ticketService.createTicket((String) ticketParameters.get("type"),
                ticketParameters, appStorage.getTickets().size(), super.getTimestamp());
        if (reporterName != null && reporterName.isEmpty()
                && newTicket.getType() == TicketType.BUG) {
            newTicket.setBusinessPriority(BusinessPriority.LOW);
        }
        appStorage.addTicket(newTicket);
        return null;
    }
}
