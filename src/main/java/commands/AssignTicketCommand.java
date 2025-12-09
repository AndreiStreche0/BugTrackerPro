package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.*;
import milestones.Milestone;
import processing.AppStorage;
import tickets.Ticket;
import users.Developer;
import users.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AssignTicketCommand extends Command {
    private final int ticketId;
    private final ObjectMapper mapper = new ObjectMapper();
    private final AppStorage appStorage;
    public AssignTicketCommand(String username, String timestamp, int ticketId) {
        super("assignTicket", username, timestamp);
        this.ticketId = ticketId;
        this.appStorage = AppStorage.getInstance();
    }

    private ObjectNode createErrorNode(String message) {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "assignTicket");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        output.put("error", message);
        return output;
    }

    @Override
    public ObjectNode execute(boolean correctPhase) {
        User user = appStorage.findUser(super.getUsername());
        Ticket ticket = appStorage.getTicket(this.ticketId);
        if (user == null) {
            return createErrorNode("The user " + super.getUsername() + " does not exist.");
        }
        if (user.getRole() != UserRole.DEVELOPER) {
            return createErrorNode("The user does not have permission to execute this command:" +
                    " required role DEVELOPER; user role " + user.getRole() + ".");
        }
        Milestone currentMilestone = null;
        Developer developer = (Developer) user;

        if (!hasRequiredExpertise(developer.getExpertise(), ticket.getExpertiseArea())) {
            return createErrorNode("Developer " + developer.getUsername() +
                    " cannot assign ticket " + ticketId + " due to expertise area. Required: " +
                    errorExpertiseString(ticket.getExpertiseArea()) + "; Current: " +
                            developer.getExpertise() + ".");
        }
        if (!hasRequiredSeniority(developer.getSeniority(), ticket.getBusinessPriority())) {
            return createErrorNode("Developer " + developer.getUsername() + " cannot assign ticket" +
                    " " + ticketId + " due to seniority level. Required: " +
                    errorSeniorityString(ticket.getBusinessPriority()) + "; Current: " +
                    developer.getSeniority() + ".");
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
            return createErrorNode("Developer " + user.getUsername() +
                    " is not assigned to milestone " + currentMilestone.getName() + ".");
        }
        if (currentMilestone.isBlocked()) {
            return createErrorNode("Cannot assign ticket " + ticketId + " from blocked milestone "
                    + currentMilestone.getName() + ".");
        }
        ticket.setAssignedAt(super.getTimestamp());
        ticket.setAssignedTo(user);
        ticket.setState(TicketStatus.IN_PROGRESS);
        developer.addAssignedTicket(ticket);
        return null;
    }
    private boolean hasRequiredExpertise(ExpertiseArea developerExpertise, ExpertiseArea
            ticketArea) {
        if (developerExpertise == ExpertiseArea.FULLSTACK) {
            return true;
        }
        return switch (developerExpertise) {
            case FRONTEND -> ticketArea == ExpertiseArea.FRONTEND || ticketArea == ExpertiseArea.DESIGN;
            case BACKEND -> ticketArea == ExpertiseArea.BACKEND || ticketArea == ExpertiseArea.DB;
            case DEVOPS -> ticketArea == ExpertiseArea.DEVOPS;
            case DESIGN -> ticketArea == ExpertiseArea.DESIGN || ticketArea == ExpertiseArea.FRONTEND;
            case DB -> ticketArea == ExpertiseArea.DB;
            default -> false;
        };
    }
    private String errorExpertiseString (ExpertiseArea ticketExpertise) {
        List<String> requiredAreas = new ArrayList<>();
        for (ExpertiseArea expertise : ExpertiseArea.values()) {
            if (hasRequiredExpertise(expertise, ticketExpertise)) {
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
    private boolean hasRequiredSeniority (Seniority developerSeniority, BusinessPriority
            ticketPriority) {
        return switch (developerSeniority) {
            case Seniority.SENIOR -> true;
            case Seniority.MID -> ticketPriority != BusinessPriority.CRITICAL;
            case Seniority.JUNIOR -> ticketPriority == BusinessPriority.LOW ||
                    ticketPriority == BusinessPriority.MEDIUM;
            default -> false;
        };
    }
    public String errorSeniorityString (BusinessPriority ticketPriority) {
        List<String> requiredLevels = new ArrayList<>();
        for (Seniority seniority : Seniority.values()) {
            if (hasRequiredSeniority(seniority, ticketPriority)) {
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
