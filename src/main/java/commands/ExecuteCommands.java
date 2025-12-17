package commands;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * contains the methods for executing the commands
 */
public interface ExecuteCommands {
    /**
     * @param correctPhase if the command can be executed this phase
     * @return the output node for that command
     */
    ObjectNode execute(final boolean correctPhase);
}
