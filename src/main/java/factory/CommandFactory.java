package factory;

import commands.*;
import commands.SearchCommand;
import fileio.CommandInput;

public class CommandFactory {
    public static Command createCommand(CommandInput commandInput) {
        String username = commandInput.getUsername();
        String timestamp = commandInput.getTimestamp();
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
            case "assignTicket" -> new AssignTicketCommand(username, timestamp,
                    commandInput.getTicketId());
            case "undoAssignTicket" -> new UndoAssignTicketCommand(username, timestamp,
                    commandInput.getTicketId());
            case "viewAssignedTickets" -> new ViewAssignedTicketsCommand(username, timestamp);
            case "addComment" -> new AddCommentCommand(username, timestamp,
                    commandInput.getTicketId(), commandInput.getComment());
            case "undoAddComment" -> new UndoAddCommentCommand(username, timestamp,
                    commandInput.getTicketId());
            case "changeStatus" -> new ChangeStatusCommand(username, timestamp,
                    commandInput.getTicketId());
            case "undoChangeStatus" -> new UndoChangeStatusCommand(username, timestamp,
                    commandInput.getTicketId());
            case "viewTicketHistory" -> new ViewTicketHistoryCommand(username, timestamp);
            case "search" -> new SearchCommand(username, timestamp, commandInput.getFilters());
            case "viewNotifications" -> new ViewNotificationsCommand(username, timestamp);
            case "generateCustomerImpactReport" -> new GenerateCustomerImpactReportCommand(username
                    , timestamp);
            case "generateTicketRiskReport" -> new GenerateTicketRiskReportCommand(username
                    , timestamp);
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
