package filters;

import enums.Seniority;
import tickets.Ticket;
import users.Developer;

public class SeniorityFilter implements Filter<Developer> {
    private final Seniority wantedSeniority;

    public SeniorityFilter(Seniority wantedSeniority) {
        this.wantedSeniority = wantedSeniority;
    }
    @Override
    public boolean filter(Developer developer) {
        return developer.getSeniority() == wantedSeniority;
    }
}
