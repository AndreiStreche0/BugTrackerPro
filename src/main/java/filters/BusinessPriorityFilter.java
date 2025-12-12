package filters;

import enums.BusinessPriority;
import tickets.Ticket;

public class BusinessPriorityFilter implements Filter <Ticket>{
    private final BusinessPriority wantedPriority;

    public BusinessPriorityFilter(BusinessPriority wantedPriority) {
        this.wantedPriority = wantedPriority;
    }

    @Override
    public boolean filter(Ticket ticket) {
        return ticket.getBusinessPriority() == wantedPriority;
    }
}
