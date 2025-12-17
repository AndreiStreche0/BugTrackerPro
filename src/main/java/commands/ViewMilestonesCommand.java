package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.TicketStatus;
import enums.UserRole;
import milestones.Milestone;
import processing.AppStorage;
import tickets.Ticket;
import users.Developer;
import users.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * shows the milestones from the perspective of a developer or a manager
 */
public final class ViewMilestonesCommand extends Command{
    private final ObjectMapper mapper = new ObjectMapper();
    private final AppStorage appStorage;

    public ViewMilestonesCommand(final String username, final String timestamp) {
        super("viewMilestones", username, timestamp);
        this.appStorage = AppStorage.getInstance();
    }
    /**
     * creates error node for json output
     * @param message the string used for the node
     * @return the ObjectNode output
     */
    private ObjectNode createErrorNode(final String message) {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "viewMilestones");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        output.put("error", message);
        return output;
    }

    /**
     * sorts and shows the milestones from the perspective of a developer or a manager
     * @param correctPhase says if the command can be done in this phase
     * @return the output node
     */
    @Override
    public ObjectNode execute(final boolean correctPhase) {
        User user = appStorage.findUser(super.getUsername());
        if (user == null) {
            return createErrorNode("The user " + super.getUsername() + " doesn't exist.");
        }
        List<Milestone> viewableMilestones = new ArrayList<>();
        if (user.getRole() == UserRole.MANAGER) {
            for (Milestone milestne : appStorage.getMilestones()) {
                if (milestne.getCreatedBy().equals(user.getUsername())) {
                    viewableMilestones.add(milestne);
                }
            }
        } else if (user.getRole() == UserRole.DEVELOPER) {
            for (Milestone milestne : appStorage.getMilestones()) {
                if (milestne.getAssignedDevs().contains(user.getUsername())) {
                    viewableMilestones.add(milestne);
                }
            }
        }
        viewableMilestones.sort((m1, m2) -> {
            int dateComp = m1.getDueDate().compareTo(m2.getDueDate());
            if (dateComp != 0) return dateComp;
            return m1.getName().compareTo(m2.getName());
        });
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "viewMilestones");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());

        ArrayNode milestonesArray = mapper.createArrayNode();
        LocalDate latestClosing = LocalDate.MIN;
        for (Milestone milestone : viewableMilestones) {
            ObjectNode outputNode = mapper.createObjectNode();
            outputNode.put("name", milestone.getName());
            ArrayNode blockingNode = mapper.valueToTree(milestone.getBlockingFor());
            outputNode.set("blockingFor", blockingNode);
            outputNode.put("dueDate", milestone.getDueDate());
            outputNode.put("createdAt", milestone.getCreatedAt());
            ArrayNode ticketsNode = mapper.valueToTree(milestone.getTicketIds());
            outputNode.set("tickets", ticketsNode);
            ArrayNode devsNode = mapper.valueToTree(milestone.getAssignedDevs());
            outputNode.set("assignedDevs", devsNode);
            outputNode.put("createdBy", milestone.getCreatedBy());

            int cnt_opened = 0, cnt_closed = 0;
            List<Integer> openTickets = new ArrayList<>();
            List<Integer> closedTickets = new ArrayList<>();
            for (int ticketId : milestone.getTicketIds()) {
                TicketStatus ticketState = appStorage.getTicket(ticketId).getState();
                if (ticketState == TicketStatus.CLOSED) {
                    cnt_closed++;
                    if (LocalDate.parse(appStorage.getTicket(ticketId).getClosedAt()).isAfter(latestClosing)) {
                        latestClosing = LocalDate.parse(appStorage.getTicket(ticketId).getClosedAt());
                    }
                    closedTickets.add(ticketId);
                } else {
                    cnt_opened++;
                    openTickets.add(ticketId);
                }
            } if (milestone.getTicketIds().isEmpty()) {
                outputNode.put("status", "ACTIVE");
            } else {
                outputNode.put("status", cnt_opened > 0 ? "ACTIVE" : "COMPLETED");
            }
            outputNode.put("isBlocked", milestone.isBlocked());

            LocalDate currentTime = LocalDate.parse(super.getTimestamp());
            LocalDate dueTime = LocalDate.parse(milestone.getDueDate());
            int daysUntilMilestone = (int) ChronoUnit.DAYS.between(currentTime, dueTime) + 1;
            int overdueBy = 0;
            if (currentTime.isAfter(dueTime)) {
                if (cnt_opened != 0 || latestClosing.isAfter(currentTime)) {
                    overdueBy = (int) ChronoUnit.DAYS.between(dueTime, currentTime) + 1;
                } else {
                    overdueBy = (int) ChronoUnit.DAYS.between(dueTime, latestClosing) + 1;
                }
            }
            if (overdueBy < 0) {
                overdueBy = 0;
            }
            if (daysUntilMilestone < 0) {
                daysUntilMilestone = 0;
            }
            outputNode.put("daysUntilDue", daysUntilMilestone);
            outputNode.put("overdueBy", overdueBy);
            outputNode.set("openTickets",mapper.valueToTree(openTickets));
            outputNode.set("closedTickets", mapper.valueToTree(closedTickets));
            int totalTickets = cnt_closed + cnt_opened;
            double completionRate = (double) cnt_closed / totalTickets;
            completionRate = Math.round(completionRate * 100.0) / 100.0;
            outputNode.put("completionPercentage", completionRate);

            ArrayNode repartitionNode = mapper.createArrayNode();
            for (String developerName : milestone.getAssignedDevs()) {
                ObjectNode developerObject = mapper.createObjectNode();
                developerObject.put("developer", developerName);
                ArrayNode assignedTicketsArray = mapper.createArrayNode();
                List<Integer> developerTicketIds = new ArrayList<>();
                if (appStorage.findUser(developerName) != null) {
                    Developer developer = (Developer)appStorage.findUser(developerName);
                    List <Ticket> tickets = developer.getAssignedTickets();
                    for (Ticket ticket : tickets) {
                        if (milestone.getTicketIds().contains(ticket.getId())) {
                            developerTicketIds.add(ticket.getId());
                        }
                    }
                    Collections.sort(developerTicketIds);
                    for(Integer id : developerTicketIds) {
                        assignedTicketsArray.add(id);
                    }
                }
                developerObject.set("assignedTickets", assignedTicketsArray);
                repartitionNode.add(developerObject);
            }
            outputNode.set("repartition", repartitionNode);
            milestonesArray.add(outputNode);
        }
        output.set("milestones", milestonesArray);
        return output;
    }
}
