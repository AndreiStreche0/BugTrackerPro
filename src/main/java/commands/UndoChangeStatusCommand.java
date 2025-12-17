package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.TicketStatus;
import enums.UserRole;
import processing.AppStorage;
import tickets.Ticket;
import tickets.TicketHistory;
import users.User;

public final class UndoChangeStatusCommand extends Command {
    private final ObjectMapper mapper = new ObjectMapper();
    private final AppStorage appStorage;
    private final int ticketId;

    public UndoChangeStatusCommand(final String username, final String timestamp,
                                   final int ticketId) {
        super("undoChangeStatus", username, timestamp);
        this.appStorage = AppStorage.getInstance();
        this.ticketId = ticketId;
    }
    /**
     * creates error node for json output
     * @param message the string used for the node
     * @return the ObjectNode output
     */
    private ObjectNode createErrorNode(final String message) {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "undoChangeStatus");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        output.put("error", message);
        return output;
    }

    public ObjectNode execute(final boolean correctPhase) {
        User user = appStorage.findUser(super.getUsername());
        Ticket ticket = appStorage.getTicket(this.ticketId);
        if (user == null) {
            return createErrorNode("The user " + super.getUsername() + " does not exist.");
        }
        if (user.getRole() != UserRole.DEVELOPER) {
            return createErrorNode("The user does not have permission to execute this command: " +
                    "required role DEVELOPER; user role " + user.getRole() + ".");
        }
        if (ticket == null) {
            return null;
        }
        if (ticket.getAssignedTo() == null ||
                !ticket.getAssignedTo().getUsername().equals(user.getUsername())) {
            return createErrorNode("Ticket " + ticketId + " is not assigned to developer " +
                    user.getUsername() + ".");
        }
        TicketStatus newState = null;
        switch (ticket.getState()) {
            case TicketStatus.CLOSED:
                newState = TicketStatus.RESOLVED;
                break;
            case TicketStatus.RESOLVED:
                newState = TicketStatus.IN_PROGRESS;
                ticket.setSolvedAt("");
                break;
            default:
                return null;
        }

        TicketHistory history = TicketHistory.statusChanged(ticket.getState().toString(),
                newState.toString(), super.getUsername(), super.getTimestamp());
        ticket.setState(newState);
        ticket.addHistory(history);
        return null;
    }
}