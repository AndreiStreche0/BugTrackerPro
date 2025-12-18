package metrics;

import enums.TicketType;
import tickets.Ticket;

import java.util.List;

/**
 * interface containing all the methods necessary for metric calculation
 */
public interface MetricBaseMethods {
    double ROUNDER = 100.0;

    /**
     * @param ticket the ticket
     * @return if the ticket is valid for that metric calculation
     */
    boolean isTicketValid(Ticket ticket);

    /**
     * @param ticket the ticket used
     * @return base score for metric calculation
     */
    double calculateBaseScore(Ticket ticket);

    /**
     * @param type ticket type
     * @return the max value for that tickey type
     */
    double getMaxValue(TicketType type);

    /**
     * @param baseScore the base value for that metric
     * @param maxValue the max value for that metric
     * @return the normalized value
     */
    default double normalizeScore(final double baseScore, final double maxValue) {
        if (maxValue == 0) {
            return 0.0;
        }
        return Math.min(ROUNDER, (baseScore * ROUNDER) / maxValue);
    }

    /**
     * @param scores list of scores
     * @return their average
     */
    default double calculateAverageScore(final List<Double> scores) {
        return scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    /**
     * @param value the value
     * @return the value rounded out to 2 decimals
     */
    default double round(final double value) {
        return Math.round(value * ROUNDER) / ROUNDER;
    }
}
