package factory;

import enums.BusinessPriority;
import enums.ExpertiseArea;
import enums.Seniority;
import enums.TicketType;
import filters.*;
import main.App;
import processing.AppStorage;
import tickets.Ticket;
import users.Developer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * filter factory used to create new filters for search project functions
 */
public class FilterFactory {
    /**
     * creates filters for tickets
     * @param filters all the filters to be created
     * @param developer the developer for which they get created or null if it s a manager
     * @return a list of filters
     */
    public static List<Filter<Ticket>> createTicketFilters(final Map<String, Object> filters,
                                                           final Developer developer) {
        List<Filter<Ticket>> filterList = new ArrayList<>();
        if (filters.containsKey("businessPriority")) {
            filterList.add(new BusinessPriorityFilter(BusinessPriority.valueOf((String)
                    filters.get("businessPriority"))));
        }
        if (filters.containsKey("type")) {
            filterList.add(new TypeFilter(TicketType.valueOf((String) filters.get("type"))));
        }
        if (filters.containsKey("createdAt")) {
            filterList.add(new CreatedAtFilter((String) filters.get("createdAt")));
        }
        if (filters.containsKey("createdBefore")) {
            filterList.add(new CreatedBeforeFilter((String) filters.get("createdBefore")));
        }
        if (filters.containsKey("createdAfter")) {
            filterList.add(new CreatedAfterFilter((String) filters.get("createdAfter")));
        }
        if (filters.containsKey("keywords")) {
            filterList.add(new KeywordsFilter((List<String>) filters.get("keywords")));
        }
        if (filters.containsKey("availableForAssignment")) {
            if (developer != null) {
                filterList.add(new AvailableForAssignmentFilter(developer));
            }
        }
        return filterList;
    }
    /**
     * creates filters for developers
     * @param filters all the filters to be created
     * @return a list of filters
     */
    public static List<Filter<Developer>> createDeveloperFilters(final Map<String, Object> filters) {
        List<Filter<Developer>> filterList = new ArrayList<>();
        if (filters.containsKey("expertiseArea")) {
            filterList.add(new ExpertiseAreaFilter((String) filters.get("expertiseArea")));
        }
        if (filters.containsKey("seniority")) {
            filterList.add(new SeniorityFilter(Seniority.valueOf((String) filters.get("seniority"))));
        }
        if (filters.containsKey("performanceScoreAbove")) {
            filterList.add(new PerformanceScoreAboveFilter(((double) filters.get("performanceScoreAbove"))));
        }
        if (filters.containsKey("performanceScoreBelow")) {
            filterList.add(new PerformanceScoreBelowFilter(((double) filters.get("performanceScoreBelow"))));

        }
        return filterList;
    }
}