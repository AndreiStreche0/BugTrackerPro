package users;

import enums.BusinessPriority;
import enums.ExpertiseArea;
import enums.Seniority;
import enums.UserRole;
import notifications.Observer;
import tickets.Ticket;

import java.util.ArrayList;
import java.util.List;

public final class Developer extends User implements Observer {
    private final ExpertiseArea expertise;
    private final Seniority seniority;
    private final String hireDate;
    private final List<Ticket> assignedTickets;
    private final List<String> notifications;
    private double performanceScore;

    public Developer(final String username, final String email, final ExpertiseArea expertise,
                     final Seniority seniority, final String hireDate) {
        super(username, email, UserRole.DEVELOPER);
        this.expertise = expertise;
        this.seniority = seniority;
        this.hireDate = hireDate;
        this.assignedTickets = new ArrayList<>();
        this.notifications = new ArrayList<>();
        this.performanceScore = 0.0;
    }

    /**
     * adds ticket to the assigned list
     * @param assignedTicket the ticket
     */
    public void addAssignedTicket(Ticket assignedTicket) {
        this.assignedTickets.add(assignedTicket);
    }

    /**
     * removes ticket from the assigned ticket list
     * @param assignedTicket the ticket
     */
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

    /**
     * adds new notification to notification list
     * @param notification the notification being sent
     */
    @Override
    public void update(String notification) {
        this.notifications.add(notification);
    }

    /**
     * removes all notifications
     */
    public void clearNotifications() {
        this.notifications.clear();
    }

    /**
     * checks if the developer has the required expertise for the given expertiseArea of a ticket
     * @param ticketArea the expertiseArea
     * @return true if they do
     */
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
    /**
     * checks if the developer has the required seniority for the given priority of a ticket
     * @param ticketPriority the priority
     * @return true if they do
     */
    public boolean hasRequiredSeniority (BusinessPriority ticketPriority) {
        return switch (this.seniority) {
            case Seniority.SENIOR -> true;
            case Seniority.MID -> ticketPriority != BusinessPriority.CRITICAL;
            case Seniority.JUNIOR -> ticketPriority == BusinessPriority.LOW ||
                    ticketPriority == BusinessPriority.MEDIUM;
            default -> false;
        };
    }
}
