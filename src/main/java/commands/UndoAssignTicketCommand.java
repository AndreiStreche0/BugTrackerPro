package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.TicketStatus;
import processing.AppStorage;
import tickets.Ticket;
import tickets.TicketHistory;
import users.Developer;
import users.User;

/**
 * undoes a previous assign ticket command
 */
public final class UndoAssignTicketCommand extends Command {
    private final int ticketId;
    private final ObjectMapper mapper = new ObjectMapper();
    private final AppStorage appStorage;

    public UndoAssignTicketCommand(final String username, final String timestamp,
                                   final int ticketId) {
        super("undoAssignTicket", username, timestamp);
        this.ticketId = ticketId;
        this.appStorage = AppStorage.getInstance();
    }
    /**
     * removes the previously assigned ticket the assigned tickets of that developer
     * @param correctPhase says if the command can be done in this phase
     * @return the output node
     */
    @Override
    public ObjectNode execute(final boolean correctPhase) {
        User user = appStorage.findUser(super.getUsername());
        Ticket ticket = appStorage.getTicket(ticketId);
        ObjectNode output = mapper.createObjectNode();

        if (ticket.getState() != TicketStatus.IN_PROGRESS) {
            output.put("command", "undoAssignTicket");
            output.put("username", super.getUsername());
            output.put("timestamp", super.getTimestamp());
            output.put("error", "Only IN_PROGRESS tickets can be unassigned.");
            return output;
        }
        TicketHistory history = TicketHistory.deAssign(super.getUsername(), super.getTimestamp());
        ticket.addHistory(history);
        ((Developer) user).removeAssignedTicket(ticket);
        ticket.setAssignedAt("");
        ticket.setAssignedTo(null);
        ticket.setState(TicketStatus.OPEN);

        return null;
    }
}
