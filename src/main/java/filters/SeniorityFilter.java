package filters;

import enums.Seniority;
import users.Developer;

/**
 * filter to check if seniority matches
 */
public class SeniorityFilter implements Filter<Developer> {
    private final Seniority wantedSeniority;

    public SeniorityFilter(final Seniority wantedSeniority) {
        this.wantedSeniority = wantedSeniority;
    }
    /**
     * checks if seniority matches
     * @param developer the developer that is being filtered
     * @return true if the developer passed the filter
     */
    @Override
    public boolean filter(final Developer developer) {
        return developer.getSeniority() == wantedSeniority;
    }
}
