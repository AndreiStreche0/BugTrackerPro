package factories;

import enums.ExpertiseArea;
import enums.Seniority;
import fileio.UserInput;
import users.Developer;
import users.Manager;
import users.Reporter;
import users.User;

/**
 * user factory used to create new users
 */
public final class UserFactory {
    private UserFactory() {
        throw new UnsupportedOperationException("This is a utility class and can't be"
                + " instantiated");
    }
    /**
     * creates a new user based on its type
     * @param userInput the input containing user information
     * @return the new user
     */
    public static User createUser(final UserInput userInput) {
        String userRole = userInput.getRole();
        switch (userRole) {
            case "REPORTER":
                return new Reporter(userInput.getUsername(), userInput.getMail());
            case "DEVELOPER":
                ExpertiseArea expertise = ExpertiseArea.getType(userInput.getExpertiseArea());
                Seniority seniority = Seniority.getType(userInput.getSeniority());
                if (expertise == null || seniority == null) {
                    System.out.println("Error: required fields not found");
                    return null;
                }
                return new Developer(userInput.getUsername(), userInput.getMail(), expertise,
                        seniority, userInput.getHireDate());
            case "MANAGER":
                return new Manager(userInput.getUsername(), userInput.getMail(),
                        userInput.getSubordinates(), userInput.getHireDate());
            default:
                System.out.println("Wrong user role for " + userInput.getUsername());
                return null;
        }
    }
}
