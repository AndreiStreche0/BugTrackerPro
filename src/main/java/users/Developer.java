package users;

import enums.ExpertiseArea;
import enums.Seniority;
import enums.UserRole;
import tickets.Ticket;

import java.util.ArrayList;
import java.util.List;

public class Developer extends User {
    private ExpertiseArea expertise;
    private Seniority seniority;
    private String hireDate;
    private List<Ticket> assignedTickets;
    public Developer(String username, String email, ExpertiseArea expertise,
                     Seniority seniority, String hireDate) {
        super(username, email, UserRole.DEVELOPER);
        this.expertise = expertise;
        this.seniority = seniority;
        this.hireDate = hireDate;
        this.assignedTickets = new ArrayList<>();
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
}
