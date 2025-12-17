package filters;

import tickets.Ticket;

import java.time.LocalDate;

/**
 * filter used to check if the creation date is at given date
 */
public class CreatedAtFilter implements Filter <Ticket> {
    private final LocalDate wantedDate;

    public CreatedAtFilter(final String wantedDateString) {
        this.wantedDate = LocalDate.parse(wantedDateString);
    }
    /**
     * check if the creation date is at given date
     * @param ticket the ticket that is being filtered
     * @return true if the ticket passed the filter
     */
    @Override
    public boolean filter(final Ticket ticket) {
        LocalDate ticketDate = LocalDate.parse(ticket.getCreatedAt());
        return ticketDate.isEqual(wantedDate);
    }
}
