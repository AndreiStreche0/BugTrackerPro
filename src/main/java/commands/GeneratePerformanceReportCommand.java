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

/**
 * generates the performance report
 */
public final class GeneratePerformanceReportCommand extends Command {
    private static final double CLOSED_TICKETS_MULTIPLIER = 0.5;
    private static final double MID_PRIORITY_MULTIPLIER = 0.7;
    private static final double SENIOR_PRIORITY_MULTIPLIER = 1.0;
    private static final double JUNIOR_BONUS_VALUE = 5.0;
    private static final double MID_BONUS_VALUE = 15.0;
    private static final double SENIOR_BONUS_VALUE = 30.0;
    private static final double MID_SOLVING_MULTIPLIER = 0.3;
    private static final double SENIOR_SOLVING_MULTIPLIER = 0.5;
    private static final double ROUNDER = 100.0;
    private static final double AVG_DIVIDER = 3.0;

    private final ObjectMapper mapper = new ObjectMapper();
    private final AppStorage appStorage;

    public GeneratePerformanceReportCommand(final String username, final String timestamp) {
        super("generatePerformanceReport", username, timestamp);
        this.appStorage = AppStorage.getInstance();
    }
    /**
     * creates error node for json output
     * @param message the string used for the node
     * @return the ObjectNode output
     */
    private ObjectNode createErrorNode(final String message) {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "generatePerformanceReport");
        output.put("username", super.getUsername());
        output.put("timestamp", super.getTimestamp());
        output.put("error", message);
        return output;
    }
    private double calculateDiversityFactor(final int bugCnt, final int featureCnt,
                                            final int userInterfaceCnt) {
        double avg = (bugCnt + featureCnt + userInterfaceCnt) / AVG_DIVIDER;
        if (avg == 0) {
            return 0.0;
        }
        double variance = (Math.pow(bugCnt - avg, 2) + Math.pow(featureCnt - avg, 2)
                + Math.pow(userInterfaceCnt - avg, 2)) / AVG_DIVIDER;
        return Math.sqrt(variance) / avg;
    }
    /**
     * generates the performance report and formats the output
     * @param correctPhase says if the command can be done in this phase
     * @return the output node
     */
    @Override
    public ObjectNode execute(final boolean correctPhase) {
        User user = appStorage.findUser(super.getUsername());
        ObjectNode output = mapper.createObjectNode();
        ArrayNode outputArray = mapper.createArrayNode();
        if (user == null) {
            return createErrorNode("The user " + super.getUsername() + " does not exist.");
        }
        if (user.getRole() != UserRole.MANAGER) {
            return createErrorNode("The user does not have permission to execute this command:"
                    + " required role MANAGER; user role " + user.getRole() + ".");
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
        for (Developer developer : developers) {
            int cntBug = 0, cntFeatureReq = 0, cntUIFeedback = 0;
            int highPriorityTickets = 0;
            int totalDays = 0;
            for (Ticket ticket : developer.getAssignedTickets()) {
                if (ticket.getState() == TicketStatus.CLOSED && ticket.getSolvedAt() != null) {
                    LocalDate solved = LocalDate.parse(ticket.getSolvedAt());
                    if (solved.getMonthValue() == prevMonthDate.getMonthValue()
                            && solved.getYear() == prevMonthDate.getYear()) {
                        switch (ticket.getType()) {
                            case BUG -> cntBug++;
                            case FEATURE_REQUEST -> cntFeatureReq++;
                            case UI_FEEDBACK -> cntUIFeedback++;
                            default -> {
                                continue;
                            }
                        }
                        if (ticket.getBusinessPriority() == BusinessPriority.HIGH
                                || ticket.getBusinessPriority() == BusinessPriority.CRITICAL) {
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
                    double ticketDiversity = this.calculateDiversityFactor(cntBug, cntFeatureReq,
                            cntUIFeedback);
                    score = Math.max(0, CLOSED_TICKETS_MULTIPLIER * totalClosedTickets
                            - ticketDiversity) + JUNIOR_BONUS_VALUE;
                } else if (developer.getSeniority() == Seniority.MID) {
                    score = Math.max(0, CLOSED_TICKETS_MULTIPLIER * totalClosedTickets
                            + MID_PRIORITY_MULTIPLIER * highPriorityTickets
                            - MID_SOLVING_MULTIPLIER * averageSolving) + MID_BONUS_VALUE;
                } else if (developer.getSeniority() == Seniority.SENIOR) {
                    score = Math.max(0, CLOSED_TICKETS_MULTIPLIER * totalClosedTickets
                            + SENIOR_PRIORITY_MULTIPLIER * highPriorityTickets
                            - SENIOR_SOLVING_MULTIPLIER * averageSolving) + SENIOR_BONUS_VALUE;
                }
            }
            developer.setPerformanceScore(score);
            ObjectNode developerOutput = mapper.createObjectNode();
            developerOutput.put("username", developer.getUsername());
            developerOutput.put("closedTickets", totalClosedTickets);
            developerOutput.put("averageResolutionTime", Math.round(averageSolving * ROUNDER)
                    / ROUNDER);
            developerOutput.put("performanceScore", Math.round(score * ROUNDER) / ROUNDER);
            developerOutput.put("seniority", developer.getSeniority().toString());
            outputArray.add(developerOutput);
        }
        output.set("report", outputArray);
        return output;
    }
}
