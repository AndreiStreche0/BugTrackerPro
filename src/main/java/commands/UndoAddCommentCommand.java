package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import processing.AppStorage;
import processing.Comment;
import tickets.Ticket;

import java.util.List;

public class UndoAddCommentCommand extends Command{

    private final ObjectMapper mapper = new ObjectMapper();
    private final AppStorage appStorage;
    private int ticketId;

    public UndoAddCommentCommand(String username, String timestamp, int ticketId) {
        super("undoAddComment", username, timestamp);
        this.ticketId = ticketId;
        this.appStorage = AppStorage.getInstance();
    }
    private ObjectNode createErrorNode(String message) {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "undoAddComment");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        output.put("error", message);
        return output;
    }
    @Override
    public ObjectNode execute(boolean correctPhase) {
        Ticket ticket = appStorage.getTicket(ticketId);
        ObjectMapper mapper = new ObjectMapper();
        if (ticket == null) {
            return null;
        }
        if (ticket.getReportedBy() == null || ticket.getReportedBy().isEmpty()) {
            return createErrorNode("Comments are not allowed on anonymous tickets.");
        }
        List<Comment> comments = ticket.getComments();
        if (comments == null) {
            return null;
        }
        Comment wantedComment = null;
        for (int i = comments.size() - 1; i >= 0; i--) {
            if (comments.get(i).getAuthor().equals(super.getUsername())) {
                wantedComment = comments.get(i);
                break;
            }
        }
        if (wantedComment == null) {
            return null;
        }
        comments.remove(wantedComment);
        return null;
    }

}
