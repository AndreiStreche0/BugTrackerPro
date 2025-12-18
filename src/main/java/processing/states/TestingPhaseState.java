package processing.states;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import enums.AppPhase;

import java.util.Set;

public class TestingPhaseState implements StateSpecificActions {
    private static final Set<String> ALLOWED_COMMANDS = Set.of("reportTicket", "viewTickets",
            "viewMilestones", "search", "viewTicketHistory", "addComment", "undoAddComment",
            "viewNotifications", "lostInvestors", "startTestingPhase", "generatePerformanceReport",
            "generateTicketRiskReport", "generateResolutionEfficiencyReport",
            "generateCustomerImpactReport", "appStabilityReport");
    private static final Set<String> FORBIDDEN_COMMANDS = Set.of("createMilestone", "assignTicket",
            "undoAssignTicket", "changeStatus", "undoChangeStatus", "viewAssignedTickets");
    private final ObjectMapper mapper = new ObjectMapper();
    /**
     * @param commandName the command name
     * @return if the command can be executed this phase
     */
    @Override
    public boolean canExecuteThisPhase(final String commandName) {
        if (FORBIDDEN_COMMANDS.contains(commandName)) {
            return false;
        }
        return ALLOWED_COMMANDS.contains(commandName);
    }
    /**
     * executes the command
     * @param command the command
     * @return the output node for that command
     */
    @Override
    public ObjectNode executeCommand(final Command command) {
        if (!canExecuteThisPhase(command.getCommandName())) {

            return command.execute(false);
        }
        return command.execute(true);
    }

    /**
     * @return the app phase
     */
    @Override
    public AppPhase getPhase() {
        return AppPhase.TESTING_PHASE;
    }
}
