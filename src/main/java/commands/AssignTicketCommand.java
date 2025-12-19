package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.UserRole;
import enums.ExpertiseArea;
import enums.Seniority;
import enums.TicketStatus;
import milestones.Milestone;
import processing.AppStorage;
import tickets.Ticket;
import tickets.TicketHistory;
import users.Developer;
import users.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * command to asign ticket
 */
public final class AssignTicketCommand extends Command {
    private final int ticketId;
    private final ObjectMapper mapper = new ObjectMapper();
    private final AppStorage appStorage;
    public AssignTicketCommand(final String username, final String timestamp, final int ticketId) {
        super("assignTicket", username, timestamp);
        this.ticketId = ticketId;
        this.appStorage = AppStorage.getInstance();
    }
    /**
     * creates error node for json output
     * @param message the string used for the node
     * @return the ObjectNode output
     */
    private ObjectNode createErrorNode(final String message) {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "assignTicket");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        output.put("error", message);
        return output;
    }

    /**
     * assigns ticket to the developer
     * @param correctPhase says if the command can be done in this phase
     * @return the output node
     */
    @Override
    public ObjectNode execute(final boolean correctPhase) {
        User user = appStorage.findUser(super.getUsername());
        Ticket ticket = appStorage.getTicket(this.ticketId);
        if (user == null) {
            return createErrorNode("The user " + super.getUsername() + " does not exist.");
        }
        if (user.getRole() != UserRole.DEVELOPER) {
            return createErrorNode("The user does not have permission to execute this command:"
                    + " required role DEVELOPER; user role " + user.getRole() + ".");
        }
        Milestone currentMilestone = null;
        Developer developer = (Developer) user;
        if (ticket == null) {
            return createErrorNode("Ticket id is not valid");
        }
        if (!developer.hasRequiredExpertise(ticket.getExpertiseArea())) {
            return createErrorNode("Developer " + developer.getUsername()
                    + " cannot assign ticket " + ticketId + " due to expertise area. Required: "
                    + errorExpertiseString(ticket) + "; Current: " + developer.getExpertise()
                    + ".");
        }
        if (!developer.hasRequiredSeniority(ticket.getBusinessPriority())) {
            return createErrorNode("Developer " + developer.getUsername() + " cannot assign ticket"
                    + " " + ticketId + " due to seniority level. Required: "
                    + errorSeniorityString(ticket) + "; Current: " + developer.getSeniority()
                    + ".");
        }
        if (ticket.getState() != TicketStatus.OPEN) {
            return createErrorNode("Only OPEN tickets can be assigned.");
        }
        for (Milestone milestone : appStorage.getMilestones()) {
            if (milestone.getTicketIds().contains(this.ticketId)) {
                currentMilestone = milestone;
            }
        }
        if (currentMilestone == null) {
            return createErrorNode("Ticket " + ticketId + " is not assigned to any milestone.");
        }
        if (!currentMilestone.getAssignedDevs().contains(developer.getUsername())) {
            return createErrorNode("Developer " + user.getUsername()
                    + " is not assigned to milestone " + currentMilestone.getName() + ".");
        }
        if (currentMilestone.isBlocked()) {
            return createErrorNode("Cannot assign ticket " + ticketId + " from blocked milestone "
                    + currentMilestone.getName() + ".");
        }

        TicketHistory history = TicketHistory.assign(super.getUsername(), super.getTimestamp());
        ticket.addHistory(history);
        history = TicketHistory.statusChanged(ticket.getState().toString(),
                TicketStatus.IN_PROGRESS.toString(), super.getUsername(), super.getTimestamp());
        ticket.addHistory(history);
        ticket.setAssignedAt(super.getTimestamp());
        ticket.setAssignedTo(user);
        ticket.setState(TicketStatus.IN_PROGRESS);
        developer.addAssignedTicket(ticket);
        return null;
    }

    /**
     * helper function for output in case of wrong expertise
     * @param ticket that can't be assigned
     * @return the string containing correct expertise
     */
    private String errorExpertiseString(final Ticket ticket) {
        List<String> requiredAreas = new ArrayList<>();
        for (ExpertiseArea expertise : ExpertiseArea.values()) {
            if (ticket.checkExpertiseLevel(expertise)) {
                requiredAreas.add(expertise.toString());
            }
        }
        Collections.sort(requiredAreas);
        StringBuilder outputString = new StringBuilder();
        for (String expertise : requiredAreas) {
            if (!outputString.isEmpty()) {
                outputString.append(", ");
            }
            outputString.append(expertise);
        }
        return outputString.toString();
    }
    /**
     * helper function for output in case of wrong seniority
     * @param ticket that can't be assigned
     * @return the string containing correct seniority
     */
    private String errorSeniorityString(final Ticket ticket) {
        List<String> requiredLevels = new ArrayList<>();
        for (Seniority seniority : Seniority.values()) {
            if (ticket.checkSeniority(seniority)) {
                requiredLevels.add(seniority.toString());
            }
        }
        Collections.sort(requiredLevels);
        StringBuilder outputString = new StringBuilder();
        for (String expertise : requiredLevels) {
            if (!outputString.isEmpty()) {
                outputString.append(", ");
            }
            outputString.append(expertise);
        }
        return outputString.toString();
    }
}
