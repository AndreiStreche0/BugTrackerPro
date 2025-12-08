package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.UserRole;
import fileio.CommandInput;
import processing.AppStorage;
import users.User;

import java.util.List;

public class LostInvestorsCommand extends Command {
    private final ObjectMapper mapper = new ObjectMapper();
    private final AppStorage appStorage;

    public LostInvestorsCommand(String username, String timestamp) {
        super("lostInvestors", username, timestamp);
        appStorage = AppStorage.getInstance();
    }
    private ObjectNode createErrorNode(String message) {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "lostInvestors");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        output.put("error", message);
        return output;
    }
    @Override
    public ObjectNode execute(boolean correctPhase) {
        ObjectNode output = mapper.createObjectNode();
        if (super.getUsername() == null || appStorage.findUser(super.getUsername()) == null) {
            return createErrorNode("The user " + super.getUsername() + " doesn't exist.");
        }
        User user = appStorage.findUser(super.getUsername());
        if (user.getRole()!= UserRole.MANAGER) {
            return createErrorNode("The user does not have permission to execute this command:" +
                    " required role MANAGER; user role " + user.getRole());
        }
        if (!correctPhase) {
            return createErrorNode("LostInvestors can only happen during testing and developing" +
                    " phases");
        }
        return null;
    }
}
