package filters;

import enums.BusinessPriority;
import tickets.Ticket;

/**
 * filters ticket based on business priority
 */
public class BusinessPriorityFilter implements Filter <Ticket>{
    private final BusinessPriority wantedPriority;

    public BusinessPriorityFilter(final BusinessPriority wantedPriority) {
        this.wantedPriority = wantedPriority;
    }
    /**
     * checks the business priority
     * @param ticket the ticket that is being filtered
     * @return true if the ticket passed the filter
     */
    @Override
    public boolean filter(final Ticket ticket) {
        return ticket.getBusinessPriority() == wantedPriority;
    }
}
