package filters;

import tickets.Ticket;

import java.util.List;

public class KeywordsFilter implements Filter <Ticket> {
    private final List<String> keywords;

    public KeywordsFilter(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean filter(Ticket ticket) {
        if (keywords == null) {
            return true;
        }
        String description = "";
        for (String keyword : keywords) {
            if (ticket.getDescription() != null) {
                description = ticket.getDescription();
            }
            if (ticket.getTitle().toLowerCase().contains(keyword) ||
                    description.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}
