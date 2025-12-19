package processing;

import enums.AppPhase;
import factories.UserFactory;
import fileio.UserInput;
import milestones.Milestone;
import tickets.Ticket;
import users.User;

import java.util.ArrayList;
import java.util.List;

/**
 * class that holds all the app storage and methods for finding
 */
public final class AppStorage {
    private static final AppStorage INSTANCE = new AppStorage();
    private final List<Ticket> tickets = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private AppPhase appPhase;
    private final List<Milestone> milestones = new ArrayList<>();
    private AppStorage() {

    }

    public static AppStorage getInstance() {
        return INSTANCE;
    }

    /**
     * adds a new ticket to the ticket list
     * @param ticket the ticket
     */
    public void addTicket(final Ticket ticket) {
        ticket.setId(tickets.size());
        tickets.add(ticket);
    }
    public void removeTicket(final Ticket ticket) {
        tickets.remove(ticket);
    }
    /**
     * resets the app storage
     */
    public void reset() {
        this.tickets.clear();
        this.users.clear();
        this.milestones.clear();
        this.appPhase = null;
    }
    public AppPhase getAppPhase() {
        return appPhase;
    }

    /**
     * @param id ticket id
     * @return the ticket with given id
     */
    public Ticket getTicket(final int id) {
        for (Ticket ticket : this.tickets) {
            if (ticket.getId() == id) {
                return ticket;
            }
        }
        return null;
    }

    /**
     * @param milestoneName the milestone name
     * @return the milestone with the given name
     */
    public Milestone getMilestone(final String milestoneName) {
        for (Milestone milestone : this.milestones) {
            if (milestone.getName().equals(milestoneName)) {
                return milestone;
            }
        }
        return null;
    }

    public void setAppPhase(final AppPhase appPhase) {
        this.appPhase = appPhase;
    }

    /**
     * loads all the users based on the user input given
     * @param inputs user input list
     */
    public void loadUsers(final List<UserInput> inputs) {
        for (UserInput input : inputs) {
            User newUser = UserFactory.createUser(input);
            if (newUser != null) {
                users.add(newUser);
            }
        }
    }

    /**
     * adds new milestone to the milestone list
     * @param milestone milestone to be added
     */
    public void addMilestone(final Milestone milestone) {
        milestones.add(milestone);
    }

    public List<Milestone> getMilestones() {
        return milestones;
    }

    /**
     * @param ticketId the ticket id
     * @return the milestone with the ticket corresponding with the ticket id
     */
    public Milestone isTicketInMilestone(final int ticketId) {
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

    /**
     * @param username the user name
     * @return the user corresponding to the name
     */
    public User findUser(final String username) {
        for (User user : this.users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
