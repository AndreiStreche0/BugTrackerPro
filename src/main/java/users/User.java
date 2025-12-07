package users;

import enums.UserRole;

import java.util.ArrayList;
import java.util.List;

public abstract class User {
    protected String username;
    protected String email;
    protected UserRole role;
    protected List<String> notifications;

    public User(String username, String email, UserRole role) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.notifications = new ArrayList<>();
    }

    public void addNotification(String notification) {
        this.notifications.add(notification);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setNotifications(List<String> notifications) {
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
