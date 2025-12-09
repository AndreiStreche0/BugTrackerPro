package factory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.*;
import enums.AppPhase;
import fileio.CommandInput;

import java.util.List;

public class CommandFactory {
    public static Command createCommand(CommandInput commandInput) {
        return switch (commandInput.getCommand()) {
            case "reportTicket" -> new ReportTicketCommand(commandInput.getUsername(),
                    commandInput.getTimestamp(), commandInput.getParams());
            case "viewTickets" -> new ViewTicketsCommand(commandInput.getUsername(),
                    commandInput.getTimestamp());
            case "lostInvestors" -> new LostInvestorsCommand(commandInput.getUsername(),
                    commandInput.getTimestamp());
            case "createMilestone" -> new CreateMilestoneCommand(commandInput.getUsername(),
                    commandInput.getTimestamp(), commandInput.getBlockingFor(),
                    commandInput.getTickets(), commandInput.getAssignedDevs(),
                    commandInput.getDueDate(), commandInput.getName());
            case "viewMilestones" -> new ViewMilestonesCommand(commandInput.getUsername(),
                    commandInput.getTimestamp());
            case "assignTicket" -> new AssignTicketCommand(commandInput.getUsername(),
                    commandInput.getTimestamp(), commandInput.getTicketId());
            case "undoAssignTicket" -> new UndoAssignTicketCommand(commandInput.getUsername(),
                    commandInput.getTimestamp(), commandInput.getTicketId());
            case "viewAssignedTickets" -> new ViewAssignedTicketsCommand(commandInput.getUsername()
                    , commandInput.getTimestamp());
            case "addComment" -> new AddCommentCommand(commandInput.getUsername(),
                    commandInput.getTimestamp(), commandInput.getTicketId(),
                    commandInput.getComment());
            case "undoAddComment" -> new UndoAddCommentCommand(commandInput.getUsername(),
                    commandInput.getTimestamp(), commandInput.getTicketId());
            case "changeStatus" -> new ChangeStatusCommand(commandInput.getUsername(),
                    commandInput.getTimestamp(), commandInput.getTicketId());
            case "undoChangeStatus" -> new UndoChangeStatusCommand(commandInput.getUsername(),
                    commandInput.getTimestamp(), commandInput.getTicketId());
            case "viewTicketHistory" -> new ViewTicketHistoryCommand(commandInput.getUsername(),
                    commandInput.getTimestamp());
            default -> {
                System.out.println("Command not found");
                yield null;
            }
        };
    }
}
