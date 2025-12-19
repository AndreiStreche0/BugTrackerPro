package milestones;

import enums.BusinessPriority;
import enums.TicketStatus;
import enums.UserRole;
import notifications.Observers;
import processing.AppStorage;
import tickets.Ticket;
import users.Developer;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * milestone class that contains the milestone parameters and methods used to handle the milestone
 */
public final class Milestone extends Observers {
    private String name;
    private List<String> blockingFor;
    private String dueDate;
    private String createdAt;
    private List<Integer> ticketIds;
    private List<String> assignedDevs;
    private String createdBy;
    private final AppStorage appStorage;
    private final Set<String> blockers = new HashSet<>();
    private LocalDate lastAutomaticUpdate;
    private boolean warnedOfDeadline;

    public Milestone(final String name, final List<String> blockingFor, final String dueDate,
                     final String createdAt, final List<Integer> ticketIds,
                     final List<String> assignedDevs, final String createdBy) {
        this.name = name;
        this.blockingFor = blockingFor;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.ticketIds = ticketIds;
        this.assignedDevs = assignedDevs;
        this.createdBy = createdBy;
        this.appStorage = AppStorage.getInstance();
        this.warnedOfDeadline = false;
    }

    public boolean isWarnedOfDeadline() {
        return warnedOfDeadline;
    }

    public void setWarnedOfDeadline(final boolean warnedOfDeadline) {
        this.warnedOfDeadline = warnedOfDeadline;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
    public LocalDate getLastAutomaticUpdate() {
        return lastAutomaticUpdate;
    }

    public void setLastAutomaticUpdate(final LocalDate lastAutomaticUpdate) {
        this.lastAutomaticUpdate = lastAutomaticUpdate;
    }

    public List<String> getBlockingFor() {
        return blockingFor;
    }

    public void setBlockingFor(final List<String> blockingFor) {
        this.blockingFor = blockingFor;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(final String dueDate) {
        this.dueDate = dueDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final String createdAt) {
        this.createdAt = createdAt;
    }

    public List<Integer> getTicketIds() {
        return ticketIds;
    }

    public void setTicketIds(final List<Integer> ticketIds) {
        this.ticketIds = ticketIds;
    }

    public List<String> getAssignedDevs() {
        return assignedDevs;
    }

    public void setAssignedDevs(final List<String> assignedDevs) {
        this.assignedDevs = assignedDevs;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * adds a new blocker to the blockers list
     * @param milestoneName milestone blocker name
     */
    public void addBlocker(final String milestoneName) {
        this.blockers.add(milestoneName);
    }

    /**
     * removes a blocker milestone for the blockers list
     * @param milestoneName milestone blocker name
     */
    public void removeBlocker(final String milestoneName) {
        if (!this.blockers.isEmpty()) {
            this.blockers.remove(milestoneName);
        }
    }

    public boolean isBlocked() {
        return !blockers.isEmpty();
    }

    /**
     * sends notifications if the deadline is coming
     */
    public void deadlineIsComing() {
        for (int id : this.getTicketIds()) {
            Ticket ticket = appStorage.getTicket(id);
            if (ticket.getState() != TicketStatus.CLOSED) {
                ticket.setBusinessPriority(BusinessPriority.CRITICAL);
            }
            if (ticket.getAssignedTo() != null && ticket.getAssignedTo().getRole()
                    == UserRole.DEVELOPER) {
                if (!((Developer) ticket.getAssignedTo()).hasRequiredSeniority(ticket.getBusinessPriority())) {
                    ticket.setState(TicketStatus.OPEN);
                    ticket.setAssignedTo(null);
                    ticket.setAssignedAt(null);
                }
            }
        }
        this.sendNotifications("Milestone " + this.name + " is due tomorrow. All unresolved"
                + " tickets are now CRITICAL.");
    }

    /**
     * increments priority of milestone tickets
     * @param newLastUpdateDate changes when the ticket was last updated
     */
    public void incrementPriority(final LocalDate newLastUpdateDate) {
        this.lastAutomaticUpdate = newLastUpdateDate;
        for (int id : this.getTicketIds()) {
            Ticket ticket = appStorage.getTicket(id);
            if (ticket.getState() == TicketStatus.CLOSED) {
                continue;
            }
            switch (ticket.getBusinessPriority()) {
                case LOW:
                    ticket.setBusinessPriority(BusinessPriority.MEDIUM);
                    break;
                case MEDIUM:
                    ticket.setBusinessPriority(BusinessPriority.HIGH);
                    break;
                case HIGH:
                    ticket.setBusinessPriority(BusinessPriority.CRITICAL);

                    break;
                default:
                    continue;
            }
            if (ticket.getAssignedTo() != null && ticket.getAssignedTo().getRole()
                    == UserRole.DEVELOPER) {
                Developer dev = (Developer) ticket.getAssignedTo();

                if (!dev.hasRequiredSeniority(ticket.getBusinessPriority())) {
                    ticket.setState(TicketStatus.OPEN);
                    ticket.setAssignedTo(null);
                    ticket.setAssignedAt(null);
                }
            }
        }
    }

    /**
     * @param ticket the ticket
     * @param developer the developer
     * @return if the ticket is assignable to that developer
     */
    public boolean isTicketAssignable(final Ticket ticket, final Developer developer) {
        if (this.isBlocked()) {
            return false;
        }
        if (ticket.getState() != TicketStatus.OPEN) {
            return false;
        }
        if (!assignedDevs.contains(developer.getUsername())) {
            return false;
        }
        if (!developer.hasRequiredSeniority(ticket.getBusinessPriority())) {
            return false;
        }
        if (!developer.hasRequiredExpertise(ticket.getExpertiseArea())) {
            return false;
        }
        return true;
    }

    /**
     * @return if every ticket is completed
     */
    public boolean isCompleted() {
        for (int id : this.ticketIds) {
            Ticket ticket = appStorage.getTicket(id);
            if (ticket != null && !ticket.getState().equals(TicketStatus.CLOSED)) {
                return false;
            }
        }
        return true;
    }
}
