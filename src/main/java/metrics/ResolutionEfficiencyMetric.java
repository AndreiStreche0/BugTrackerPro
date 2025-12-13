package metrics;

import enums.TicketStatus;
import enums.TicketType;
import tickets.Bug;
import tickets.FeatureRequest;
import tickets.Ticket;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import enums.TicketType;
import tickets.UIFeedback;

public class ResolutionEfficiencyMetric implements MetricBaseMethods {
    @Override
    public boolean isTicketValid(Ticket ticket) {
        return ticket.getState() == TicketStatus.RESOLVED ||
                ticket.getState() == TicketStatus.CLOSED;
    }

    @Override
    public double calculateBaseScore(Ticket ticket) {
        long daysToResolve = calculateDaysToResolve(ticket);
        if (daysToResolve == 0) daysToResolve = 1; // Avoid division by zero

        switch (ticket.getType()) {
            case BUG:
                Bug bug = (Bug) ticket;
                int frequency = EnumToIntConverter.frequencyToInt(bug.getFrequency());
                int severity = EnumToIntConverter.severityToInt(bug.getSeverity());
                return ((frequency + severity) * 10.0) / daysToResolve;

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

    private long calculateDaysToResolve(Ticket ticket) {
        if (ticket.getAssignedAt() == null || ticket.getSolvedAt() == null) {
            return 1;
        }
        LocalDate assignedDate = LocalDate.parse(ticket.getAssignedAt());
        LocalDate solvedDate = LocalDate.parse(ticket.getSolvedAt());
        return ChronoUnit.DAYS.between(assignedDate, solvedDate) + 1;
    }

    @Override
    public double getMaxValue(TicketType type) {
        return switch (type) {
            case BUG -> 70.0;
            case FEATURE_REQUEST, UI_FEEDBACK -> 20.0;
            default -> 100.0;
        };
    }

}
