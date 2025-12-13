package metrics;

import enums.BusinessPriority;
import enums.BusinessValue;
import enums.CustomerDemand;
import enums.Frequency;
import enums.Severity;

public class EnumToIntConverter {
    public static int businessValueToInt(BusinessValue value) {
        return switch (value) {
            case S -> 1;
            case M -> 3;
            case L -> 6;
            case XL -> 10;
        };
    }
    public static int frequencyToInt(Frequency frequency) {
        return switch (frequency) {
            case RARE -> 1;
            case OCCASIONAL -> 2;
            case FREQUENT -> 3;
            case ALWAYS -> 4;
        };
    }
    public static int customerDemandToInt(CustomerDemand demand) {
        return switch (demand) {
            case LOW -> 2;
            case MEDIUM -> 3;
            case HIGH -> 6;
            case VERY_HIGH -> 8;
        };
    }
    public static int priorityToInt(BusinessPriority priority) {
        return switch (priority) {
            case LOW -> 1;
            case MEDIUM -> 2;
            case HIGH -> 3;
            case CRITICAL -> 4;
        };
    }
    public static int severityToInt(Severity severity) {
        return switch (severity) {
            case MINOR -> 1;
            case MODERATE -> 2;
            case SEVERE -> 3;
        };
    }
}
