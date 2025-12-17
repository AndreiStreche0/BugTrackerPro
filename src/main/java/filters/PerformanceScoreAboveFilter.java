package filters;

import users.Developer;

/**
 * filter used to check if the developer performance score is above the given one
 */
public class PerformanceScoreAboveFilter implements Filter<Developer> {
    private final double performanceScore;

    public PerformanceScoreAboveFilter(final double performanceScore) {
        this.performanceScore = performanceScore;
    }
    /**
     * checks if the developer performance score is above the given one
     * @param developer the developer that is being filtered
     * @return true if the developer passed the filter
     */
    @Override
    public boolean filter(final Developer developer) {
        return developer.getPerformanceScore() >= this.performanceScore;

    }
}
