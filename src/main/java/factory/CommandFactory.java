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
        switch(commandInput.getCommand()) {
            case "reportTicket":
                //TODO CHANGE TESTING_PHASE
                return new ReportTicketCommand(commandInput.getUsername(), commandInput.getTimestamp(), commandInput.getParams(), AppPhase.TESTING_PHASE);
            case "viewTickets":
                return new ViewTicketsCommand(commandInput.getUsername(), commandInput.getTimestamp());
            case "lostInvestors":
                return new LostInvestorsCommand(commandInput.getUsername(), commandInput.getTimestamp());
            default:
                System.out.println("Command not found");
        }
    }

}
