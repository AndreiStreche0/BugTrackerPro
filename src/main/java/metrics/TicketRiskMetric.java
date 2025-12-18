package metrics;

import enums.TicketStatus;
import enums.TicketType;
import tickets.Bug;
import tickets.FeatureRequest;
import tickets.Ticket;
import tickets.UIFeedback;

public final class TicketRiskMetric implements MetricBaseMethods {
    public static final double MAX_VALUE_BUG = 12.0;
    public static final double MAX_VALUE_FEATURE_REQUEST = 20.0;
    public static final double MAX_VALUE_OTHER_TYPES = 100.0;
    public static final int USABILITY_SCORE_REFERENCE = 11;

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
     * calculates base score for ticket risk report
     * @param ticket the ticket for metric
     * @return the base score
     */
    @Override
    public double calculateBaseScore(final Ticket ticket) {
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
                return (USABILITY_SCORE_REFERENCE - ui.getUsabilityScore()) * uiBusinessValue;

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
            case FEATURE_REQUEST -> MAX_VALUE_FEATURE_REQUEST;
            default -> MAX_VALUE_OTHER_TYPES;
        };
    }
}
