package processing;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import enums.AppPhase;
import enums.TicketStatus;
import milestones.Milestone;
import processing.states.ClosedPhaseState;
import processing.states.DevelopmentPhaseState;
import processing.states.StateSpecificActions;
import processing.states.TestingPhaseState;
import tickets.Ticket;
import tickets.TicketHistory;
import users.Developer;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * class that handles the app phase and ticket priority updating
 */
public final class ProjectProcessing {
    private static final int MIN_NUMBER_DAYS = 12;
    private static final int AVG_DIVIDER = 3;
    private StateSpecificActions currentState;
    private String startingDate;
    private final AppStorage appStorage;

    public ProjectProcessing(final String startDate) {
        this.currentState = new TestingPhaseState();
        this.startingDate = startDate;
        this.appStorage = AppStorage.getInstance();
    }

    /**
     * checks for automatic phase update
     * @param timestamp the current timestamp
     */
    public void automaticUpdatePhase(final String timestamp) {
        LocalDate start = LocalDate.parse(this.startingDate);
        LocalDate current = LocalDate.parse(timestamp);

        int daysBetween = (int) ChronoUnit.DAYS.between(start, current) + 1;
        if (daysBetween >= MIN_NUMBER_DAYS && currentState.getPhase() == AppPhase.TESTING_PHASE) {
            this.startDevelopingPhase(timestamp);
        }
    }

    /**
     * updates ticket priority for automatic time passing
     * @param currentTimestamp current time
     */
    public void ticketPriorityUpdate(final String currentTimestamp) {
        LocalDate current = LocalDate.parse(currentTimestamp);
        List<Milestone> milestones = appStorage.getMilestones();
        for (Milestone milestone: milestones) {
            LocalDate dueDate = LocalDate.parse(milestone.getDueDate());
            LocalDate lastChange = milestone.getLastAutomaticUpdate();
            if (!milestone.isBlocked() && current.isEqual(dueDate.minusDays(1))
                    && lastChange.isBefore(dueDate.minusDays(1))
                    && !milestone.isWarnedOfDeadline()) {
                milestone.deadlineIsComing();
                milestone.setWarnedOfDeadline(true);
            }

            int daysSinceCreationCurrent = (int) ChronoUnit.DAYS.between(lastChange, current);
            int increments = Math.max(0, daysSinceCreationCurrent / AVG_DIVIDER);
            LocalDate newUpdateDate = current.minusDays(daysSinceCreationCurrent % AVG_DIVIDER);
            for (int k = 0; k < increments; k++) {
                if (!milestone.isBlocked()) {
                    milestone.incrementPriority(newUpdateDate);
                }
            }
            if (increments > 0) {
                LocalDate newLastUpdate = lastChange.plusDays(increments * AVG_DIVIDER);
                milestone.setLastAutomaticUpdate(newLastUpdate);
            }
            if (!milestone.isBlocked()) {
                for (int ticketId : milestone.getTicketIds()) {
                    Ticket ticket = appStorage.getTicket(ticketId);

                    if (ticket != null && ticket.getAssignedTo() != null) {

                        Developer developer = (Developer) ticket.getAssignedTo();

                        if (!developer.hasRequiredSeniority(ticket.getBusinessPriority())) {
                            developer.removeAssignedTicket(ticket);
                            TicketHistory history = TicketHistory.systemRemove(
                                    developer.getUsername(), currentTimestamp);
                            ticket.addHistory(history);
                            ticket.setAssignedTo(null);
                            ticket.setState(TicketStatus.OPEN);
                        }
                    }
                }
            }
        }
    }

    /**
     * starts developing phase
     * @param startDate current date
     */
    public void startDevelopingPhase(final String startDate) {
        this.currentState = new DevelopmentPhaseState();
        this.startingDate = startDate;
    }

    /**
     * starts testing phase
     * @param startDate current date
     */
    public void startTestingPhase(final String startDate) {
        this.currentState = new TestingPhaseState();
        this.startingDate = startDate;
    }
    /**
     * closes app
     */
    public void closeApp(final String closingDate) {
        this.currentState = new ClosedPhaseState();
        this.startingDate = closingDate;
    }

    /**
     * does automatic updates before executing the command
     * @param command the command
     * @param timestamp the timestamp
     * @return output node of execution
     */
    public ObjectNode executeCommand(final Command command, final String timestamp) {
        automaticUpdatePhase(timestamp);
        ticketPriorityUpdate(timestamp);
        return currentState.executeCommand(command);
    }
}
