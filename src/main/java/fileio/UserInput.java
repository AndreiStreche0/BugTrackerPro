package fileio;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * class containing all the needed input for users
 */
@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class UserInput {
    private String username;
    private String mail;
    private String role;
    private String hireDate;
    private String expertiseArea;
    private String seniority;
    private String email;
    private List<String> subordinates;

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(final String mail) {
        this.mail = mail;
    }

    public String getRole() {
        return role;
    }

    public void setRole(final String role) {
        this.role = role;
    }

    public String getHireDate() {
        return hireDate;
    }

    public void setHireDate(final String hireDate) {
        this.hireDate = hireDate;
    }

    public String getExpertiseArea() {
        return expertiseArea;
    }

    public void setExpertiseArea(final String expertiseArea) {
        this.expertiseArea = expertiseArea;
    }

    public String getSeniority() {
        return seniority;
    }

    public void setSeniority(final String seniority) {
        this.seniority = seniority;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public List<String> getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(final List<String> subordinates) {
        this.subordinates = subordinates;
    }
}
