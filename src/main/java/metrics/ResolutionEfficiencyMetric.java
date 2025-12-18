package metrics;

import enums.TicketStatus;
import enums.TicketType;
import tickets.Bug;
import tickets.FeatureRequest;
import tickets.Ticket;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import tickets.UIFeedback;

public final class ResolutionEfficiencyMetric implements MetricBaseMethods {
    public static final double MAX_VALUE_BUG = 70.0;
    public static final double MAX_VALUE_OTHER_TYPES = 20.0;
    public static final double BUG_MULTIPLIER = 10.0;
    /**
     * @param ticket ticket for the resolution efficiency metric
     * @return if the ticket is valid for the metric calculation
     */
    @Override
    public boolean isTicketValid(final Ticket ticket) {
        return ticket.getState() == TicketStatus.RESOLVED
                || ticket.getState() == TicketStatus.CLOSED;
    }
    /**
     * calculates base score for resolution efficiency report
     * @param ticket the ticket for metric
     * @return the base score
     */
    @Override
    public double calculateBaseScore(final Ticket ticket) {
        long daysToResolve = calculateDaysToResolve(ticket);
        if (daysToResolve == 0) {
            daysToResolve = 1;
        }

        switch (ticket.getType()) {
            case BUG:
                Bug bug = (Bug) ticket;
                int frequency = EnumToIntConverter.frequencyToInt(bug.getFrequency());
                int severity = EnumToIntConverter.severityToInt(bug.getSeverity());
                return ((frequency + severity) * BUG_MULTIPLIER) / daysToResolve;

            case FEATURE_REQUEST:
                FeatureRequest fr = (FeatureRequest) ticket;
                int businessValue = EnumToIntConverter.businessValueToInt(fr.getBusinessValue());
                int customerDemand = EnumToIntConverter.customerDemandToInt(fr.getCustomerDemand());
                return (businessValue + customerDemand) / (double) daysToResolve;

            case UI_FEEDBACK:
                UIFeedback ui = (UIFeedback) ticket;
                int uiBusinessValue = EnumToIntConverter.businessValueToInt(ui.getBusinessValue());
                return (ui.getUsabilityScore() + uiBusinessValue) / (double) daysToResolve;

            default:
                return 0;
        }
    }

    /**
     * @param ticket the ticket used for metric
     * @return how many days it took for it to be resolved
     */
    private long calculateDaysToResolve(final Ticket ticket) {
        if (ticket.getAssignedAt() == null || ticket.getSolvedAt() == null) {
            System.out.println("This ticket has not been resolved");
            return 1;
        }
        LocalDate assignedDate = LocalDate.parse(ticket.getAssignedAt());
        LocalDate solvedDate = LocalDate.parse(ticket.getSolvedAt());
        return ChronoUnit.DAYS.between(assignedDate, solvedDate) + 1;
    }
    /**
     * @param type the ticket type
     * @return max value used for report calculation
     */
    @Override
    public double getMaxValue(final TicketType type) {
        return switch (type) {
            case BUG -> MAX_VALUE_BUG;
            default -> MAX_VALUE_OTHER_TYPES;
        };
    }

}
