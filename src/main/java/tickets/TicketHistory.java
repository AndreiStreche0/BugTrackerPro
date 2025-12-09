package tickets;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketHistory {
    private String action;
    private String from;
    private String to;
    private String by;
    private String timestamp;
    private String milestone;

    public TicketHistory(String action, String by, String timestamp) {
        this.action = action;
        this.by = by;
        this.timestamp = timestamp;
    }
    public static TicketHistory assigned(String by, String timestamp) {
        return new TicketHistory("ASSIGNED", by, timestamp);
    }

    public static TicketHistory deAssigned(String by, String timestamp) {
        return new TicketHistory("DE-ASSIGNED", by, timestamp);
    }
    public static TicketHistory statusChanged(String from, String to, String by, String timestamp) {
        TicketHistory history = new TicketHistory("STATUS_CHANGED", by, timestamp);
        history.from = from;
        history.to = to;
        return history;
    }
    public static TicketHistory addedToMilestone(String milestoneName, String by, String timestamp) {
        TicketHistory history = new TicketHistory("ADDED_TO_MILESTONE", by, timestamp);
        history.milestone = milestoneName;
        return history;
    }
    public static TicketHistory removedFromDev(String by, String timestamp) {
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
