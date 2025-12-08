package processing.states;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import enums.AppPhase;
import fileio.CommandInput;

public interface StateSpecificActions {
    ObjectNode executeCommand(Command command);
    boolean canExecuteThisPhase(String commandName);
    AppPhase getPhase();
}
