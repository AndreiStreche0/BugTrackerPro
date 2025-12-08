package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.UserRole;
import processing.AppStorage;
import tickets.Ticket;
import users.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ViewTicketsCommand extends Command{
    private final ObjectMapper mapper = new ObjectMapper();
    private final AppStorage appStorage;

    public ViewTicketsCommand(String username, String timestamp) {
        super("viewTickets", username, timestamp);
        appStorage = AppStorage.getInstance();
    }
    private ObjectNode createErrorNode(String message) {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "viewTickets");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        output.put("error", message);
        return output;
    }
    @Override
    public ObjectNode execute(boolean correctPhase) {
        ObjectNode output = mapper.createObjectNode();
        if (super.getUsername() == null || appStorage.findUser(super.getUsername()) == null) {
            return createErrorNode("The user " + super.getUsername() + " doesn't exist.");
        }
        User user = appStorage.findUser(super.getUsername());
        if (!correctPhase) {
            return createErrorNode("Tickets can only be viewed during testing and developing" +
                    " phases.");
        }

        List<Ticket> viewableTickets = new ArrayList<>();
        if (user.getRole() == UserRole.MANAGER) {
            viewableTickets.addAll(appStorage.getTickets());
        } else if (user.getRole() == UserRole.REPORTER) {
            for (Ticket ticket : appStorage.getTickets()) {
                if (ticket.getReportedBy().equals(user.getUsername())) {
                    viewableTickets.add(ticket);
                }
            }
        }
        viewableTickets.sort((t1, t2) -> {
            int dateComparison = t1.getCreatedAt().compareTo(t2.getCreatedAt());
            if (dateComparison != 0) {
                return dateComparison;
            }
            return Integer.compare(t1.getId(), t2.getId());
        });
        output.put("command", "viewTickets");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        ArrayNode ticketsOutput = mapper.createArrayNode();
        for (Ticket ticket : viewableTickets) {
            ObjectNode ticketNode = mapper.createObjectNode();

            ticketNode.put("id", ticket.getId());
            ticketNode.put("type", ticket.getType().toString());
            ticketNode.put("title", ticket.getTitle());
            ticketNode.put("businessPriority", ticket.getBusinessPriority().toString());
            ticketNode.put("status", ticket.getState().toString());
            ticketNode.put("createdAt", ticket.getCreatedAt());

            ticketNode.put("assignedAt", ticket.getAssignedAt() ==
                    null ? "" : ticket.getAssignedAt());
            ticketNode.put("solvedAt", ticket.getSolvedAt() == null ? "" : ticket.getSolvedAt());
            ticketNode.put("assignedTo", ticket.getAssignedTo() ==
                    null ? "" : ticket.getAssignedTo().getUsername());

            String reporter = ticket.getReportedBy();
            ticketNode.put("reportedBy", reporter == null ? "" : reporter);
            if (ticket.getComments() == null) {
                ticketNode.putArray("comments");
            } else {
                //TODO
//                ticketNode.set("comments", mapper.valueToTree(ticket.getComments()));
            }
            ticketsOutput.add(ticketNode);
        }
        output.set("tickets", ticketsOutput);

        return output;
    }
}
