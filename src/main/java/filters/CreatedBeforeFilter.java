package filters;

import tickets.Ticket;

import java.time.LocalDate;

public class CreatedBeforeFilter implements Filter<Ticket> {
    private final LocalDate wantedDate;

    public CreatedBeforeFilter(String wantedDateString) {
        this.wantedDate = LocalDate.parse(wantedDateString);
    }

    @Override
    public boolean filter(Ticket ticket) {
        LocalDate ticketDate = LocalDate.parse(ticket.getCreatedAt());
        return !ticketDate.isAfter(wantedDate);
    }
}
