package fileio;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class CommandInput {
    private String command;
    private String username;
    private String timestamp;
    private Integer ticketId;
    private Integer ticketID;
    private String name;
    private String dueDate;
    private List<String> blockingFor;
    private List<Integer> tickets;
    private List<String> assignedDevs;
    private Map<String, Object> params;
    private Map<String, Object> filters;
    private String comment;
    private String status;
    public Integer getTicketId() {
        if (ticketId != null) {
            return ticketId;
        }
        if (ticketID != null) {
            return ticketID;
        }
        return null;
    }

    public String getCommand() {
        return command;
    }

    public String getUsername() {
        return username;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getName() {
        return name;
    }

    public List<String> getBlockingFor() {
        return blockingFor;
    }

    public String getDueDate() {
        return dueDate;
    }

    public List<String> getAssignedDevs() {
        return assignedDevs;
    }

    public List<Integer> getTickets() {
        return tickets;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public String getComment() {
        return comment;
    }

    public String getStatus() {
        return status;
    }
}