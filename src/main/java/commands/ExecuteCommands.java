package commands;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface ExecuteCommands {
    ObjectNode execute(boolean correctPhase);
}
