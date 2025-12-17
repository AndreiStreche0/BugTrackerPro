package users;

import enums.UserRole;

import java.util.List;

public final class Manager extends User {
    private final List<String> subordinates;
    private final String hireDate;

    public Manager(final String username, final String email, final List<String> subordinates,
                   final String hireDate) {
        super(username, email, UserRole.MANAGER);
        this.subordinates = subordinates;
        this.hireDate = hireDate;
    }

    public List<String> getSubordinates() {
        return subordinates;
    }
}
