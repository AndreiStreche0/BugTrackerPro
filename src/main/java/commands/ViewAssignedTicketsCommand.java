package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.UserRole;
import processing.AppStorage;
import tickets.Ticket;
import users.Developer;
import users.User;

import java.util.ArrayList;
import java.util.List;

/**
 * shows the assigned tickets of a developer
 */
public final class ViewAssignedTicketsCommand extends Command {
    private final ObjectMapper mapper = new ObjectMapper();
    private final AppStorage appStorage;
    public ViewAssignedTicketsCommand(final String username, final String timestamp) {
        super("viewAssignedTickets", username, timestamp);
        this.appStorage = AppStorage.getInstance();
    }
    /**
     * creates error node for json output
     * @param message the string used for the node
     * @return the ObjectNode output
     */
    private ObjectNode createErrorNode(final String message) {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "viewAssignedTickets");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        output.put("error", message);
        return output;
    }
    /**
     * sorts and shows all the assigned tickets of a developer
     * @param correctPhase says if the command can be done in this phase
     * @return the output node
     */
    @Override
    public ObjectNode execute(final boolean correctPhase) {
        ObjectNode output = mapper.createObjectNode();
        User user = appStorage.findUser(super.getUsername());
        if (user == null) {
            return createErrorNode("The user " + super.getUsername() + " does not exist.");
        }
        if (user.getRole() != UserRole.DEVELOPER) {
            return createErrorNode("The user does not have permission to execute this command:"
                    + " required role DEVELOPER; user role " + user.getRole() + ".");
        }
        Developer developer = (Developer) user;
        List<Ticket> currTickets = new ArrayList<>(developer.getAssignedTickets());

        currTickets.sort((ticket1, ticket2) -> {
            int priority1 = ticket1.getBusinessPriority().ordinal();
            int priority2 = ticket2.getBusinessPriority().ordinal();
            if (priority1 != priority2) {
                return priority2 - priority1;
            }
            return ticket1.getId() - ticket2.getId();
        });

        output.put("command", "viewAssignedTickets");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());

        ArrayNode outputArray = mapper.createArrayNode();

        for (Ticket ticket : currTickets) {
            ObjectNode ticketNode = mapper.createObjectNode();
            ticketNode.put("id", ticket.getId());
            ticketNode.put("type", ticket.getType().toString());
            ticketNode.put("title", ticket.getTitle());
            ticketNode.put("businessPriority", ticket.getBusinessPriority().toString());
            ticketNode.put("status", ticket.getState().toString());
            ticketNode.put("createdAt", ticket.getCreatedAt());
            ticketNode.put("assignedAt", ticket.getAssignedAt() == null ? ""
                    : ticket.getAssignedAt());
            ticketNode.put("reportedBy", ticket.getReportedBy());
            if (ticket.getComments() == null || ticket.getComments().isEmpty()) {
                ticketNode.putArray("comments");
            } else {
                ticketNode.set("comments", mapper.valueToTree(ticket.getComments()));
            }
            outputArray.add(ticketNode);
        }
        output.set("assignedTickets", outputArray);
        return output;
    }
}
