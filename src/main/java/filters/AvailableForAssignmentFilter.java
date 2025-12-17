package filters;

import milestones.Milestone;
import processing.AppStorage;
import tickets.Ticket;
import users.Developer;

/**
 * filter that checks if a ticket is available for assignment
 */
public class AvailableForAssignmentFilter implements Filter <Ticket> {
    AppStorage appStorage;
    Developer currentDeveloper;

    public AvailableForAssignmentFilter(Developer currentDeveloper) {
        this.currentDeveloper = currentDeveloper;
        this.appStorage = AppStorage.getInstance();
    }

    /**
     * checks if the ticket is assignable using a method in the milestone class
     * @param ticket the ticket that is being filtered
     * @return true if the ticket passed the filter
     */
    @Override
    public boolean filter(final Ticket ticket) {
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
