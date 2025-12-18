package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.UserRole;
import milestones.Milestone;
import processing.AppStorage;
import tickets.TicketHistory;
import users.Developer;
import users.User;

import java.time.LocalDate;
import java.util.List;

/**
 * creates a new milestone for the app
 */
public final class CreateMilestoneCommand extends Command {
    private final String name;
    private final String dueDate;
    private final List<String> blockingFor;
    private final List<Integer> ticketIds;
    private final List<String> assignedDevs;
    private final ObjectMapper mapper = new ObjectMapper();
    private final AppStorage appStorage;

    public CreateMilestoneCommand(final String username, final String timestamp,
                                  final List<String> blockingFor, final List<Integer> ticketIds,
                                  final List<String> assignedDevs, final String dueDate,
                                  final String name) {
        super("createMilestone", username, timestamp);
        this.blockingFor = blockingFor;
        this.ticketIds = ticketIds;
        this.assignedDevs = assignedDevs;
        this.appStorage = AppStorage.getInstance();
        this.dueDate = dueDate;
        this.name = name;
    }
    /**
     * creates error node for json output
     * @param message the string used for the node
     * @return the ObjectNode output
     */
    private ObjectNode createErrorNode(final String message) {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "createMilestone");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        output.put("error", message);
        return output;
    }
    /**
     * allows manager to create a new milestone for the app
     * @param correctPhase says if the command can be done in this phase
     * @return the output node
     */
    @Override
    public ObjectNode execute(final boolean correctPhase) {
        User user = appStorage.findUser(super.getUsername());
        if (user == null) {
            return createErrorNode("The user " + super.getUsername() + " doesn't exist.");
        }
        if (user.getRole() != UserRole.MANAGER) {
            return createErrorNode("The user does not have permission to execute this command:"
                    + " required role MANAGER; user role " + user.getRole() + ".");
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

        Milestone milestone = new Milestone(this.name, this.blockingFor, this.dueDate,
                super.getTimestamp(), ticketIds, this.assignedDevs, super.getUsername());
        appStorage.addMilestone(milestone);
        for (String developerName : this.assignedDevs) {
            User subordonate = appStorage.findUser(developerName);
            if (subordonate.getRole() == UserRole.DEVELOPER) {
                milestone.addObserver((Developer) subordonate);
            } else {
                return createErrorNode("Assigned developer is not a developer");
            }
        }
        String notificationMessage = "New milestone " + milestone.getName()
                + " has been created with due date " + milestone.getDueDate() + ".";
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
