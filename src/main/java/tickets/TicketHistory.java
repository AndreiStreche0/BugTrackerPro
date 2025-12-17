package tickets;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Ticket history class that holds the needed fields and methods needed
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class TicketHistory {
    private final String action;
    private String from;
    private String to;
    private final String by;
    private final String timestamp;
    private String milestone;

    public TicketHistory(final String action, final String by, final String timestamp) {
        this.action = action;
        this.by = by;
        this.timestamp = timestamp;
    }

    /**
     * makes new TicketHistory object needed for ticket history list logic
     * @param by the user name of whom de-assigned the ticket
     * @param timestamp timestamp of assign
     * @return ticket history object
     */
    public static TicketHistory assign(final String by, final String timestamp) {
        return new TicketHistory("ASSIGNED", by, timestamp);
    }
    /**
     * makes new TicketHistory object needed for ticket history list logic
     * @param by the user name of whom de-assigned the ticket
     * @param timestamp timestamp of assign
     * @return ticket history object
     */
    public static TicketHistory deAssign(final String by, final String timestamp) {
        return new TicketHistory("DE-ASSIGNED", by, timestamp);
    }
    /**
     * makes new TicketHistory object needed for ticket history list logic
     * @param by the user name of whom changed the status the ticket
     * @param from the former status
     * @param to the new status
     * @param timestamp timestamp of status change
     * @return ticket history object
     */
    public static TicketHistory statusChanged(final String from, final String to, final String by,
                                              final String timestamp) {
        TicketHistory history = new TicketHistory("STATUS_CHANGED", by, timestamp);
        history.from = from;
        history.to = to;
        return history;
    }
    /**
     * makes new TicketHistory object needed for ticket history list logic
     * @param by the user name of who added to the milestone
     * @param milestoneName name of the milestone
     * @param timestamp timestamp of change
     * @return ticket history object
     */
    public static TicketHistory addedToMilestone(final String milestoneName, final String by,
                                                 final String timestamp) {
        TicketHistory history = new TicketHistory("ADDED_TO_MILESTONE", by, timestamp);
        history.milestone = milestoneName;
        return history;
    }

    /**
     * makes new TicketHistory object needed for ticket history list logic
     * @param by username of user who removed the ticket from dev
     * @param timestamp timestamp of change
     * @return ticket history object
     */
    public static TicketHistory removedFromDev(final String by, final String timestamp) {
        return new TicketHistory("REMOVED_FROM_DEV", by, timestamp);
    }

    public String getAction() {
        return action;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getBy() {
        return by;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMilestone() {
        return milestone;
    }
}
