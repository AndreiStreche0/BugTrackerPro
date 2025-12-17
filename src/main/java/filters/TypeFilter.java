package filters;

import enums.TicketType;
import tickets.Ticket;

/**
 * filter used to check if the ticket type matches
 */
public class TypeFilter implements Filter<Ticket> {
    private final TicketType wantedType;

    public TypeFilter(final TicketType wantedType) {
        this.wantedType = wantedType;
    }
    /**
     * checks if the type matches
     * @param ticket the ticket that is being filtered
     * @return true if the ticket passed the filter
     */
    @Override
    public boolean filter(final Ticket ticket) {
        return ticket.getType() == wantedType;
    }
}
