package processing.states;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import enums.AppPhase;
import fileio.CommandInput;

public interface StateSpecificActions {
    ObjectNode executeCommand(final Command command);
    boolean canExecuteThisPhase(final String commandName);
    AppPhase getPhase();
}
