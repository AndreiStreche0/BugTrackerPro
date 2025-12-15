package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import enums.BusinessPriority;
import enums.Seniority;
import enums.TicketStatus;
import enums.UserRole;
import processing.AppStorage;
import tickets.Ticket;
import users.Developer;
import users.Manager;
import users.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GeneratePerformanceReportCommand extends Command {
    private final ObjectMapper mapper = new ObjectMapper();
    private final AppStorage appStorage;

    public GeneratePerformanceReportCommand(String username, String timestamp) {
        super("generatePerformanceReport", username, timestamp);
        this.appStorage = AppStorage.getInstance();
    }
    private ObjectNode createErrorNode(String message) {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "generatePerformanceReport");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        output.put("error", message);
        return output;
    }
    private double calculateDiversityFactor(int bugCnt, int featureCnt, int UICnt) {
        double avg = (bugCnt + featureCnt + UICnt) / 3.0;
        if (avg == 0) return 0.0;
        double variance = (Math.pow(bugCnt - avg, 2) + Math.pow(featureCnt - avg, 2) + Math.pow(UICnt - avg, 2)) / 3.0;
        return Math.sqrt(variance) / avg;
    }
    @Override
    public ObjectNode execute(boolean correctPhase) {
        User user = appStorage.findUser(super.getUsername());
        ObjectNode output = mapper.createObjectNode();
        ArrayNode outputArray = mapper.createArrayNode();
        if (user == null) {
            return createErrorNode("The user " + super.getUsername() + " does not exist.");
        }
        if (user.getRole() != UserRole.MANAGER) {
            return createErrorNode("The user does not have permission to execute this command:" +
                    " required role MANAGER; user role " + user.getRole() + ".");
        }

        List<Developer> developers = new ArrayList<>();
        Manager manager = (Manager) user;
        for (String developerName : manager.getSubordinates()) {
            developers.add((Developer) appStorage.findUser(developerName));
        }
        developers.sort(Comparator.comparing(User::getUsername));
        LocalDate prevMonthDate = LocalDate.parse(super.getTimestamp()).minusMonths(1);
        output.put("command", "generatePerformanceReport");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        for (Ticket ticket : appStorage.getTickets()) {

            if (ticket.getAssignedTo() != null) {
                System.out.println("developers is: " + ticket.getAssignedTo().getUsername() + " and has the tickets: ");
                System.out.println(ticket.getTitle() + " with id " + ticket.getId() + " and state " + ticket.getState());
            }else {
                System.out.println("developers not existent " + " and has the tickets: ");
                System.out.println("ticket not assigned " +  " with id " + ticket.getId() + " and state " + ticket.getState());

            }
        }
        for (Developer developer : developers) {
            int cntBug = 0, cntFeatureReq = 0, cntUIFeedback = 0;
            int highPriorityTickets = 0;
            int totalDays = 0;
            for (Ticket ticket : developer.getAssignedTickets()) {
                if (ticket.getState() == TicketStatus.CLOSED && ticket.getSolvedAt() != null) {
                    LocalDate solved = LocalDate.parse(ticket.getSolvedAt());
                    if (solved.getMonthValue() == prevMonthDate.getMonthValue() &&
                            solved.getYear() == prevMonthDate.getYear()) {
                        switch (ticket.getType()) {
                            case BUG -> cntBug++;
                            case FEATURE_REQUEST -> cntFeatureReq++;
                            case UI_FEEDBACK -> cntUIFeedback++;
                        }
                        if (ticket.getBusinessPriority() == BusinessPriority.HIGH ||
                                ticket.getBusinessPriority() == BusinessPriority.CRITICAL) {
                            highPriorityTickets++;
                        }
                        totalDays += (int) (ChronoUnit.DAYS.between(LocalDate.parse(
                                ticket.getAssignedAt()), LocalDate.parse(ticket.getSolvedAt()))
                                + 1);
                    }
                }
            }
            int totalClosedTickets = cntBug + cntUIFeedback + cntFeatureReq;
            double score = 0.0, averageSolving = (double) totalDays / totalClosedTickets;
            if (totalClosedTickets > 0) {
                if (developer.getSeniority() == Seniority.JUNIOR) {
                    double ticketDiversity = this.calculateDiversityFactor(cntBug, cntFeatureReq, cntUIFeedback);
                    score = Math.max(0, 0.5 * totalClosedTickets - ticketDiversity) + 5;
                } else if (developer.getSeniority() == Seniority.MID) {
                    score = Math.max(0, 0.5 * totalClosedTickets + 0.7 * highPriorityTickets - 0.3 * averageSolving) + 15;
                } else if (developer.getSeniority() == Seniority.SENIOR) {
                    score = Math.max(0, 0.5 * totalClosedTickets + 1.0 * highPriorityTickets - 0.5 * averageSolving) + 30;
                }
            }
            developer.setPerformanceScore(score);
            ObjectNode developerOutput = mapper.createObjectNode();
            developerOutput.put("username", developer.getUsername());
            developerOutput.put("closedTickets", totalClosedTickets);
            developerOutput.put("averageResolutionTime", Math.round(averageSolving * 100.0) / 100.0);
            developerOutput.put("performanceScore", Math.round(score * 100.0) / 100.0);
            developerOutput.put("seniority", developer.getSeniority().toString());
            outputArray.add(developerOutput);
        }
        output.set("report", outputArray);
        return output;
    }
}
