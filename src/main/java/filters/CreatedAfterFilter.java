package filters;

import tickets.Ticket;

import java.time.LocalDate;

public class CreatedAfterFilter implements Filter <Ticket> {
    private final LocalDate wantedDate;

    public CreatedAfterFilter(String wantedDateString) {
        this.wantedDate = LocalDate.parse(wantedDateString);
    }

    @Override
    public boolean filter(Ticket ticket) {
        LocalDate ticketDate = LocalDate.parse(ticket.getCreatedAt());
        return !ticketDate.isBefore(wantedDate);
    }
}
