package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.BusinessPriority;
import enums.TicketStatus;
import enums.TicketType;
import enums.UserRole;
import metrics.MetricBaseMethods;
import metrics.ResolutionEfficiencyMetric;
import metrics.TicketRiskMetric;
import processing.AppStorage;
import tickets.Ticket;
import users.User;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * generates resolution efficiency report
 */
public final class GenerateResolutionEfficiencyReport extends Command{
    private final ObjectMapper mapper = new ObjectMapper();
    private final AppStorage appStorage;
    public GenerateResolutionEfficiencyReport(final String username, final String timestamp) {
        super("generateResolutionEfficiencyReport", username, timestamp);
        this.appStorage = AppStorage.getInstance();
    }
    /**
     * creates error node for json output
     * @param message the string used for the node
     * @return the ObjectNode output
     */
    private ObjectNode createErrorNode(final String message) {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "generateResolutionEfficiencyReport");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        output.put("error", message);
        return output;
    }
    /**
     * generates the resolution efficiency report and formats the output
     * @param correctPhase says if the command can be done in this phase
     * @return the output node
     */
    @Override
    public ObjectNode execute(final boolean correctPhase) {
        User user = appStorage.findUser(super.getUsername());
        ObjectNode output = mapper.createObjectNode();
        if (user == null) {
            return createErrorNode("The user " + super.getUsername() + " does not exist.");
        }
        if (user.getRole() != UserRole.MANAGER) {
            return createErrorNode("The user does not have permission to execute this command:" +
                    " required role MANAGER; user role " + user.getRole() + ".");
        }
        MetricBaseMethods report = new ResolutionEfficiencyMetric();
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
            if ((ticket.getState() == TicketStatus.RESOLVED || ticket.getState() == TicketStatus.CLOSED)
                    && report.isTicketValid(ticket)) {
                ticketsByType.put(ticket.getType(), ticketsByType.get(ticket.getType()) + 1);
                ticketsByPriority.put(ticket.getBusinessPriority(),
                        ticketsByPriority.get(ticket.getBusinessPriority()) + 1);

                scoresByType.get(ticket.getType()).add(report.normalizeScore(
                        report.calculateBaseScore(ticket), report.getMaxValue(ticket.getType())));
                cntTickets++;
            }
        }

        output.put("command", "generateResolutionEfficiencyReport");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());

        ObjectNode reportNode = mapper.createObjectNode();
        reportNode.put("totalTickets", cntTickets);
        reportNode.set("ticketsByType", mapper.valueToTree(ticketsByType));
        reportNode.set("ticketsByPriority", mapper.valueToTree(ticketsByPriority));
        ObjectNode impactOutput = mapper.createObjectNode();
        for (TicketType type : TicketType.values()) {
            double avg = report.calculateAverageScore(scoresByType.get(type));
            impactOutput.put(type.toString(), report.round(avg));
        }
        reportNode.set("efficiencyByType", impactOutput);
        output.set("report", reportNode);
        return output;
    }
}
