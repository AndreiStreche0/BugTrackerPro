package tickets;
import commands.Command;
import enums.BusinessPriority;

import enums.ExpertiseArea;
import enums.TicketStatus;
import enums.TicketType;
import processing.Comment;
import users.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private String assignedAt;
    private User assignedTo;
    private TicketStatus state;
    private TicketType type;

    private List<Comment> comments = new ArrayList<>();

    public String getSolvedAt() {
        return solvedAt;
    }

    public String getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(String assignedAt) {
        this.assignedAt = assignedAt;
    }

    public void setSolvedAt(String solvedAt) {
        this.solvedAt = solvedAt;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BusinessPriority getBusinessPriority() {
        return businessPriority;
    }

    public void setBusinessPriority(BusinessPriority businessPriority) {
        this.businessPriority = businessPriority;
    }

    public ExpertiseArea getExpertiseArea() {
        return expertiseArea;
    }

    public void setExpertiseArea(ExpertiseArea expertiseArea) {
        this.expertiseArea = expertiseArea;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
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

    public void setType(TicketType type) {
        this.type = type;
    }

    public Ticket(String createdAt, String updatedAt) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public List<Comment> getComments() {
        return this.comments;
    }
    public void addComment(Comment comment) {
        this.comments.add(comment);
    }
    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }
}
