package filters;

import milestones.Milestone;
import processing.AppStorage;
import tickets.Ticket;
import users.Developer;

public class AvailableForAssignmentFilter implements Filter <Ticket> {
    AppStorage appStorage;
    Developer currentDeveloper;

    public AvailableForAssignmentFilter(Developer currentDeveloper) {
        this.currentDeveloper = currentDeveloper;
        this.appStorage = AppStorage.getInstance();
    }
    @Override
    public boolean filter(Ticket ticket) {
        if (appStorage.getMilestones() == null) {
            return false;
        }
        for (Milestone milestone : appStorage.getMilestones()) {
            if (milestone.getTicketIds().contains(ticket.getId())) {
                return milestone.isTicketAssignable(ticket, currentDeveloper);
            }
        }
        return false;
    }
}
