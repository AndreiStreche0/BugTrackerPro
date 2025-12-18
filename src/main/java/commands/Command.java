package commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
/**
 * abstract command class that contains common parameters and methods
 */
public abstract class Command implements ExecuteCommands {
    private final String commandName;
    private final String username;
    private final String timestamp;

    public Command(final String commandName, final String username, final String timestamp) {
        this.commandName = commandName;
        this.username = username;
        this.timestamp = timestamp;
    }

    /**
     * @return command name
     */
    public String getCommandName() {
        return commandName;
    }
    /**
     * @return username of command initiator
     */
    public String getUsername() {
        return username;
    }
    /**
     * @return command timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * executes the command
     * @param correctPhase says if the command can be executed in that phase
     * @return the output node specific to that command
     */
    public abstract ObjectNode execute(boolean correctPhase);
}
