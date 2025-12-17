package filters;

import tickets.Ticket;

import java.time.LocalDate;

/**
 * filter used to check if the creation date is before given date
 */
public class CreatedBeforeFilter implements Filter<Ticket> {
    private final LocalDate wantedDate;

    public CreatedBeforeFilter(final String wantedDateString) {
        this.wantedDate = LocalDate.parse(wantedDateString);
    }
    /**
     * check if the creation date is before given date
     * @param ticket the ticket that is being filtered
     * @return true if the ticket passed the filter
     */
    @Override
    public boolean filter(final Ticket ticket) {
        LocalDate ticketDate = LocalDate.parse(ticket.getCreatedAt());
        return !ticketDate.isAfter(wantedDate);
    }
}
