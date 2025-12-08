package commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;
import processing.AppStorage;

import java.util.List;

public abstract class Command implements ExecuteCommands{
    private final String commandName;
    private final String username;
    private final String timestamp;

    public Command(String commandName, String username, String timestamp) {
        this.commandName = commandName;
        this.username = username;
        this.timestamp = timestamp;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getUsername() {
        return username;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public abstract ObjectNode execute(boolean correctPhase);
}
