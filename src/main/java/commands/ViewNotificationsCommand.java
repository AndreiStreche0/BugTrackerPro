package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.UserRole;
import processing.AppStorage;
import users.Developer;
import users.User;

public class ViewNotificationsCommand extends Command {
    private final ObjectMapper mapper = new ObjectMapper();
    private final AppStorage appStorage;

    public ViewNotificationsCommand(String username, String timestamp) {
        super("viewNotifications", username, timestamp);
        appStorage = AppStorage.getInstance();
    }
    private ObjectNode createErrorNode(String message) {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "viewNotifications");
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
        if (user.getRole() != UserRole.DEVELOPER) {
            return createErrorNode("The user does not have permission to execute this command:" +
                    " required role DEVELOPER; user role " + user.getRole() + ".");
        }
        Developer developer = (Developer) user;
        output.put("command", "viewNotifications");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        ArrayNode notificationArray = mapper.createArrayNode();
        for (String notification : developer.getNotifications()) {
            notificationArray.add(notification);
        }
        developer.clearNotifications();
        output.set("notifications", notificationArray);
        return output;
    }
}
