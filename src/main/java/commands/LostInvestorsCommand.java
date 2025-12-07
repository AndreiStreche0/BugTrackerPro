package commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CommandInput;

import java.util.List;

public class LostInvestorsCommand {
    public LostInvestorsCommand(CommandInput commandInput, List<ObjectNode> output) {
        super(commandInput, output);
    }
    @Override
    public void executeSpecificCommand() {

    }
}
