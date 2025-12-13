package metrics;

import enums.*;
import tickets.FeatureRequest;
import tickets.Ticket;
import tickets.Bug;
import tickets.UIFeedback;

public class CustomerImpactMetric implements MetricBaseMethods{
    @Override
    public boolean isTicketValid(Ticket ticket) {
        return ticket.getState() == TicketStatus.OPEN ||
                ticket.getState() == TicketStatus.IN_PROGRESS;
    }

    @Override
    public double calculateBaseScore(Ticket ticket) {
        switch (ticket.getType()) {
            case BUG:
                Bug bug = (Bug) ticket;
                int frequencyValue = EnumToIntConverter.frequencyToInt(bug.getFrequency());
                int priorityValue = EnumToIntConverter.priorityToInt(ticket.getBusinessPriority());
                int severityValue = EnumToIntConverter.severityToInt(bug.getSeverity());
                return frequencyValue * priorityValue * severityValue;

            case FEATURE_REQUEST:
                FeatureRequest fr = (FeatureRequest) ticket;
                int businessValue = EnumToIntConverter.businessValueToInt(fr.getBusinessValue());
                int customerDemand = EnumToIntConverter.customerDemandToInt(fr.getCustomerDemand());
                return businessValue * customerDemand;

            case UI_FEEDBACK:
                UIFeedback ui = (UIFeedback) ticket;
                int uiBusinessValue = EnumToIntConverter.businessValueToInt(ui.getBusinessValue());
                return uiBusinessValue * ui.getUsabilityScore();

            default:
                return 0;
        }
    }
    @Override
    public double getMaxValue(TicketType type) {
        return switch (type) {
            case BUG -> 48.0;
            case FEATURE_REQUEST -> 100.0;
            case UI_FEEDBACK -> 100.0;
            default -> 100.0;
        };
    }
}
