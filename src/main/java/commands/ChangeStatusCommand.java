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

public class ChangeStatusCommand extends Command {
    private final ObjectMapper mapper = new ObjectMapper();
    private final AppStorage appStorage;
    private int ticketId;

    public ChangeStatusCommand(String username, String timestamp, int ticketId) {
        super("changeStatus", username, timestamp);
        this.appStorage = AppStorage.getInstance();
        this.ticketId = ticketId;
    }

    private ObjectNode createErrorNode(String message) {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "changeStatus");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        output.put("error", message);
        return output;
    }

    public ObjectNode execute(boolean correctPhase) {
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
            case TicketStatus.IN_PROGRESS:
                newState = TicketStatus.RESOLVED;
                break;
            case TicketStatus.RESOLVED:
                newState = TicketStatus.CLOSED;
                ticket.setSolvedAt(super.getTimestamp());
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
    private void unblockMilestones(Ticket ticket) {
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
                for (String developerName : blockedMilestone.getAssignedDevs()) {
                    User developer = appStorage.findUser(developerName);
                    blockedMilestone.addObserver((Developer) developer);
                }
                blockedMilestone.sendNotifications("Milestone " + blockedMilestone.getName() +
                        " is now unblocked as tichet " + ticket.getId() + " has been CLOSED.");
                LocalDate dueDate = LocalDate.parse(blockedMilestone.getDueDate());
                LocalDate current = LocalDate.parse(super.getTimestamp());

                if (current.isAfter(dueDate)) {
                    blockedMilestone.sendNotifications("Milestone " + blockedMilestone.getName() +
                            " was unblocked after due date. All active tickets are now CRITICAL.");
                }
            }
        }
    }
}