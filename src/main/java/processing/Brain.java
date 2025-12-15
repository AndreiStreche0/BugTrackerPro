package processing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import factory.CommandFactory;
import fileio.CommandInput;
import fileio.UserInput;
import processing.states.StateSpecificActions;
import processing.states.TestingPhaseState;
import users.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Brain {
    private final List<CommandInput> commands;
    private final AppStorage appStorage;
    private final ObjectMapper mapper;
    private ProjectProcessing projectProcessor;
    private StateSpecificActions currentState;

    public Brain(List<CommandInput> commands, List<UserInput> users) {
        this.commands = commands;
        this.appStorage = AppStorage.getInstance();
        this.mapper = new ObjectMapper();
        this.projectProcessor = null;
        this.appStorage.loadUsers(users);
    }

    public List<ObjectNode> parseCommands() {
        List<ObjectNode> outputs = new ArrayList<>();
        for (CommandInput commandInput : commands) {
            if (this.projectProcessor == null) {
                this.projectProcessor = new ProjectProcessing(commandInput.getTimestamp());
            }

            Command command = CommandFactory.createCommand(commandInput);
            ObjectNode result = projectProcessor.executeCommand(command, commandInput.getTimestamp());
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
