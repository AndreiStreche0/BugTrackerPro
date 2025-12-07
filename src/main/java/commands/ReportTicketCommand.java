package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.AppPhase;
import enums.BusinessPriority;
import enums.TicketType;
import enums.UserRole;
import factory.FactoryService;
import fileio.CommandInput;
import processing.AppStorage;
import tickets.Ticket;
import users.User;

import java.util.List;
import java.util.Map;

public class ReportTicketCommand extends Command{
    private final Map<String, Object> ticketParameters;
    private final FactoryService ticketService;
    private final ObjectMapper mapper;
    private AppStorage appStorage;
    private AppPhase appPhase;
    public ReportTicketCommand(String username, String timestamp, Map<String, Object> params, AppPhase appPhase) {
        super("reportTicket", username, timestamp);
        this.ticketParameters = params;
        this.ticketService = FactoryService.getInstance();
        this.mapper = new ObjectMapper();
        this.appStorage = AppStorage.getInstance();
        this.appPhase = appPhase;
    }
    public ObjectNode execute() {
        Ticket newTicket = ticketService.createTicket((String) ticketParameters.get("type"), ticketParameters);
        ObjectNode output = mapper.createObjectNode();
        if (super.getUsername() == null || appStorage.findUser(super.getUsername()) == null) {
            output.put("error", "The user " + super.getUsername() + " doesn't exist.");
            return output;
        }
        User currentUser = appStorage.findUser(super.getUsername());
        if (currentUser.getRole() != UserRole.REPORTER) {
            output.put("error", "The user does not have permission to execute this command:" +
                    " required role Reporter; user role " + currentUser.getRole());
            return output;
        }
        if (appPhase != AppPhase.TESTING_PHASE) {
            output.put("error", "Tickets can only be reported during testing phases.)";
            return output;
        }
        String reporterName = (String) ticketParameters.get("reportedBy");
        if (reporterName != null && reporterName.isEmpty()) {
            if (newTicket.getType() == TicketType.BUG) {
                newTicket.setReportedBy("ANONYMOUS");
                newTicket.setBusinessPriority(BusinessPriority.LOW);
            } else {
                output.put("error", "Anonymous reports are only allowed for tickets of type BUG.");
                return output;
            }
        }
        appStorage.addTicket(newTicket);
        return null;
    }
}
