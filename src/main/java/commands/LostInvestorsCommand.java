package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.UserRole;
import processing.AppStorage;
import users.User;

/**
 * closes the app
 */
public final class LostInvestorsCommand extends Command {
    private final ObjectMapper mapper = new ObjectMapper();
    private final AppStorage appStorage;

    public LostInvestorsCommand(final String username, final String timestamp) {
        super("lostInvestors", username, timestamp);
        appStorage = AppStorage.getInstance();
    }
    /**
     * creates error node for json output
     * @param message the string used for the node
     * @return the ObjectNode output
     */
    private ObjectNode createErrorNode(final String message) {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "lostInvestors");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        output.put("error", message);
        return output;
    }
    /**
     * closes the app after the investors leave
     * @param correctPhase says if the command can be done in this phase
     * @return the output node
     */
    @Override
    public ObjectNode execute(final boolean correctPhase) {
        ObjectNode output = mapper.createObjectNode();
        if (super.getUsername() == null || appStorage.findUser(super.getUsername()) == null) {
            return createErrorNode("The user " + super.getUsername() + " doesn't exist.");
        }
        User user = appStorage.findUser(super.getUsername());
        if (user.getRole() != UserRole.MANAGER) {
            return createErrorNode("The user does not have permission to execute this command:"
                    + " required role MANAGER; user role " + user.getRole());
        }
        if (!correctPhase) {
            return createErrorNode("LostInvestors can only happen during testing and developing"
                    + " phases");
        }
        return null;
    }
}
