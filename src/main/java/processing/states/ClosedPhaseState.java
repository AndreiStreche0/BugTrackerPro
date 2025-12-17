package processing.states;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import enums.AppPhase;

import java.util.Set;

/**
 * the closed phase class that handles all the possibilities for that phase
 */
public final class ClosedPhaseState implements StateSpecificActions {
    private static final Set<String> FORBIDDEN_COMMANDS = Set.of("createMilestone", "assignTicket",
            "undoAssignTicket", "changeStatus", "undoChangeStatus", "viewAssignedTickets",
            "viewTickets", "viewMilestones", "search", "viewTicketHistory", "addComment",
            "undoAddComment", "viewNotifications", "startTestingPhase", "lostInvestors",
            "generatePerformanceReport", "generateTicketRiskReport",
            "generateResolutionEfficiencyReport", "generateCustomerImpactReport",
            "appStabilityReport", "reportTicket");

    /**
     * @param commandName the command name
     * @return if the command can be executed
     */
    @Override
    public boolean canExecuteThisPhase(final String commandName) {
        return false;
    }

    /**
     * executes the command
     * @param command the command
     * @return the output node for that command
     */
    @Override
    public ObjectNode executeCommand(final Command command) {
        return command.execute(false);
    }
    /**
     * @return the app phase
     */
    @Override
    public AppPhase getPhase() {
        return AppPhase.CLOSED;
    }
}
