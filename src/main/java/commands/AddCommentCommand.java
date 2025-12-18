package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.TicketStatus;
import enums.UserRole;
import processing.AppStorage;
import processing.Comment;
import tickets.Ticket;
import users.User;

/**
 * command to add a comment for a specific ticket
 */
public final class AddCommentCommand extends Command {
    private static final int LENGTH_THRESHOLD = 10;
    private final ObjectMapper mapper = new ObjectMapper();
    private final AppStorage appStorage;
    private final int ticketId;
    private final String commentString;

    public AddCommentCommand(final String username, final String timestamp, final int ticketId,
                             final String commentString) {
        super("addComment", username, timestamp);
        this.appStorage = AppStorage.getInstance();
        this.ticketId = ticketId;
        this.commentString = commentString;
    }

    /**
     * creates error node for json output
     * @param message the string used for the node
     * @return the ObjectNode output
     */
    private ObjectNode createErrorNode(final String message) {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "addComment");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        output.put("error", message);
        return output;
    }

    /**
     * checks for errors and if everything is okay, creates new comment and adds it to the ticket
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
        if (user.getRole() == UserRole.MANAGER) {
            return createErrorNode("The user does not have permission to execute this command:"
                    + " required role DEVELOPER, REPORTER; user role " + user.getRole() + ".");
        }
        if (ticket == null) {
            return null;
        }
        if (ticket.getReportedBy() == null || ticket.getReportedBy().isEmpty()) {
            return createErrorNode("Comments are not allowed on anonymous tickets.");
        }
        if (commentString == null || commentString.length() < LENGTH_THRESHOLD) {
            return createErrorNode("Comment must be at least 10 characters long.");
        }
        switch (user.getRole()) {
            case UserRole.DEVELOPER:
                if (ticket.getAssignedTo() == null
                        || !ticket.getAssignedTo().getUsername().equals(user.getUsername())) {
                    return createErrorNode("Ticket " + ticket.getId() + " is not assigned to"
                            + " the developer " + user.getUsername() + ".");
                }
                break;
            case UserRole.REPORTER:
                if (ticket.getState() == TicketStatus.CLOSED) {
                    return createErrorNode("Reporters cannot comment on CLOSED tickets.");
                }
                if (!ticket.getReportedBy().equals(user.getUsername())) {
                    return createErrorNode("Reporter " + user.getUsername()
                            + " cannot comment on ticket " + ticketId + ".");
                }
                break;
            default:
                return null;
        }
        Comment newComment = new Comment(user.getUsername(), this.commentString,
                super.getTimestamp());
        ticket.addComment(newComment);
        return null;
    }
}
