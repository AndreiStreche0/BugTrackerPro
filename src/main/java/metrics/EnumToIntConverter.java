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
    public static final int BUSINESS_VALUE_S_VALUE = 1;
    public static final int BUSINESS_VALUE_M_VALUE = 3;
    public static final int BUSINESS_VALUE_L_VALUE = 6;
    public static final int BUSINESS_VALUE_XL_VALUE = 10;
    public static final int FREQUENCY_RARE_VALUE = 1;
    public static final int FREQUENCY_OCCASIONAL_VALUE = 2;
    public static final int FREQUENCY_FREQUENT_VALUE = 3;
    public static final int FREQUENCY_ALWAYS_VALUE = 4;
    public static final int CUSTOMER_DEMAND_LOW_VALUE = 2;
    public static final int CUSTOMER_DEMAND_MEDIUM_VALUE = 3;
    public static final int CUSTOMER_DEMAND_HIGH_VALUE = 6;
    public static final int CUSTOMER_DEMAND_VERY_HIGH_VALUE = 10;
    public static final int PRIORITY_LOW_VALUE = 1;
    public static final int PRIORITY_MEDIUM_VALUE = 2;
    public static final int PRIORITY_HIGH_VALUE = 3;
    public static final int PRIORITY_CRITICAL_VALUE = 4;
    public static final int SEVERITY_MINOR_VALUE = 1;
    public static final int SEVERITY_MODERATE_VALUE = 2;
    public static final int SEVERITY_SEVERE_VALUE = 3;
    private EnumToIntConverter() {
        throw new UnsupportedOperationException("This is a utility class and can't be"
                + " instantiated");
    }
    /**
     * @param value the business value
     * @return the int corespondent
     */
    public static int businessValueToInt(final BusinessValue value) {
        return switch (value) {
            case S -> BUSINESS_VALUE_S_VALUE;
            case M -> BUSINESS_VALUE_M_VALUE;
            case L -> BUSINESS_VALUE_L_VALUE;
            case XL -> BUSINESS_VALUE_XL_VALUE;
        };
    }
    /**
     * @param frequency the frequency
     * @return the int corespondent
     */
    public static int frequencyToInt(final Frequency frequency) {
        return switch (frequency) {
            case RARE -> FREQUENCY_RARE_VALUE;
            case OCCASIONAL -> FREQUENCY_OCCASIONAL_VALUE;
            case FREQUENT -> FREQUENCY_FREQUENT_VALUE;
            case ALWAYS -> FREQUENCY_ALWAYS_VALUE;
        };
    }
    /**
     * @param demand the customer demand
     * @return the int corespondent
     */
    public static int customerDemandToInt(final CustomerDemand demand) {
        return switch (demand) {
            case LOW -> CUSTOMER_DEMAND_LOW_VALUE;
            case MEDIUM -> CUSTOMER_DEMAND_MEDIUM_VALUE;
            case HIGH -> CUSTOMER_DEMAND_HIGH_VALUE;
            case VERY_HIGH -> CUSTOMER_DEMAND_VERY_HIGH_VALUE;
        };
    }
    /**
     * @param priority the priority
     * @return the int corespondent
     */
    public static int priorityToInt(final BusinessPriority priority) {
        return switch (priority) {
            case LOW -> PRIORITY_LOW_VALUE;
            case MEDIUM -> PRIORITY_MEDIUM_VALUE;
            case HIGH -> PRIORITY_HIGH_VALUE;
            case CRITICAL -> PRIORITY_CRITICAL_VALUE;
        };
    }
    /**
     * @param severity the severity
     * @return the int corespondent
     */
    public static int severityToInt(final Severity severity) {
        return switch (severity) {
            case MINOR -> SEVERITY_MINOR_VALUE;
            case MODERATE -> SEVERITY_MODERATE_VALUE;
            case SEVERE -> SEVERITY_SEVERE_VALUE;
        };
    }
}
