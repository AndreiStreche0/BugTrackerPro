package filters;

import enums.ExpertiseArea;
import users.Developer;

public class ExpertiseAreaFilter implements Filter <Developer> {
    private final ExpertiseArea experiseArea;

    public ExpertiseAreaFilter(String expertiseAreaString) {
        this.experiseArea = ExpertiseArea.valueOf(expertiseAreaString);
    }
    @Override
    public boolean filter(Developer developer) {
        return developer.getExpertise() == experiseArea;
    }
}
