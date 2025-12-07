package users;

import enums.UserRole;

import java.util.List;

public class Manager extends User {
    private List<String> subordinates;
    private String hireDate;

    public Manager(String username, String email, List<String> subordinates, String hireDate) {
        super(username, email, UserRole.MANAGER);
        this.subordinates = subordinates;
        this.hireDate = hireDate;
    }

    public List<String> getSubordinates() {
        return subordinates;
    }
}
