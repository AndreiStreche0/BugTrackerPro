package processing;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import enums.AppPhase;
import processing.states.ClosedPhaseState;
import processing.states.DevelopmentPhaseState;
import processing.states.StateSpecificActions;
import processing.states.TestingPhaseState;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ProjectProcessing {
    private StateSpecificActions currentState;
    private String startingDate;
    public ProjectProcessing (String startDate) {
        this.currentState = new TestingPhaseState();
        this.startingDate = startDate;
    }
    public void automaticUpdatePhase(String timestamp) {
        LocalDate start = LocalDate.parse(this.startingDate);
        LocalDate current = LocalDate.parse(timestamp);

        int daysBetween = (int) ChronoUnit.DAYS.between(start, current) + 1;
        if (daysBetween >= 12 && currentState.getPhase() == AppPhase.TESTING_PHASE) {
            this.startDevelopingPhase(timestamp);
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
        return currentState.executeCommand(command);
    }
}
