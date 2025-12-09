package processing;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import enums.AppPhase;
import milestones.Milestone;
import processing.states.ClosedPhaseState;
import processing.states.DevelopmentPhaseState;
import processing.states.StateSpecificActions;
import processing.states.TestingPhaseState;
import tickets.Ticket;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ProjectProcessing {
    private StateSpecificActions currentState;
    private String startingDate;
    private final AppStorage appStorage;

    public ProjectProcessing (String startDate) {
        this.currentState = new TestingPhaseState();
        this.startingDate = startDate;
        this.appStorage = AppStorage.getInstance();
    }
    public void automaticUpdatePhase(String timestamp) {
        LocalDate start = LocalDate.parse(this.startingDate);
        LocalDate current = LocalDate.parse(timestamp);

        int daysBetween = (int) ChronoUnit.DAYS.between(start, current) + 1;
        if (daysBetween >= 12 && currentState.getPhase() == AppPhase.TESTING_PHASE) {
            this.startDevelopingPhase(timestamp);
        }
    }

    public void ticketPriorityUpdate(String currentTimestamp) {
        LocalDate current = LocalDate.parse(currentTimestamp);
        List<Milestone> milestones = appStorage.getMilestones();
        for (Milestone milestone: milestones) {
            if (milestone.isBlocked()) {
                continue;
            }
            LocalDate dueDate = LocalDate.parse(milestone.getDueDate());
            LocalDate lastChange = milestone.getLastAutomaticUpdate();
            if (!current.isBefore(dueDate.minusDays(1)) &&
                    lastChange.isBefore(dueDate.minusDays(1))) {
                milestone.deadlineIsComing();
            }

            int daysSinceCreationCurrent = (int) ChronoUnit.DAYS.between(lastChange, current) + 1;
            int increments = Math.max(0, daysSinceCreationCurrent / 3);
            LocalDate newUpdateDate = current.minusDays(daysSinceCreationCurrent % 3);
            for (int k = 0; k < increments; k++) {
                milestone.incrementPriority(newUpdateDate);
            }
        }
    }

    public void startDevelopingPhase(String startDate) {
        this.currentState = new DevelopmentPhaseState();
        this.startingDate = startDate;
    }
    public void startTestingPhase(String startDate) {
        this.currentState = new TestingPhaseState();
        this.startingDate = startDate;
    }
    public void closeApp(String closingDate) {
        this.currentState = new ClosedPhaseState();
        this.startingDate = closingDate;
    }
    public ObjectNode executeCommand(Command command, String timestamp) {
        automaticUpdatePhase(timestamp);
        ticketPriorityUpdate(timestamp);
        return currentState.executeCommand(command);
    }
}
