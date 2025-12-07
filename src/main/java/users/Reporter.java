package users;

import enums.UserRole;

public class Reporter extends User {
    public Reporter(String username, String email) {
        super(username, email, UserRole.REPORTER);
    }
}
