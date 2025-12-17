package users;

import enums.UserRole;

public final class Reporter extends User {
    public Reporter(final String username, final String email) {
        super(username, email, UserRole.REPORTER);
    }
}
