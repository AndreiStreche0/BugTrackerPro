package tickets;
import enums.ExpertiseArea;
import enums.BusinessPriority;
import enums.TicketStatus;
import enums.TicketType;
import enums.Seniority;
import processing.Comment;
import users.User;

import java.util.ArrayList;
import java.util.List;

/**
 * abstract ticket class that holds all the common fields and methods
 */
public abstract class Ticket {
    private int id;
    private String title;
    private String description;
    private BusinessPriority businessPriority;
    private ExpertiseArea expertiseArea;
    private String createdAt;
    private String updatedAt;
    private String reportedBy;
    private String solvedAt;
    private String closedAt;
    private String assignedAt;
    private User assignedTo;
    private TicketStatus state;
    private TicketType type;

    private List<Comment> comments = new ArrayList<>();
    private final List<TicketHistory> history = new ArrayList<>();
    public Ticket(final String createdAt, final String updatedAt) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    /**
     * @return close date for ticket
     */
    public String getClosedAt() {
        return closedAt;
    }
    /**
     * modifies closing date
     */
    public void setClosedAt(final String closedAt) {
        this.closedAt = closedAt;
    }
    /**
     * @return date of ticket solving
     */
    public String getSolvedAt() {
        return solvedAt;
    }
    /**
     * @return date of assigning
     */
    public String getAssignedAt() {
        return assignedAt;
    }

    /**
     * changes assigning date
     * @param assignedAt new date
     */
    public void setAssignedAt(final String assignedAt) {
        this.assignedAt = assignedAt;
    }

    /**
     * changes solving date
     * @param solvedAt new solving date
     */
    public void setSolvedAt(final String solvedAt) {
        this.solvedAt = solvedAt;
    }

    /**
     * changes comment list
     * @param comments new comment list
     */
    public void setComments(final List<Comment> comments) {
        this.comments = comments;
    }
    /**
     * @return ticket id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id new ticket id
     */
    public void setId(final int id) {
        this.id = id;
    }
    /**
     * @return ticket title
     */
    public String getTitle() {
        return title;
    }

    /**
     * changes ticket title
     * @param title new title
     */
    public void setTitle(final String title) {
        this.title = title;
    }
    /**
     * @return ticket description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @return assignment username
     */
    public User getAssignedTo() {
        return assignedTo;
    }

    /**
     * @param assignedTo changes who the ticket is assigned to
     */
    public void setAssignedTo(final User assignedTo) {
        this.assignedTo = assignedTo;
    }

    /**
     * @param description the new description
     */
    public void setDescription(final String description) {
        this.description = description;
    }
    /**
     * @return ticket's business priority
     */
    public BusinessPriority getBusinessPriority() {
        return businessPriority;
    }
    /**
     * changes to new business priority
     */
    public void setBusinessPriority(final BusinessPriority businessPriority) {
        this.businessPriority = businessPriority;
    }
    /**
     * @return ticket's expertise area
     */
    public ExpertiseArea getExpertiseArea() {
        return expertiseArea;
    }

    /**
     * @param expertiseArea changes expertise area
     */
    public void setExpertiseArea(final ExpertiseArea expertiseArea) {
        this.expertiseArea = expertiseArea;
    }
    /**
     * @return ticket's creation date
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * changes ticket's creation time
     * @param createdAt new creation time
     */
    public void setCreatedAt(final String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return ticket's last update date
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt new last update date
     */
    public void setUpdatedAt(final String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * get ticket's reporter
     * @return username
     */
    public String getReportedBy() {
        return reportedBy;
    }

    /**
     * changes ticket's reporter
     * @param reportedBy new reporter
     */
    public void setReportedBy(final String reportedBy) {
        this.reportedBy = reportedBy;
    }
    /**
     * @return ticket's state
     */
    public TicketStatus getState() {
        return state;
    }
    /**
     * changes ticket's state
     */
    public void setState(final TicketStatus state) {
        this.state = state;
    }
    /**
     * @return ticket's type
     */
    public TicketType getType() {
        return type;
    }

    /**
     * @param type new ticket type
     */
    public void setType(final TicketType type) {
        this.type = type;
    }

    /**
     * @return list of ticket's comments
     */
    public List<Comment> getComments() {
        return this.comments;
    }

    /**
     * @param comment new comment added to comment list
     */
    public void addComment(final Comment comment) {
        this.comments.add(comment);
    }

    /**
     * @param comment comment to be removed from comment list
     */
    public void removeComment(final Comment comment) {
        this.comments.remove(comment);
    }

    /**
     * @param historyItem adds new history to history list
     */
    public void addHistory(final TicketHistory historyItem) {
        this.history.add(historyItem);
    }

    /**
     * @return returns history list
     */
    public List<TicketHistory> getHistory() {
        return history;
    }

    /**
     * @param expertise the wanted expertise
     * @return if the ticket has the wanted expertise
     */
    public boolean checkExpertiseLevel(final ExpertiseArea expertise) {
        if (expertise == ExpertiseArea.FULLSTACK) {
            return true;
        }
        return switch (expertise) {
            case FRONTEND -> this.expertiseArea == ExpertiseArea.FRONTEND
                    || this.expertiseArea == ExpertiseArea.DESIGN;
            case BACKEND -> this.expertiseArea == ExpertiseArea.BACKEND
                    || this.expertiseArea == ExpertiseArea.DB;
            case DEVOPS -> this.expertiseArea == ExpertiseArea.DEVOPS;
            case DESIGN -> this.expertiseArea == ExpertiseArea.DESIGN
                    || this.expertiseArea == ExpertiseArea.FRONTEND;
            case DB -> this.expertiseArea == ExpertiseArea.DB;
            default -> false;
        };
    }

    /**
     * @param seniority the wanted seniority
     * @return checks if the given seniority is enough to solve the ticket
     */
    public boolean checkSeniority(final Seniority seniority) {
        return switch (seniority) {
            case SENIOR -> true;
            case MID -> this.businessPriority != BusinessPriority.CRITICAL;
            case JUNIOR -> this.businessPriority == BusinessPriority.LOW
                    || this.businessPriority == BusinessPriority.MEDIUM;
            default -> false;
        };
    }
}
