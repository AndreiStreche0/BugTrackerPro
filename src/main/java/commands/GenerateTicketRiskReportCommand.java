package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import enums.BusinessPriority;
import enums.TicketType;
import enums.UserRole;
import metrics.MetricBaseMethods;
import metrics.TicketRiskMetric;
import processing.AppStorage;
import tickets.Ticket;
import users.User;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class GenerateTicketRiskReportCommand extends Command {
    private final ObjectMapper mapper = new ObjectMapper();
    private final AppStorage appStorage;
    public GenerateTicketRiskReportCommand(String username, String timestamp) {
        super("generateTicketRiskReport", username, timestamp);
        this.appStorage = AppStorage.getInstance();
    }
    private ObjectNode createErrorNode(String message) {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "generateTicketRiskReport");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        output.put("error", message);
        return output;
    }
    @Override
    public ObjectNode execute(boolean correctPhase) {
        User user = appStorage.findUser(super.getUsername());
        ObjectNode output = mapper.createObjectNode();
        if (user == null) {
            return createErrorNode("The user " + super.getUsername() + " does not exist.");
        }
        if (user.getRole() != UserRole.MANAGER) {
            return createErrorNode("The user does not have permission to execute this command:" +
                    " required role MANAGER; user role " + user.getRole() + ".");
        }
        MetricBaseMethods report = new TicketRiskMetric();
        Map<TicketType, Integer> ticketsByType = new EnumMap<>(TicketType.class);
        Map<BusinessPriority, Integer> ticketsByPriority = new EnumMap<>(BusinessPriority.class);
        Map<TicketType, List<Double>> scoresByType = new EnumMap<>(TicketType.class);
        int cntTickets = 0;
        for (BusinessPriority p : BusinessPriority.values()) {
            ticketsByPriority.put(p, 0);
        }
        for (TicketType t : TicketType.values()) {
            ticketsByType.put(t, 0);
            scoresByType.put(t, new ArrayList<>());
        }
        for (Ticket ticket : appStorage.getTickets()) {
            if (report.isTicketValid(ticket)) {
                ticketsByType.put(ticket.getType(), ticketsByType.get(ticket.getType()) + 1);
                ticketsByPriority.put(ticket.getBusinessPriority(),
                        ticketsByPriority.get(ticket.getBusinessPriority()) + 1);

                scoresByType.get(ticket.getType()).add(report.normalizeScore(
                        report.calculateBaseScore(ticket), report.getMaxValue(ticket.getType())));
                cntTickets++;
            }
        }

        output.put("command", "generateTicketRiskReport");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());

        ObjectNode reportNode = mapper.createObjectNode();
        reportNode.put("totalTickets", cntTickets);
        reportNode.set("ticketsByType", mapper.valueToTree(ticketsByType));
        reportNode.set("ticketsByPriority", mapper.valueToTree(ticketsByPriority));
        ObjectNode impactOutput = mapper.createObjectNode();
        for (TicketType type : TicketType.values()) {
            double avg = report.calculateAverageScore(scoresByType.get(type));
            impactOutput.put(type.toString(), this.getRiskCategory(report.round(avg)));
        }
        reportNode.set("riskByType", impactOutput);
        output.set("report", reportNode);
        return output;
    }

    public String getRiskCategory(double score) {
        if (score <= 24) {
            return "NEGLIGIBLE";
        } else if (score <= 49) {
            return "MODERATE";
        } else if (score <= 74) {
            return "SIGNIFICANT";
        } else {
            return "MAJOR";
        }
    }
}
