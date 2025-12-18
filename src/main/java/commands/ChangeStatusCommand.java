package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.TicketStatus;
import enums.UserRole;
import milestones.Milestone;
import processing.AppStorage;
import tickets.Ticket;
import tickets.TicketHistory;
import users.Developer;
import users.User;

import java.time.LocalDate;

/**
 * command to change the app status
 */
public final class ChangeStatusCommand extends Command {
    private final ObjectMapper mapper = new ObjectMapper();
    private final AppStorage appStorage;
    private final int ticketId;

    public ChangeStatusCommand(final String username, final String timestamp, final int ticketId) {
        super("changeStatus", username, timestamp);
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
        output.put("command", "changeStatus");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        output.put("error", message);
        return output;
    }
    /**
     * allows developer to change the status of their ticket
     * @param correctPhase says if the command can be done in this phase
     * @return the output node
     */
    public ObjectNode execute(final boolean correctPhase) {
        User user = appStorage.findUser(super.getUsername());
        Ticket ticket = appStorage.getTicket(this.ticketId);
        if (user == null) {
            return createErrorNode("The user " + super.getUsername() + " does not exist.");
        }
        if (user.getRole() != UserRole.DEVELOPER) {
            return createErrorNode("The user does not have permission to execute this command: "
                    + "required role DEVELOPER; user role " + user.getRole() + ".");
        }
        if (ticket == null) {
            return null;
        }
        if (ticket.getAssignedTo() == null
                || !ticket.getAssignedTo().getUsername().equals(user.getUsername())) {
            return createErrorNode("Ticket " + ticketId + " is not assigned to developer "
                    + user.getUsername() + ".");
        }
        TicketStatus newState = null;
        switch (ticket.getState()) {
            case TicketStatus.IN_PROGRESS:
                newState = TicketStatus.RESOLVED;
                ticket.setSolvedAt(super.getTimestamp());
                break;
            case TicketStatus.RESOLVED:
                newState = TicketStatus.CLOSED;
                ticket.setClosedAt(super.getTimestamp());
                break;
            default:
                return null;
        }
        TicketHistory history = TicketHistory.statusChanged(ticket.getState().toString(),
                newState.toString(), super.getUsername(), super.getTimestamp());
        ticket.setState(newState);
        ticket.addHistory(history);
        if (newState == TicketStatus.CLOSED) {
            unblockMilestones(ticket);
        }
        return null;
    }
    private void unblockMilestones(final Ticket ticket) {
        Milestone currMilestone = null;
        for (Milestone milestone : appStorage.getMilestones()) {
            if (milestone.getTicketIds().contains(ticket.getId())) {
                currMilestone = milestone;
                break;
            }
        }
        if (currMilestone == null) {
            return;
        }
        boolean completeMilestone = true;
        for (int ticketId : currMilestone.getTicketIds()) {
            if (appStorage.getTicket(ticketId).getState() != TicketStatus.CLOSED) {
                completeMilestone = false;
            }
        }
        if (completeMilestone && currMilestone.getBlockingFor() != null) {
            for (String blockingFor : currMilestone.getBlockingFor()) {
                Milestone blockedMilestone = appStorage.getMilestone(blockingFor);
                if (blockedMilestone == null) {
                    break;
                }
                blockedMilestone.removeBlocker(currMilestone.getName());
                blockedMilestone.setLastAutomaticUpdate(LocalDate.parse(super.getTimestamp()));
                for (String developerName : blockedMilestone.getAssignedDevs()) {
                    User developer = appStorage.findUser(developerName);
                    blockedMilestone.addObserver((Developer) developer);
                }
                LocalDate dueDate = LocalDate.parse(blockedMilestone.getDueDate());
                LocalDate current = LocalDate.parse(super.getTimestamp());

                if (current.isAfter(dueDate)) {
                    blockedMilestone.sendNotifications("Milestone " + blockedMilestone.getName()
                            + " was unblocked after due date. All active tickets are now"
                            + " CRITICAL.");
                } else {
                    blockedMilestone.sendNotifications("Milestone " + blockedMilestone.getName()
                            + " is now unblocked as ticket " + ticket.getId() + " has been"
                            + " CLOSED.");
                }
            }
        }
    }
}
