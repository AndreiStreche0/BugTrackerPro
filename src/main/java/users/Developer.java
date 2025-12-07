package users;

import enums.ExpertiseArea;
import enums.Seniority;
import enums.UserRole;

import java.util.ArrayList;

public class Developer extends User {
    private ExpertiseArea expertise;
    private Seniority seniority;
    private String hireDate;

    public Developer(String username, String email, ExpertiseArea expertise,
                     Seniority seniority, String hireDate) {
        super(username, email, UserRole.DEVELOPER);
        this.expertise = expertise;
        this.seniority = seniority;
        this.hireDate = hireDate;
    }

    public ExpertiseArea getExpertise() {
        return expertise;
    }
    public Seniority getSeniority() {
        return seniority;
    }

}
