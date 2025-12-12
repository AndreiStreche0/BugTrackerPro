package filters;

import enums.TicketType;
import tickets.Ticket;

public class TypeFilter implements Filter<Ticket> {
    private final TicketType wantedType;

    public TypeFilter(TicketType wantedType) {
        this.wantedType = wantedType;
    }

    @Override
    public boolean filter(Ticket ticket) {
        return ticket.getType() == wantedType;
    }
}
