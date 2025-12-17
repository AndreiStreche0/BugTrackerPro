package filters;

import tickets.Ticket;

import java.time.LocalDate;

/**
 * filter used to check if the creation date is after given date
 */
public class CreatedAfterFilter implements Filter <Ticket> {
    private final LocalDate wantedDate;

    public CreatedAfterFilter(final String wantedDateString) {
        this.wantedDate = LocalDate.parse(wantedDateString);
    }
    /**
     * checks if the ticket was created after the given date
     * @param ticket the ticket that is being filtered
     * @return true if the ticket passed the filter
     */
    @Override
    public boolean filter(final Ticket ticket) {
        LocalDate ticketDate = LocalDate.parse(ticket.getCreatedAt());
        return !ticketDate.isBefore(wantedDate);
    }
}
