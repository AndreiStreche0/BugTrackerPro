package metrics;

import enums.TicketStatus;
import enums.TicketType;
import tickets.Bug;
import tickets.FeatureRequest;
import tickets.Ticket;
import tickets.UIFeedback;

public class TicketRiskMetric implements MetricBaseMethods{
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
                int frequency = EnumToIntConverter.frequencyToInt(bug.getFrequency());
                int severity = EnumToIntConverter.severityToInt(bug.getSeverity());
                return frequency * severity;

            case FEATURE_REQUEST:
                FeatureRequest fr = (FeatureRequest) ticket;
                int businessValue = EnumToIntConverter.businessValueToInt(fr.getBusinessValue());
                int customerDemand = EnumToIntConverter.customerDemandToInt(fr.getCustomerDemand());
                return businessValue + customerDemand;

            case UI_FEEDBACK:
                UIFeedback ui = (UIFeedback) ticket;
                int uiBusinessValue = EnumToIntConverter.businessValueToInt(ui.getBusinessValue());
                return (11 - ui.getUsabilityScore()) * uiBusinessValue;

            default:
                return 0;
        }
    }

    @Override
    public double getMaxValue(TicketType type) {
        switch (type) {
            case BUG: return 12.0;
            case FEATURE_REQUEST: return 20.0;
            case UI_FEEDBACK: return 100.0;
            default: return 100.0;
        }
    }
}
