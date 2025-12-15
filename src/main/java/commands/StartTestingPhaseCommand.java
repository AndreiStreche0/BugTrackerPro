package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.AppPhase;
import enums.TicketStatus;
import enums.UserRole;
import processing.AppStorage;
import tickets.Ticket;
import users.User;
import milestones.Milestone;

public class StartTestingPhaseCommand extends Command{
    private final ObjectMapper mapper = new ObjectMapper();
    private final AppStorage appStorage;
    public StartTestingPhaseCommand(String username, String timestamp) {
        super("startTestingPhase", username, timestamp);
        this.appStorage = AppStorage.getInstance();
    }
    private ObjectNode createErrorNode(String message) {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "startTestingPhase");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        output.put("error", message);
        return output;
    }
    @Override
    public ObjectNode execute(boolean correctPhase) {
        User user = appStorage.findUser(super.getUsername());
        if (user == null) {
            return createErrorNode("The user " + super.getUsername() + " does not exist.");
        }
        if (user.getRole() != UserRole.MANAGER) {
            return createErrorNode("The user does not have permission to execute this command:" +
                    " required role MANAGER; user role " + user.getRole() + ".");
        }
        boolean noOpenMilestone = true;
        for (Milestone milestone : appStorage.getMilestones()) {
            for (int id : milestone.getTicketIds()) {
                Ticket ticket = appStorage.getTicket(id);
                if (ticket.getState() == TicketStatus.OPEN || ticket.getState() == TicketStatus.IN_PROGRESS) {
                     noOpenMilestone = false;
                }
            }
        }
        if (!noOpenMilestone) {
            return createErrorNode("Cannot start a new testing phase.");
        }
        return null;
    }
}
