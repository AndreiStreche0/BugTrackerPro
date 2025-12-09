package processing.states;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import enums.AppPhase;
import fileio.CommandInput;

import java.util.Set;

public class DevelopmentPhaseState implements  StateSpecificActions{
    private static final Set<String> ALLOWED_COMMANDS = Set.of("createMilestone", "assignTicket",
            "undoAssignTicket", "changeStatus", "undoChangeStatus", "viewAssignedTickets",
            "viewTickets", "viewMilestones", "search", "viewTicketHistory", "addComment",
            "undoAddComment", "viewNotifications", "startTestingPhase", "lostInvestors",
            "generatePerformanceReport", "generateTicketRiskReport",
            "generateResolutionEfficiencyReport", "generateCustomerImpactReport",
            "appStabilityReport");
    private static final Set<String> FORBIDDEN_COMMANDS = Set.of("reportTicket");
    @Override
    public boolean canExecuteThisPhase(String commandName) {
        if (FORBIDDEN_COMMANDS.contains(commandName)) {
            return false;
        }
        return ALLOWED_COMMANDS.contains(commandName);
    }
    @Override
    public ObjectNode executeCommand(Command command) {
        if (!canExecuteThisPhase(command.getCommandName())) {
            return command.execute(false);
        }
        return command.execute(true);
    }
    @Override
    public AppPhase getPhase() {
        return AppPhase.DEVELOPING_PHASE;
    }
}
