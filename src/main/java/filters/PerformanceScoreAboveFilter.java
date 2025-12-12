package filters;

import users.Developer;

public class PerformanceScoreAboveFilter implements Filter<Developer> {
    private double performanceScore;

    public PerformanceScoreAboveFilter(double performanceScore) {
        this.performanceScore = performanceScore;
    }
    @Override
    public boolean filter(Developer developer) {
        //TODO CHANGE
        return true;
    }
}
