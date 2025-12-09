package processing;

import enums.AppPhase;
import factory.UserFactory;
import fileio.UserInput;
import main.App;
import milestones.Milestone;
import tickets.Ticket;
import users.Developer;
import users.User;

import java.util.ArrayList;
import java.util.List;

public class AppStorage {
    private final static AppStorage INSTANCE = new AppStorage();
    private final List<Ticket> tickets = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private AppPhase appPhase;
    private final List<Milestone> milestones = new ArrayList<>();
    private AppStorage() {}

    public static AppStorage getInstance() {
        return INSTANCE;
    }
    public void addTicket(Ticket ticket) {
        ticket.setId(tickets.size());
        tickets.add(ticket);
    }
    public void reset() {
        this.tickets.clear();
        this.users.clear();
        this.milestones.clear();
        this.appPhase = null;
    }

    public AppPhase getAppPhase() {
        return appPhase;
    }

    public Ticket getTicket(int id) {
        for (Ticket ticket : this.tickets) {
            if (ticket.getId() == id) {
                return ticket;
            }
        }
        return null;
    }
    public Milestone getMilestone(String milestoneName) {
        for (Milestone milestone : this.milestones) {
            if (milestone.getName().equals(milestoneName)) {
                return milestone;
            }
        }
        return null;
    }

    public void setAppPhase(AppPhase appPhase) {
        this.appPhase = appPhase;
    }

    public void loadUsers(List<UserInput> inputs) {
        for (UserInput input : inputs) {
            User newUser = UserFactory.createUser(input);
            if (newUser != null) {
                users.add(newUser);
            }
        }
    }
    public void addMilestone(Milestone milestone) {
        milestones.add(milestone);
    }

    public List<Milestone> getMilestones() {
        return milestones;
    }
    public Milestone isTicketInMilestone(int ticketId) {
        for (Milestone milestone : this.milestones) {
            if (milestone.getTicketIds().contains(ticketId)) {
                return milestone;
            }
        }
        return null;
    }
    public List<Ticket> getTickets() {
        return tickets;
    }
    public User findUser(String username) {
        for (User user : this.users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
