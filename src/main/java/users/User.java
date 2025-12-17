package users;

import enums.UserRole;
import notifications.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * User class that has all the fields and notification logic common for all user types
 */
public abstract class User implements Observer {
    private String username;
    private String email;
    private UserRole role;
    private List<String> notifications;

    public User(final String username, final String email, final UserRole role) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.notifications = new ArrayList<>();
    }

    public void addNotification(final String notification) {
        this.notifications.add(notification);
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    /**
     * adds new notification to the notification list
     * @param notification the notification being sent
     */
    @Override
    public void update(final String notification) {
        this.notifications.add(notification);
    }
    public void setEmail(final String email) {
        this.email = email;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setNotifications(final List<String> notifications) {
        this.notifications = notifications;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public String getUsername() {
        return username;
    }
    public UserRole getRole() {
        return role;
    }
}
