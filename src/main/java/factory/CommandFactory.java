package factory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import commands.LostInvestorsCommand;
import commands.ReportTicketCommand;
import commands.ViewTicketsCommand;
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
            default -> {
                System.out.println("Command not found");
                yield null;
            }
        };
    }
}
