package processing.states;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import enums.AppPhase;

/**
 * all the methods used by all the App Phases
 */
public interface StateSpecificActions {
    /**
     * executes command
     * @param command the command
     * @return output node
     */
    ObjectNode executeCommand(Command command);

    /**
     * @param commandName the command name string
     * @return if this command can be executed for that phase
     */
    boolean canExecuteThisPhase(String commandName);

    /**
     * @return app phase
     */
    AppPhase getPhase();
}
