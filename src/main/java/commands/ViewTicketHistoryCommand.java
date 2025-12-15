package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.UserRole;
import milestones.Milestone;
import processing.AppStorage;
import tickets.Ticket;
import tickets.TicketHistory;
import users.User;

import java.util.ArrayList;
import java.util.List;

public class ViewTicketHistoryCommand extends Command{
    private final ObjectMapper mapper = new ObjectMapper();
    private final AppStorage appStorage;

    public ViewTicketHistoryCommand(String username, String timestamp) {
        super("viewTicketHistory", username, timestamp);
        this.appStorage = AppStorage.getInstance();
    }
    private ObjectNode createErrorNode(String message) {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "viewTicketHistory");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        output.put("error", message);
        return output;
    }
    @Override
    public ObjectNode execute(boolean correctPhase) {
        User user = appStorage.findUser(super.getUsername());
        if (user == null) {
            return createErrorNode("The user " + super.getUsername() + " does not exist.");
        }

        List<Ticket> viewableTickets = new ArrayList<>();
        if (user.getRole() == UserRole.DEVELOPER) {
            for (Ticket ticket : appStorage.getTickets()) {
                boolean userTicket = false;
                for (TicketHistory history : ticket.getHistory()) {
                    if (history.getBy() != null && history.getBy().equals(user.getUsername())) {
                        userTicket = true;
                        break;
                    }
                }

                if (userTicket || ticket.getAssignedTo() != null &&
                        ticket.getAssignedTo().getUsername().equals(user.getUsername())) {
                    viewableTickets.add(ticket);
                }
            }
        } else if(user.getRole() == UserRole.MANAGER) {
            for (Milestone milestone : appStorage.getMilestones()) {
                if (milestone.getCreatedBy().equals(user.getUsername())) {
                    for (int id : milestone.getTicketIds()) {
                        viewableTickets.add(appStorage.getTicket(id));
                    }
                }
            }
        } else {
            return createErrorNode("The user does not have permission to execute this command:" +
                    " required role DEVELOPER; user role " + user.getRole() + ".");
        }
        viewableTickets.sort((t1, t2) -> {
            int dateComp = t1.getCreatedAt().compareTo(t2.getCreatedAt());
            if (dateComp != 0) return dateComp;
            return t1.getId() - t2.getId();
        });
        ArrayNode outputArray = mapper.createArrayNode();
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "viewTicketHistory");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        for (Ticket ticket : viewableTickets) {

            ObjectNode ticketNode = mapper.createObjectNode();
            ticketNode.put("id", ticket.getId());
            ticketNode.put("title", ticket.getTitle());
            ticketNode.put("status", ticket.getState().toString());

            ticketNode.set("actions", mapper.valueToTree(ticket.getHistory()));

            if (ticket.getComments() == null || ticket.getComments().isEmpty()) {
                ticketNode.putArray("comments");
            } else {
                ticketNode.set("comments", mapper.valueToTree(ticket.getComments()));
            }
            outputArray.add(ticketNode);
        }
        output.set("ticketHistory", outputArray);
        return output;
    }
}
