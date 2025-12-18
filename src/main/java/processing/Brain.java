package processing;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import factories.CommandFactory;
import fileio.CommandInput;
import fileio.UserInput;
import processing.states.StateSpecificActions;

import java.util.ArrayList;
import java.util.List;

/**
 * class used for parsing commands and handling app phase
 */
public final class Brain {
    private final List<CommandInput> commands;
    private ProjectProcessing projectProcessor;
    private StateSpecificActions currentState;

    public Brain(final List<CommandInput> commands, final List<UserInput> users) {
        this.commands = commands;
        AppStorage appStorage = AppStorage.getInstance();
        this.projectProcessor = null;
        appStorage.loadUsers(users);
    }

    /**
     * parses the commands
     * @return list of output nodes
     */
    public List<ObjectNode> parseCommands() {
        List<ObjectNode> outputs = new ArrayList<>();
        for (CommandInput commandInput : commands) {
            if (this.projectProcessor == null) {
                this.projectProcessor = new ProjectProcessing(commandInput.getTimestamp());
            }

            Command command = CommandFactory.createCommand(commandInput);
            ObjectNode result = projectProcessor.executeCommand(command,
                    commandInput.getTimestamp());
            if (result == null && command.getCommandName().equals("lostInvestors")) {
                projectProcessor.closeApp(command.getTimestamp());
            }
            if (result == null && command.getCommandName().equals("startTestingPhase")) {
                projectProcessor.startTestingPhase(command.getTimestamp());
            }
            if (result != null) {
                outputs.add(result);
            }
        }
        return outputs;
    }
}
