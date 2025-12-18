package factories;

import commands.ReportTicketCommand;
import commands.ViewTicketsCommand;
import commands.LostInvestorsCommand;
import commands.CreateMilestoneCommand;
import commands.ViewMilestonesCommand;
import commands.AssignTicketCommand;
import commands.UndoAssignTicketCommand;
import commands.ViewAssignedTicketsCommand;
import commands.AddCommentCommand;
import commands.UndoAddCommentCommand;
import commands.ChangeStatusCommand;
import commands.UndoChangeStatusCommand;
import commands.ViewTicketHistoryCommand;
import commands.ViewNotificationsCommand;
import commands.GenerateCustomerImpactReportCommand;
import commands.GenerateTicketRiskReportCommand;
import commands.GenerateResolutionEfficiencyReport;
import commands.AppStabilityReportCommand;
import commands.GeneratePerformanceReportCommand;
import commands.StartTestingPhaseCommand;
import commands.Command;
import commands.SearchCommand;
import fileio.CommandInput;

/**
 * factory for creating the commands
 */
public final class CommandFactory {
    private CommandFactory() {
        throw new UnsupportedOperationException("This is a utility class and can't be"
                + " instantiated");
    }
    /**
     * @param commandInput the input necessary for command creation
     * @return a new command object
     */
    public static Command createCommand(final CommandInput commandInput) {
        String username = commandInput.getUsername();
        String timestamp = commandInput.getTimestamp();
        int ticketId = 0;
        if (commandInput.getTicketId() != null) {
            ticketId = commandInput.getTicketId();
        }
        return switch (commandInput.getCommand()) {
            case "reportTicket" -> new ReportTicketCommand(username, timestamp,
                    commandInput.getParams());
            case "viewTickets" -> new ViewTicketsCommand(username, timestamp);
            case "lostInvestors" -> new LostInvestorsCommand(username, timestamp);
            case "createMilestone" -> new CreateMilestoneCommand(username, timestamp,
                    commandInput.getBlockingFor(), commandInput.getTickets(),
                    commandInput.getAssignedDevs(), commandInput.getDueDate(),
                    commandInput.getName());
            case "viewMilestones" -> new ViewMilestonesCommand(username, timestamp);
            case "assignTicket" -> new AssignTicketCommand(username, timestamp, ticketId);
            case "undoAssignTicket" -> new UndoAssignTicketCommand(username, timestamp, ticketId);
            case "viewAssignedTickets" -> new ViewAssignedTicketsCommand(username, timestamp);
            case "addComment" -> new AddCommentCommand(username, timestamp, ticketId,
                    commandInput.getComment());
            case "undoAddComment" -> new UndoAddCommentCommand(username, timestamp, ticketId);
            case "changeStatus" -> new ChangeStatusCommand(username, timestamp, ticketId);
            case "undoChangeStatus" -> new UndoChangeStatusCommand(username, timestamp, ticketId);
            case "viewTicketHistory" -> new ViewTicketHistoryCommand(username, timestamp);
            case "search" -> new SearchCommand(username, timestamp, commandInput.getFilters());
            case "viewNotifications" -> new ViewNotificationsCommand(username, timestamp);
            case "generateCustomerImpactReport" -> new GenerateCustomerImpactReportCommand(
                    username, timestamp);
            case "generateTicketRiskReport" -> new GenerateTicketRiskReportCommand(username,
                    timestamp);
            case "generateResolutionEfficiencyReport" -> new GenerateResolutionEfficiencyReport(
                    username, timestamp);
            case "appStabilityReport" -> new AppStabilityReportCommand(username, timestamp);
            case "generatePerformanceReport" -> new GeneratePerformanceReportCommand(username,
                    timestamp);
            case "startTestingPhase" -> new StartTestingPhaseCommand(username, timestamp);
            default -> {
                System.out.println("Command not found");
                yield null;
            }
        };
    }
}
