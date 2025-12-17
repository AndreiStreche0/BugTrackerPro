package metrics;

import enums.BusinessPriority;
import enums.BusinessValue;
import enums.CustomerDemand;
import enums.Frequency;
import enums.Severity;

/**
 * converts enum values to int value based on report formulas
 */
public final class EnumToIntConverter {
    /**
     * @param value the business value
     * @return the int corespondent
     */
    public static int businessValueToInt(final BusinessValue value) {
        return switch (value) {
            case S -> 1;
            case M -> 3;
            case L -> 6;
            case XL -> 10;
        };
    }
    /**
     * @param frequency the frequency
     * @return the int corespondent
     */
    public static int frequencyToInt(final Frequency frequency) {
        return switch (frequency) {
            case RARE -> 1;
            case OCCASIONAL -> 2;
            case FREQUENT -> 3;
            case ALWAYS -> 4;
        };
    }
    /**
     * @param demand the customer demand
     * @return the int corespondent
     */
    public static int customerDemandToInt(CustomerDemand demand) {
        return switch (demand) {
            case LOW -> 2;
            case MEDIUM -> 3;
            case HIGH -> 6;
            case VERY_HIGH -> 8;
        };
    }
    /**
     * @param priority the priority
     * @return the int corespondent
     */
    public static int priorityToInt(BusinessPriority priority) {
        return switch (priority) {
            case LOW -> 1;
            case MEDIUM -> 2;
            case HIGH -> 3;
            case CRITICAL -> 4;
        };
    }
    /**
     * @param severity the severity
     * @return the int corespondent
     */
    public static int severityToInt(Severity severity) {
        return switch (severity) {
            case MINOR -> 1;
            case MODERATE -> 2;
            case SEVERE -> 3;
        };
    }
}
