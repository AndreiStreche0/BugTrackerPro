package milestones;

import enums.BusinessPriority;
import enums.Seniority;
import enums.TicketStatus;
import notifications.Observers;
import processing.AppStorage;
import tickets.Ticket;
import tickets.TicketHistory;
import users.Developer;
import users.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Milestone extends Observers {
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

    public Milestone(String name, List<String> blockingFor, String dueDate,
                     String createdAt, List<Integer> ticketIds,
                     List<String> assignedDevs, String createdBy) {
        this.name = name;
        this.blockingFor = blockingFor;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.ticketIds = ticketIds;
        this.assignedDevs = assignedDevs;
        this.createdBy = createdBy;
        this.appStorage = AppStorage.getInstance();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public LocalDate getLastAutomaticUpdate() {
        return lastAutomaticUpdate;
    }

    public void setLastAutomaticUpdate(LocalDate lastAutomaticUpdate) {
        this.lastAutomaticUpdate = lastAutomaticUpdate;
    }

    public List<String> getBlockingFor() {
        return blockingFor;
    }

    public void setBlockingFor(List<String> blockingFor) {
        this.blockingFor = blockingFor;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<Integer> getTicketIds() {
        return ticketIds;
    }

    public void setTicketIds(List<Integer> ticketIds) {
        this.ticketIds = ticketIds;
    }

    public List<String> getAssignedDevs() {
        return assignedDevs;
    }

    public void setAssignedDevs(List<String> assignedDevs) {
        this.assignedDevs = assignedDevs;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public void addBlocker(String milestoneName) {
        this.blockers.add(milestoneName);
    }

    public void removeBlocker(String milestoneName) {
        this.blockers.remove(milestoneName);
    }

    public boolean isBlocked() {
        return !blockers.isEmpty();
    }

    public void deadlineIsComing() {
        for (int id : this.getTicketIds()) {
            Ticket ticket = appStorage.getTicket(id);
            ticket.setBusinessPriority(BusinessPriority.CRITICAL);
            this.sendNotifications("Milestone " + this.name + "is due tomorrow. All unresolved" +
                    " tickets are now CRITICAL.");
        }
    }

    public void incrementPriority(LocalDate newLastUpdateDate) {
        this.lastAutomaticUpdate = newLastUpdateDate;
        for (int id : this.getTicketIds()) {
            Ticket ticket = appStorage.getTicket(id);
            switch (ticket.getBusinessPriority()) {
                case LOW:
                    ticket.setBusinessPriority(BusinessPriority.MEDIUM);
                    for (String developerName : this.assignedDevs) {
                        User user = appStorage.findUser(developerName);
                        if (((Developer) user).getSeniority() == Seniority.JUNIOR) {
                            ticket.setState(TicketStatus.OPEN);
                        }
                    }
                    break;
                case MEDIUM:
                    ticket.setBusinessPriority(BusinessPriority.HIGH);
                    for (String developerName : this.assignedDevs) {
                        User user = appStorage.findUser(developerName);
                        if (((Developer) user).getSeniority() == Seniority.JUNIOR ||
                                ((Developer) user).getSeniority() == Seniority.MID) {
                            ticket.setState(TicketStatus.OPEN);
                        }
                    }
                    break;
                case HIGH:
                    ticket.setBusinessPriority(BusinessPriority.CRITICAL);

                    break;
                default:
                    continue;
            }
        }
    }
    public boolean isTicketAssignable (Ticket ticket, Developer developer) {
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
}