package filters;

import enums.ExpertiseArea;
import users.Developer;

/**
 * filter to check if the expertise area matches with the given one
 */
public class ExpertiseAreaFilter implements Filter <Developer> {
    private final ExpertiseArea expertiseArea;

    public ExpertiseAreaFilter(final String expertiseAreaString) {
        this.expertiseArea = ExpertiseArea.valueOf(expertiseAreaString);
    }
    /**
     * checks if the expertise area matches
     * @param developer the developer that is being filtered
     * @return true if the developer passed the filter
     */
    @Override
    public boolean filter(final Developer developer) {
        return developer.getExpertise() == expertiseArea;
    }
}
