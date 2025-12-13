package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.AppPhase;
import enums.BusinessPriority;
import enums.TicketType;
import enums.UserRole;
import metrics.CustomerImpactMetric;
import metrics.TicketRiskMetric;
import processing.AppStorage;
import tickets.Ticket;
import users.User;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class AppStabilityReportCommand extends Command {
    private final ObjectMapper mapper = new ObjectMapper();
    private final AppStorage appStorage;

    public AppStabilityReportCommand(String username, String timestamp) {
        super("appStabilityReport", username, timestamp);
        this.appStorage = AppStorage.getInstance();
    }
    private ObjectNode createErrorNode(String message) {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "generateResolutionEfficiencyReport");
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
        TicketRiskMetric riskReport = new TicketRiskMetric();
        CustomerImpactMetric impactReport = new CustomerImpactMetric();
        Map<TicketType, List<Double>> riskScores = new EnumMap<>(TicketType.class);
        Map<TicketType, List<Double>> impactScores = new EnumMap<>(TicketType.class);
        Map<TicketType, Integer> ticketsByType = new EnumMap<>(TicketType.class);
        Map<BusinessPriority, Integer> ticketsByPriority = new EnumMap<>(BusinessPriority.class);
        Map<TicketType, Double> averageRisk = new EnumMap<>(TicketType.class);
        Map<TicketType, Double> averageImpact = new EnumMap<>(TicketType.class);
        int cntOpenTickets = 0;
        for(BusinessPriority p : BusinessPriority.values()) {
            ticketsByPriority.put(p, 0);
        }
        for(TicketType t : TicketType.values()) {
            riskScores.put(t, new ArrayList<>());
            impactScores.put(t, new ArrayList<>());
            ticketsByType.put(t, 0);
        }
        for (Ticket ticket : appStorage.getTickets()) {
            if (riskReport.isTicketValid(ticket) && impactReport.isTicketValid(ticket)) {
                ticketsByType.put(ticket.getType(), ticketsByType.get(ticket.getType()) + 1);
                ticketsByPriority.put(ticket.getBusinessPriority(),
                        ticketsByPriority.get(ticket.getBusinessPriority()) + 1);
                riskScores.get(ticket.getType()).add(riskReport.normalizeScore(
                        riskReport.calculateBaseScore(ticket),
                        riskReport.getMaxValue(ticket.getType())));
                impactScores.get(ticket.getType()).add(impactReport.normalizeScore(
                        impactReport.calculateBaseScore(ticket),
                        impactReport.getMaxValue(ticket.getType())));
                cntOpenTickets++;
            }
        }
        boolean foundNonNegligible = false, foundSignificantRisk = false, foundNonLowImpact = false;
        for (TicketType ticket : TicketType.values()) {
            double riskAverageScore = riskReport.calculateAverageScore(riskScores.get(ticket));
            double impactAverageScore = impactReport.calculateAverageScore(
                    impactScores.get(ticket));
            averageRisk.put(ticket, riskAverageScore);
            averageImpact.put(ticket, impactAverageScore);

            if (!this.getRiskCategory(riskAverageScore).equals("NEGLIGIBLE")) {
                foundNonNegligible = true;
            }
            if (this.getRiskCategory(riskAverageScore).equals("SIGNIFICANT")
                    || this.getRiskCategory(riskAverageScore).equals("MAJOR")) {
                foundSignificantRisk = true;
            }
            if (impactAverageScore >= 50.0) {
                foundNonLowImpact = true;
            }
        }
        output.put("command", "appStabilityReport");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());

        ObjectNode reportNode = mapper.createObjectNode();
        reportNode.put("totalOpenTickets", cntOpenTickets);
        reportNode.set("openTicketsByType", mapper.valueToTree(ticketsByType));
        reportNode.set("openTicketsByPriority", mapper.valueToTree(ticketsByPriority));

        ObjectNode riskByType = mapper.createObjectNode();
        for (Map.Entry<TicketType, Double> entry : averageRisk.entrySet()) {
            TicketType ticketType = entry.getKey();
            double riskScore = entry.getValue();
            String riskCategory = this.getRiskCategory(riskScore);
            riskByType.put(ticketType.toString(), riskCategory);
        }
        reportNode.set("riskByType", riskByType);

        ObjectNode impactByType = mapper.createObjectNode();
        for (Map.Entry<TicketType, Double> entry : averageImpact.entrySet()) {
            TicketType ticketType = entry.getKey();
            double impactScore = entry.getValue();
            double roundedScore = impactReport.round(impactScore);
            impactByType.put(ticketType.toString(), roundedScore);
        }
        reportNode.set("impactByType", impactByType);
        String appStability = "PARTIALLY STABLE";
        if (cntOpenTickets == 0) {
            appStability = "STABLE";
        } else if (!foundNonNegligible && !foundNonLowImpact) {
            appStability = "STABLE";
        } else if (foundSignificantRisk) {
            appStability = "UNSTABLE";
        }
        reportNode.put("appStability", appStability);
        output.set("report", reportNode);
        if(appStability.equals("STABLE")) {
            appStorage.setAppPhase(AppPhase.CLOSED);
        }
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
