package metrics;

import enums.TicketType;
import enums.TicketStatus;
import tickets.FeatureRequest;
import tickets.Ticket;
import tickets.Bug;
import tickets.UIFeedback;

/**
 * class that calculates the customer impact metric for a given ticket
 */
public class CustomerImpactMetric implements MetricBaseMethods {
    public static final double MAX_VALUE_BUG = 48.0;
    public static final double MAX_VALUE_OTHER_TYPES = 100.0;

    /**
     * @param ticket ticket for the metric
     * @return if the ticket is valid for the metric calculation
     */
    @Override
    public boolean isTicketValid(final Ticket ticket) {
        return ticket.getState() == TicketStatus.OPEN
                || ticket.getState() == TicketStatus.IN_PROGRESS;
    }

    /**
     * calculates base score for report
     * @param ticket the ticket for metric
     * @return the base score
     */
    @Override
    public double calculateBaseScore(final Ticket ticket) {
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
    /**
     * @param type the ticket type
     * @return max value used for report calculation
     */
    @Override
    public double getMaxValue(final TicketType type) {
        return switch (type) {
            case BUG -> MAX_VALUE_BUG;
            case UI_FEEDBACK, FEATURE_REQUEST -> MAX_VALUE_OTHER_TYPES;
            default -> 1.0;
        };
    }
}
