package tickets;
import commands.Command;
import enums.*;

import processing.Comment;
import users.User;

import java.time.LocalDate;
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

    public String getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(final String closedAt) {
        this.closedAt = closedAt;
    }

    public String getSolvedAt() {
        return solvedAt;
    }

    public String getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(final String assignedAt) {
        this.assignedAt = assignedAt;
    }

    public void setSolvedAt(final String solvedAt) {
        this.solvedAt = solvedAt;
    }

    public void setComments(final List<Comment> comments) {
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(final User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public BusinessPriority getBusinessPriority() {
        return businessPriority;
    }

    public void setBusinessPriority(final BusinessPriority businessPriority) {
        this.businessPriority = businessPriority;
    }

    public ExpertiseArea getExpertiseArea() {
        return expertiseArea;
    }

    public void setExpertiseArea(final ExpertiseArea expertiseArea) {
        this.expertiseArea = expertiseArea;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(final String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(final String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public TicketStatus getState() {
        return state;
    }

    public void setState(TicketStatus state) {
        this.state = state;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(final TicketType type) {
        this.type = type;
    }

    public Ticket(final String createdAt, final String updatedAt) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public List<Comment> getComments() {
        return this.comments;
    }
    public void addComment(final Comment comment) {
        this.comments.add(comment);
    }
    public void removeComment(final Comment comment) {
        this.comments.remove(comment);
    }
    public void addHistory(final TicketHistory historyItem) {
        this.history.add(historyItem);
    }
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
            case FRONTEND -> this.expertiseArea == ExpertiseArea.FRONTEND || this.expertiseArea == ExpertiseArea.DESIGN;
            case BACKEND -> this.expertiseArea == ExpertiseArea.BACKEND || this.expertiseArea == ExpertiseArea.DB;
            case DEVOPS -> this.expertiseArea == ExpertiseArea.DEVOPS;
            case DESIGN -> this.expertiseArea == ExpertiseArea.DESIGN || this.expertiseArea == ExpertiseArea.FRONTEND;
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
            case JUNIOR -> this.businessPriority == BusinessPriority.LOW ||
                    this.businessPriority == BusinessPriority.MEDIUM;
            default -> false;
        };
    }
}
