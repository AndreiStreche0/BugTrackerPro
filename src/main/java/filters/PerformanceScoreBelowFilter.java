package filters;

import users.Developer;

public class PerformanceScoreBelowFilter implements Filter<Developer> {
    private double performanceScore;

    public PerformanceScoreBelowFilter(double performanceScore) {
        this.performanceScore = performanceScore;
    }
    @Override
    public boolean filter(Developer developer) {
        //TODO CHANGE
        return true;
    }
}
