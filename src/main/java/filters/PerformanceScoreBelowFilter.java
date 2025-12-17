package filters;

import users.Developer;

/**
 *  filter that checks if the developer performance score is below the given one
 */
public class PerformanceScoreBelowFilter implements Filter<Developer> {
    private final double performanceScore;

    public PerformanceScoreBelowFilter(final double performanceScore) {
        this.performanceScore = performanceScore;
    }
    /**
     * checks if the developer performance score is below the given one
     * @param developer the ticket that is being filtered
     * @return true if the developer passed the filter
     */
    @Override
    public boolean filter(final Developer developer) {
        return developer.getPerformanceScore() < this.performanceScore;
    }
}
