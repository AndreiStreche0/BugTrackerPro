package users;

import enums.BusinessPriority;
import enums.ExpertiseArea;
import enums.Seniority;
import enums.UserRole;
import notifications.Observer;
import tickets.Ticket;

import java.util.ArrayList;
import java.util.List;

public class Developer extends User implements Observer {
    private ExpertiseArea expertise;
    private Seniority seniority;
    private String hireDate;
    private List<Ticket> assignedTickets;
    private List<String> notifications;
    private double performanceScore;

    public Developer(String username, String email, ExpertiseArea expertise,
                     Seniority seniority, String hireDate) {
        super(username, email, UserRole.DEVELOPER);
        this.expertise = expertise;
        this.seniority = seniority;
        this.hireDate = hireDate;
        this.assignedTickets = new ArrayList<>();
        this.notifications = new ArrayList<>();
        this.performanceScore = 0.0;
    }

    public void addAssignedTicket(Ticket assignedTicket) {
        this.assignedTickets.add(assignedTicket);
    }
    public void removeAssignedTicket(Ticket assignedTicket) {
        this.assignedTickets.remove(assignedTicket);
    }

    public List<Ticket> getAssignedTickets() {
        return assignedTickets;
    }

    public String getHireDate() {
        return hireDate;
    }

    public double getPerformanceScore() {
        return performanceScore;
    }

    public void setPerformanceScore(double performanceScore) {
        this.performanceScore = performanceScore;
    }

    public ExpertiseArea getExpertise() {
        return expertise;
    }
    public Seniority getSeniority() {
        return seniority;
    }
    public List<String> getNotifications() {
        return notifications;
    }
    @Override
    public void update(String notification) {
        this.notifications.add(notification);
    }
    public boolean hasRequiredExpertise(ExpertiseArea ticketArea) {
        if (this.expertise == ExpertiseArea.FULLSTACK) {
            return true;
        }
        return switch (this.expertise) {
            case FRONTEND -> ticketArea == ExpertiseArea.FRONTEND || ticketArea == ExpertiseArea.DESIGN;
            case BACKEND -> ticketArea == ExpertiseArea.BACKEND || ticketArea == ExpertiseArea.DB;
            case DEVOPS -> ticketArea == ExpertiseArea.DEVOPS;
            case DESIGN -> ticketArea == ExpertiseArea.DESIGN || ticketArea == ExpertiseArea.FRONTEND;
            case DB -> ticketArea == ExpertiseArea.DB;
            default -> false;
        };
    }
    public boolean hasRequiredSeniority (BusinessPriority ticketPriority) {
        return switch (this.seniority) {
            case Seniority.SENIOR -> true;
            case Seniority.MID -> ticketPriority != BusinessPriority.CRITICAL;
            case Seniority.JUNIOR -> ticketPriority == BusinessPriority.LOW ||
                    ticketPriority == BusinessPriority.MEDIUM;
            default -> false;
        };
    }
    public void calculatePerformanceScore() {
        this.performanceScore = 0.0;
    }
}
