package processing;

import enums.AppPhase;
import factory.UserFactory;
import fileio.UserInput;
import main.App;
import tickets.Ticket;
import users.User;

import java.util.ArrayList;
import java.util.List;

public class AppStorage {
    private final static AppStorage INSTANCE = new AppStorage();
    private final List<Ticket> tickets = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private AppPhase appPhase;
    private AppStorage() {}

    public static AppStorage getInstance() {
        return INSTANCE;
    }
    public void addTicket(Ticket ticket) {
        ticket.setId(tickets.size());
        tickets.add(ticket);
    }

    public AppPhase getAppPhase() {
        return appPhase;
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
