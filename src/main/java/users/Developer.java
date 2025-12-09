package users;

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
    public Developer(String username, String email, ExpertiseArea expertise,
                     Seniority seniority, String hireDate) {
        super(username, email, UserRole.DEVELOPER);
        this.expertise = expertise;
        this.seniority = seniority;
        this.hireDate = hireDate;
        this.assignedTickets = new ArrayList<>();
        this.notifications = new ArrayList<>();
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
}
