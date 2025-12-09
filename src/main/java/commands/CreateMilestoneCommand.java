package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.UserRole;
import milestones.Milestone;
import processing.AppStorage;
import tickets.TicketHistory;
import users.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class CreateMilestoneCommand extends Command {
    private final String name;
    private final String dueDate;
    private final List<String> blockingFor;
    private final List<Integer> ticketIds;
    private final List<String> assignedDevs;
    private final ObjectMapper mapper = new ObjectMapper();
    private final AppStorage appStorage;

    public CreateMilestoneCommand(String username, String timestamp, List<String> blockingFor,
                                  List<Integer> ticketIds, List<String> assignedDevs,
                                  String dueDate, String name) {
        super("createMilestone", username, timestamp);
        this.blockingFor = blockingFor;
        this.ticketIds = ticketIds;
        this.assignedDevs = assignedDevs;
        this.appStorage = AppStorage.getInstance();
        this.dueDate = dueDate;
        this.name = name;
    }
    private ObjectNode createErrorNode(String message) {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "createMilestone");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        output.put("error", message);
        return output;
    }
    @Override
    public ObjectNode execute(boolean correctPhase) {
        User user = appStorage.findUser(super.getUsername());
        if (user == null) {
            return createErrorNode("The user " + super.getUsername() + " doesn't exist.");
        }
        if (user.getRole() != UserRole.MANAGER) {
            return createErrorNode("The user does not have permission to execute this command:" +
                    " required role MANAGER; user role " + user.getRole() + ".");
        }
        if (!correctPhase) {
            return createErrorNode("Milestones can only be created during development phase.");
        }
        for (Integer ticketId : this.ticketIds) {
            Milestone assignedMilestone = appStorage.isTicketInMilestone(ticketId);
            if (assignedMilestone != null) {
                return createErrorNode("Tickets " + ticketId + " already assigned to milestone "
                        + assignedMilestone.getName() + ".");
            }
        }
        LocalDate created = LocalDate.parse(super.getTimestamp());
        LocalDate due = LocalDate.parse(this.dueDate);
//        if (ChronoUnit.DAYS.between(created, due) + 1 < 5) {
//            return createErrorNode("Milestone duration must be at least 5 days.");
//        }
        Milestone milestone = new Milestone(this.name, this.blockingFor, this.dueDate,
                super.getTimestamp(), ticketIds, this.assignedDevs, super.getUsername());
        appStorage.addMilestone(milestone);
        String notificationMessage = "New milestone " + milestone.getName() +
                " has been created with due date " + milestone.getDueDate() + ".";
        if (milestone.getBlockingFor() != null) {
            for (String blockedMilestoneName : milestone.getBlockingFor()) {
                Milestone blockedMilestone = appStorage.getMilestone(blockedMilestoneName);
                if (blockedMilestone != null) {
                    blockedMilestone.addBlocker(milestone.getName());
                }
            }
        }
        for (int id : milestone.getTicketIds()) {
            TicketHistory history = TicketHistory.addedToMilestone(milestone.getName(),
                    milestone.getCreatedBy(), super.getTimestamp());
            appStorage.getTicket(id).addHistory(history);
        }
        milestone.setLastAutomaticUpdate(LocalDate.parse(super.getTimestamp()));
        milestone.sendNotifications(notificationMessage);
        return null;
    }
}
