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

/**
 * command used for starting the testing phase
 */
public final class StartTestingPhaseCommand extends Command{
    private final ObjectMapper mapper = new ObjectMapper();
    private final AppStorage appStorage;
    public StartTestingPhaseCommand(final String username, final String timestamp) {
        super("startTestingPhase", username, timestamp);
        this.appStorage = AppStorage.getInstance();
    }
    /**
     * creates error node for json output
     * @param message the string used for the node
     * @return the ObjectNode output
     */
    private ObjectNode createErrorNode(final String message) {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "startTestingPhase");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        output.put("error", message);
        return output;
    }

    /**
     * starts new testing phase
     * @param correctPhase says if the command can be done in this phase
     * @return the output node
     */
    @Override
    public ObjectNode execute(final boolean correctPhase) {
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
