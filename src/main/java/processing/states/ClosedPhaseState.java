package processing.states;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import enums.AppPhase;

import java.util.Set;

public class ClosedPhaseState implements StateSpecificActions {
    private static final Set<String> FORBIDDEN_COMMANDS = Set.of("createMilestone", "assignTicket",
            "undoAssignTicket", "changeStatus", "undoChangeStatus", "viewAssignedTickets",
            "viewTickets", "viewMilestones", "search", "viewTicketHistory", "addComment",
            "undoAddComment", "viewNotifications", "startTestingPhase", "lostInvestors",
            "generatePerformanceReport", "generateTicketRiskReport",
            "generateResolutionEfficiencyReport", "generateCustomerImpactReport",
            "appStabilityReport", "reportTicket");
    @Override
    public boolean canExecuteThisPhase(String commandName) {
        return false;
    }
    @Override
    public ObjectNode executeCommand(Command command) {
        return command.execute(false);
    }
    @Override
    public AppPhase getPhase() {
        return AppPhase.CLOSED;
    }
}
