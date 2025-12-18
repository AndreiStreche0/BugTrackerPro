package filters;

import tickets.Ticket;

import java.util.List;

/**
 * checks if the keywords match with the ticket
 */
public class KeywordsFilter implements Filter<Ticket> {
    private final List<String> keywords;

    public KeywordsFilter(final List<String> keywords) {
        this.keywords = keywords;
    }
    /**
     * checks if any keyword matches with the ticket description or title
     * @param ticket the ticket that is being filtered
     * @return true if the ticket passed the filter
     */
    @Override
    public boolean filter(final Ticket ticket) {
        if (keywords == null) {
            return true;
        }
        String description = "";
        for (String keyword : keywords) {
            if (ticket.getDescription() != null) {
                description = ticket.getDescription();
            }
            if (ticket.getTitle().toLowerCase().contains(keyword)
                    || description.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}
