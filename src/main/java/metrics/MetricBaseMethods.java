package metrics;

import enums.TicketType;
import tickets.Ticket;

import java.util.List;

public interface MetricBaseMethods {
    boolean isTicketValid(Ticket ticket);
    double calculateBaseScore(Ticket ticket);
    double getMaxValue(TicketType type);
    default double normalizeScore(double baseScore, double maxValue) {
        if (maxValue == 0) return 0.0;
        return Math.min(100.0, (baseScore * 100.0) / maxValue);
    }
    default double calculateAverageScore(List<Double> scores) {
        return scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
    default double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
